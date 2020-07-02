package io.jokester.raytracing1weekend

case class HitRecord(hitAt: Vec3, normal: Vec3, t: Double)

sealed trait Hittable {
  def hitBy(ray: Ray, tMin: Double, tMax: Double): Option[HitRecord]

  def colorAt(hitRecord: HitRecord): Color3
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
            // always point to crush
            // so that we can infer reflection from ray && normal
            hitAt - center,
            t
          )
        })
    }
  }

  override def colorAt(hit: HitRecord): Color3 = {
    val n = hit.normal.unit
    Color3(
      (n.x + 1) / 2,
      (n.y + 1) / 2,
      (n.z + 1) / 2
    )
  }

}
