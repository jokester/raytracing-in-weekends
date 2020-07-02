package io.jokester.raytracing1weekend

case class HitRecord(hitAt: Vec3, normal: Vec3, t: Double)

sealed trait Hittable {
  def hitBy(ray: Ray, tMin: Double, tMax: Double): Option[HitRecord]
}

case class Sphere(center: Vec3, radius: Double) extends Hittable {
  override def hitBy(ray: Ray, tMin: Double, tMax: Double): Option[HitRecord] = {
    val oc           = ray.origin - center
    val a            = ray.direction.squareSum
    val b            = 2 * ray.direction.dot(oc)
    val c            = oc.squareSum - radius * radius
    val discriminant = b * b - 4 * a * c
    if (discriminant < 0) {
      None
    } else {

      val r1 = (-b - Math.sqrt(discriminant)) / (2 * a);
      val r2 = (-b + Math.sqrt(discriminant)) / (2 * a); // r1 <= r2

      Seq(r1, r2)
        .find(t => tMin <= t && t <= tMax)
        .map(t => {
          val hitAt = ray.at(t)
          HitRecord(
            hitAt,
            hitAt - center,
            t
          )
        })
    }
  }

}
