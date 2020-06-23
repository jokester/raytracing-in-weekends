package io.jokester.raytracing1weekend

sealed trait Model {

}

case class Plane(origin: Vec3, normal: Vec3) extends Model
case class Ball(center: Vec3, radius: Double) extends Model
