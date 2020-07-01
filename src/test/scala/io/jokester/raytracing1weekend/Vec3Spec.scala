package io.jokester.raytracing1weekend

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Vec3Spec extends AnyFlatSpec with Matchers {

  "Vec3" should "provide basic Vector arithmetic" in {
    Vec3(1, 2, 3) + Vec3(0.5, 1.5, 2.5) shouldEqual Vec3(1.5, 3.5, 5.5)
  }

}
