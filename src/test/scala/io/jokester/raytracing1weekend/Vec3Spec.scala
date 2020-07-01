package io.jokester.raytracing1weekend

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Vec3Spec extends AnyFlatSpec with Matchers {

  "Vec3" should "provide basic Vector arithmetic" in {
    Vec3(1, 2, 3) + Vec3(0.5, 1.5, 2.5) shouldEqual Vec3(1.5, 3.5, 5.5)
    Vec3(1, 2, 3) - Vec3(3, 2, 1) shouldEqual Vec3(-2, 0, 2)
    Vec3(3, 5, 4) * 2 shouldEqual Vec3(6, 10, 8)

    Vec3(0, 3, 4).length shouldEqual 5.0
    Vec3(3,4, 0).unit shouldEqual Vec3(0.6, 0.8, 0)

    Vec3(1, 2, 3) / 2 shouldEqual Vec3(0.5, 1, 1.5)

    Vec3(1, 2, 3) dot Vec3(2, 3, 4) shouldEqual 20
  }

}
