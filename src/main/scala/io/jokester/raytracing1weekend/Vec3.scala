package io.jokester.raytracing1weekend

case class Vec3(val x: Double, val y: Double, val z: Double) {
  def +(that: Vec3) = Vec3(x + that.x, y + that.y, z + that.z)
  def -(that: Vec3): Vec3 = Vec3(x - that.x, y - that.y, z - that.z)
  def *(m: Double): Vec3 = Vec3(x * m, y * m, z * m)
  def /(d: Double): Vec3 = Vec3(x / d, y / d, z / d)
  def length: Double = Math.sqrt(squareSum)
  def squareSum = x * x + y * y + z * z
}
