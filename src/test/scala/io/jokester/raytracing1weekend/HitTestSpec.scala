package io.jokester.raytracing1weekend

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HitTestSpec extends AnyFlatSpec with Matchers {

  "Ray and Sphere" should "find intersection point" in {
    val ray = Ray(Vec3(0, 0, 0), Vec3(0, 1, 0))
    val material = Metal(Color3.black, 0)

    // not hit
    Sphere(Vec3(0, 0, 5), 1, material).hitBy(ray, -1, 1e6) shouldEqual None

    // 2 intersections
    val sphere = Sphere(Vec3(0, 1, 0), 1, material)
    sphere.hitBy(ray, -1, 1e6) shouldEqual Some(
      HitRecord(Vec3(0, 0, 0), Vec3(0, -1, 0), 0, material))

    sphere.hitBy(ray, 0.01, 1e6) shouldEqual Some(
      HitRecord(Vec3(0, 2, 0), Vec3(0, 1, 0), 2, material))

    sphere.hitBy(ray, 0.01, 1.99) shouldEqual None
  }
}
