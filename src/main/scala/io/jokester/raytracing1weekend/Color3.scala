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

object Color3 {
  def mean(samples: Seq[Color3]): Color3 = {
    val sumR = samples.map(_.r).sum
    val sumG = samples.map(_.g).sum
    val sumB = samples.map(_.b).sum
    Color3(
      sumR / samples.size,
      sumG / samples.size,
      sumB / samples.size
    )
  }
}
