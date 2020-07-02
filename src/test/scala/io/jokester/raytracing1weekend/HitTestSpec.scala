package io.jokester.raytracing1weekend

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HitTestSpec extends AnyFlatSpec with Matchers {

  "Ray and Sphere" should "find intersection point" in {
    val ray = Ray(Vec3(0,0,0), Vec3(0, 1, 0))

    val sphere = Sphere(Vec3(0, 0, 5), 1)

    sphere.hitBy(ray, -1, 1e6) shouldEqual None

  }
}
