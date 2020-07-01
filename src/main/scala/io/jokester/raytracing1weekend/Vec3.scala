package io.jokester.raytracing1weekend

import java.awt.Color

case class Vec3(val x: Double, val y: Double, val z: Double) {
  def +(that: Vec3)       = Vec3(x + that.x, y + that.y, z + that.z)
  def -(that: Vec3): Vec3 = Vec3(x - that.x, y - that.y, z - that.z)
  def *(m: Double): Vec3  = Vec3(x * m, y * m, z * m)
  def /(d: Double): Vec3  = Vec3(x / d, y / d, z / d)

  def length: Double = Math.sqrt(squareSum)
  def squareSum      = dot(this)
  def unit           = this / length

  def dot(that: Vec3): Double = x * that.x + y * that.y + z * that.z
  def cross(that: Vec3): Vec3 =
    Vec3(
      y * that.z - z * that.y,
      z * that.x - x * that.z,
      x * that.y - y * that.x
    )
  def cosTo(that: Vec3): Double = dot(that) / length / that.length
}

object VecOperators {
  case class WrappedDouble(d: Double) extends AnyVal {
    def *(v: Vec3): Vec3 = v * d
  }
}

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

case class Ray(origin: Vec3, direction: Vec3) {
  def at(t: Double) = origin + direction * t
}
