package io.jokester.raytracing1weekend

import java.awt.Graphics2D

class Scene(focal: Double, canvasW: Int, canvasH: Int) {
  private var models = List.empty[Hittable]

  def addModel(m: Hittable): this.type = {
    models ::= m
    this
  }

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

  def rayColor(ray: Ray): Color3 = {
    val hitWithSmallestT: Option[(HitRecord, Hittable)] = models
      .flatMap(m => m.hitBy(ray, 0, Double.MaxValue).map(h => (h, m)))
      .filter(_._1.t >= 0)
      .sortBy(_._1.t)
      .headOption

    hitWithSmallestT.map(t => t._2.colorAt(t._1)).getOrElse(gradientBackground(ray))
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

  def drawTo(canvas: Graphics2D) = {
    val aspectRatio = canvasW.toDouble / canvasH
    val viewportH   = 2.0
    val viewportW   = viewportH * aspectRatio

    val origin     = Vec3(0, 0, 0)
    val horizontal = Vec3(viewportW, 0, 0)
    val vertical   = Vec3(0, viewportH, 0)
    val lowerLeft  = origin - (horizontal / 2) - (vertical / 2) - Vec3(0, 0, focal)

    val msaa2x = List(
      (0.25, 0.25),
      (0.25, 0.75),
      (0.75, 0.25),
      (0.75, 0.75)
    )

    val noAntiAlias = List(
      (0.5, 0.5)
    )

    for (pixelI <- 0 until canvasW; pixelJ <- 0 until canvasH) {
      val samples = msaa2x.map(dij => {
        val (di, dj) = dij
        val i        = pixelI + di
        val j        = pixelJ + dj
        val u        = i.toDouble / (canvasW - 1) // 0 => 1
        val v        = j.toDouble / (canvasH - 1) // 0 => 1
        val pixel    = lowerLeft + horizontal * u + vertical * v
        val ray      = Ray(origin, pixel - origin)
        rayColor(ray)
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
