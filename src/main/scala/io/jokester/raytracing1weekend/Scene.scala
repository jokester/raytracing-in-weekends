package io.jokester.raytracing1weekend

import java.awt.Graphics2D
import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

case class SceneMetrics(focal: Double, canvasW: Int, canvasH: Int) {
  val aspectRatio: Double = canvasW.toDouble / canvasH
  val viewportH: Double   = 2.0
  val viewportW: Double   = viewportH * aspectRatio

  val origin: Vec3     = Vec3(0, 0, 0)
  val horizontal: Vec3 = Vec3(viewportW, 0, 0)
  val vertical: Vec3   = Vec3(0, viewportH, 0)
  val lowerLeft: Vec3  = origin - (horizontal / 2) - (vertical / 2) - Vec3(0, 0, focal)
}

class Scene(metrics: SceneMetrics, msaaCount: Int, models: Seq[Hittable]) extends LazyLogging {

  def gradientBackground(ray: Ray): Color3 = {
    val unitDir = ray.direction.unit
    val t       = 0.5 * (unitDir.y + 1)
    // y=1 => t = 1 => rgba(0,5, 0.7, 1)
    // y=-1 => t=0 => rgba(1,1,1)

    Color3(
      (1 - t) + 0.5 * t,
      (1 - t) + 0.7 * t,
      (1 - t) + 1 * t
    )
  }

  lazy private val world = World(models)

  def rayColor(ray: Ray, depth: Int): Color3 = {
    if (depth < 0) return Color3(0, 0, 0)

    val hitWithSmallestT: Option[HitRecord] = world.hitBy(ray, 0.00001, Double.MaxValue)

    hitWithSmallestT
      .map(hitRecord => {
        hitRecord.material
          .scatter(ray, hitRecord)
          .map(scatterRecord => {
            scatterRecord.attenuation * rayColor(scatterRecord.scattered, depth - 1)
          })
          .getOrElse(Color3.black)
      })
      .getOrElse(gradientBackground(ray))
  }

  def nextRay1(hit: HitRecord): Ray = {
    val reflectionTarget = hit.hitAt + hit.normal + Threaded.randomUnit()
    Ray(hit.hitAt, reflectionTarget - hit.hitAt)
  }

  def nextRay2(hit: HitRecord): Ray = {
    val reflectionTarget = hit.hitAt + Threaded.randomInHemisphere(hit.normal)
    Ray(hit.hitAt, reflectionTarget - hit.hitAt)
  }

  def normalColor(normal: Vec3): Color3 = {
    val n = normal.unit
    Color3(
      (n.x + 1) / 2,
      (n.y + 1) / 2,
      (n.z + 1) / 2
    )
  }

  /**
    *
    * @param x ranged [-w/2, w/2]
    * @param y
    */
  private def drawPixel(canvas: Graphics2D, x: Int, y: Int, color: Color3): Unit = {
    canvas.setColor(color.toColor())
    canvas.drawRect(x, /* need a flip */ metrics.canvasH - 1 - y, 1, 1)
  }

  private def fixedMsaaOffsets(t: Int): Seq[(Double, Double)] = {
    for (x <- 0 until t; y <- 0 until t) yield ((x + 1).toDouble / t, (y + 1).toDouble / t)
  }

  private def renderPixel(pixelI: Int, pixelJ: Int): Color3 = {
    val samples = for (dij <- Threaded.randomMsaaOffsets(msaaCount)) yield {
      val (di, dj) = dij
      val i        = pixelI + di
      val j        = pixelJ + dj
      val u        = i.toDouble / (metrics.canvasW - 1) // 0 => 1
      val v        = j.toDouble / (metrics.canvasH - 1) // 0 => 1
      val pixel    = metrics.lowerLeft + metrics.horizontal * u + metrics.vertical * v
      val ray      = Ray(metrics.origin, pixel - metrics.origin)
      rayColor(ray, 50)
    }
    Color3.mean(samples).gammaCorrection()
  }

  private def renderPixelThreaded(pixelI: Int, pixelJ: Int)(implicit
      executionContext: ExecutionContext
  ): Future[(Int, Int, Color3)] = {
    Future((pixelI, pixelJ, renderPixel(pixelI, pixelJ)))
  }

  def renderCanvas(canvas: Graphics2D): Unit = {
    for (pixelI <- 0 until metrics.canvasW; pixelJ <- 0 until metrics.canvasH) {
      drawPixel(canvas, pixelI, pixelJ, renderPixel(pixelI, pixelJ))
    }
  }

  def renderCanvasThreaded(
      canvas: Graphics2D
  )(implicit executionContext: ExecutionContext): Unit = {
    val pixelsF: Seq[Future[(Int, Int, Color3)]] =
      for (pixelI <- 0 until metrics.canvasW; pixelJ <- 0 until metrics.canvasH) yield {
        renderPixelThreaded(pixelI, pixelJ)
      }

    val pixels = Await.result(Future.sequence(pixelsF), Duration(36000, TimeUnit.SECONDS))

    pixels.foreach(p => {
      val (pixelI, pixelJ, color) = p;
      drawPixel(canvas, pixelI, pixelJ, color)
    })

  }
}
