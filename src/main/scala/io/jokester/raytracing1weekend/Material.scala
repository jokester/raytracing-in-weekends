package io.jokester.raytracing1weekend

case class ScatterRecord(attenuation: Color3, scattered: Ray)

sealed trait Material {
  def scatter(in: Ray, hitRecord: HitRecord): Option[ScatterRecord]
}

case class Lambertian(albedo: Color3) extends Material {
  override def scatter(in: Ray, hitRecord: HitRecord): Option[ScatterRecord] = {
    val scatteredDirection = hitRecord.normal + Threaded.randomUnit()
    val scattered          = Ray(hitRecord.hitAt, direction = scatteredDirection)
    Some(ScatterRecord(albedo, scattered))
  }
}

case class Metal(albedo: Color3, fuzz: Double) extends Material {
  override def scatter(in: Ray, hitRecord: HitRecord): Option[ScatterRecord] = {
    val reflected = reflect(in.direction.unit, hitRecord.normal)

    if (reflected.dot(hitRecord.normal) > 0) {
      val scattered = Ray(hitRecord.hitAt, reflected + Threaded.randomUnitSphere() * fuzz)
      Some(
        ScatterRecord(
          albedo,
          scattered
        )
      )
    } else None
  }

  private def reflect(v: Vec3, n: Vec3) = v - n * 2 * v.dot(n)
}
