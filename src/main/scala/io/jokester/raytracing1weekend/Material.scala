package io.jokester.raytracing1weekend

case class ScatterRecord(attenuation: Color3, scattered: Ray)

sealed trait Material {
  def scatter(in: Ray, hitRecord: HitRecord): Option[ScatterRecord]
}

case class Lambertian(private val albedo: Color3) extends Material {
  override def scatter(in: Ray, hitRecord: HitRecord): Option[ScatterRecord] = {
    val scatteredDirection = hitRecord.normal + Threaded.randomUnit()
    val scattered          = Ray(hitRecord.hitAt, direction = scatteredDirection)
    Some(ScatterRecord(albedo, scattered))
  }
}

case class Metal(private val albedo: Color3) extends Material {
  override def scatter(in: Ray, hitRecord: HitRecord): Option[ScatterRecord] = {
    val reflected = reflect(in.direction.unit, hitRecord.normal)

    if (reflected.dot(hitRecord.normal) > 0) {
      Some(
        ScatterRecord(
          albedo,
          Ray(hitRecord.hitAt, reflected)
        )
      )
    } else None
  }

  private def reflect(v: Vec3, n: Vec3) = v - n * 2 * v.dot(n)
}
