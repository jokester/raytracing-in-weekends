package io.jokester.raytracing1weekend

import scala.util.Random

object Threaded {
  private val _random: ThreadLocal[Random] = ThreadLocal.withInitial(() => new Random())
  def random: Random                       = _random.get()

  @scala.annotation.tailrec
  def randomUnitSphere(): Vec3 = {
    val r = Vec3(
      randomDouble(-1, 1),
      randomDouble(-1, 1),
      randomDouble(-1, 1)
    )
    if (r.length < 1) r else randomUnitSphere()
  }

  def randomUnit(): Vec3 = {
    val a = randomDouble(0, Math.PI * 2)
    val z = randomDouble(-1, 1)
    val r = Math.sqrt(1 - z * z)
    Vec3(r * Math.cos(a), r * Math.sin(a), z)
  }

  def randomInHemisphere(normal: Vec3): Vec3 = {
    val r = randomUnitSphere()
    if (r.dot(normal) > 0) {
      r
    } else {
      r * -1
    }
  }

  @inline
  private def randomDouble(min: Double, max: Double): Double =
    min + (max - min) * random.nextDouble()

  def randomMsaaOffsets(sampleCount: Int): Seq[(Double, Double)] =
    (0 until sampleCount).map(_ => (random.nextDouble, random.nextDouble))
}
