package io.jokester.raytracing1weekend

import java.awt.Color

case class Color3(r: Double, g: Double, b: Double) {
  @inline
  private def clip(min: Double, max: Double, v: Double) =
    if (v > max) max else if (v < min) min else v

  def toColor(): Color =
    new Color(
      clip(0, 1, r).toFloat,
      clip(0, 1, g).toFloat,
      clip(0, 1, b).toFloat
    )
}
