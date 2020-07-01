package io.jokester.raytracing1weekend

import java.awt.Graphics2D

class Scene(focal: Double) {
  private var models = List.empty[Model]

  def addModel(m: Model): Unit = {}

  def gradientColor(ray: Ray): Color3 = {
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

  def sphereColor(ray: Ray): Option[Color3] = {
    val sphere   = Sphere(Vec3(0, 0, -1), 0.5)
    val maybeHit = sphere.hitBy(ray, 0, 1e6)
    for (hit <- maybeHit)
      yield {
        val n = hit.normal.unit
        Color3(
          (n.x + 1) / 2,
          (n.y + 1) / 2,
          (n.z + 1) / 2
        )
      }
  }

  def rayColor(ray: Ray): Color3 = sphereColor(ray).getOrElse(gradientColor(ray))

  /**
    *
    * @param x ranged [-w/2, w/2]
    * @param y
    */
  private def drawPixel(canvas: Graphics2D, x: Int, y: Int, color: Color3): Unit = {
    canvas.setColor(color.toColor())
    canvas.drawRect(x, y, 1, 1)
  }

  def drawTo(canvasW: Int, canvasH: Int, canvas: Graphics2D) = {
    val aspectRatio = canvasW.toDouble / canvasH
    val viewportH   = 2.0
    val viewportW   = viewportH * aspectRatio
  }

}
