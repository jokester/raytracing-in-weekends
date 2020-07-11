package io.jokester.raytracing1weekend

import java.awt.Graphics2D

import com.typesafe.scalalogging.LazyLogging

import scala.util.Random

class Scene(focal: Double, canvasW: Int, canvasH: Int, msaaCount: Int, models: Seq[Hittable])
    extends LazyLogging {

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

    val hitWithSmallestT: Option[HitRecord] = world.hitBy(ray, 0, Double.MaxValue)

    if (hitWithSmallestT.isEmpty) return gradientBackground(ray)

    hitWithSmallestT
      .map(hit => {
        val reflectionTarget = hit.hitAt + hit.normal + Vec3.randomUnit()
        rayColor(Ray(hit.hitAt, reflectionTarget), depth - 1)
      })
      .getOrElse(gradientBackground(ray))
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
    canvas.drawRect(x, canvasH - 1 - y, 1, 1)
  }

  private def fixedMsaaOffsets(t: Int): Seq[(Double, Double)] = {
    for (x <- 0 until t; y <- 0 until t) yield ((x + 1).toDouble / t, (y + 1).toDouble / t)
  }

  private def randomMsaaOffsets(sampleCount: Int): Seq[(Double, Double)] =
    (0 until sampleCount).map(_ => (Random.nextDouble, Random.nextDouble))

  def drawTo(canvas: Graphics2D): Unit = {
    val aspectRatio = canvasW.toDouble / canvasH
    val viewportH   = 2.0
    val viewportW   = viewportH * aspectRatio

    val origin     = Vec3(0, 0, 0)
    val horizontal = Vec3(viewportW, 0, 0)
    val vertical   = Vec3(0, viewportH, 0)
    val lowerLeft  = origin - (horizontal / 2) - (vertical / 2) - Vec3(0, 0, focal)

    for (pixelI <- 0 until canvasW; pixelJ <- 0 until canvasH) {
      val samples = randomMsaaOffsets(msaaCount).map(dij => {
        val (di, dj) = dij
        val i        = pixelI + di
        val j        = pixelJ + dj
        val u        = i.toDouble / (canvasW - 1) // 0 => 1
        val v        = j.toDouble / (canvasH - 1) // 0 => 1
        val pixel    = lowerLeft + horizontal * u + vertical * v
        val ray      = Ray(origin, pixel - origin)
        rayColor(ray, 50)
      })

      drawPixel(
        canvas,
        pixelI,
        pixelJ,
        Color3.mean(samples)
      )
    }
  }
}
