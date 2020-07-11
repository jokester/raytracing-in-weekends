package io.jokester.raytracing1weekend

import scala.util.Random

object Threaded {
  private val _random: ThreadLocal[Random] = ThreadLocal.withInitial(() => new Random())
  def random: Random                       = _random.get()

  @scala.annotation.tailrec
  def randomUnit(): Vec3 = {
    val r = Vec3(
      randomDouble(-1, 1),
      randomDouble(-1, 1),
      randomDouble(-1, 1)
    )
    if (r.length < 1) r else randomUnit()
  }

  @inline
  private def randomDouble(min: Double, max: Double): Double =
    min + (max - min) * random.nextDouble()

  def randomMsaaOffsets(sampleCount: Int): Seq[(Double, Double)] =
    (0 until sampleCount).map(_ => (random.nextDouble, random.nextDouble))
}
