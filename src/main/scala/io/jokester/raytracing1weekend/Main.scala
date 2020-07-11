package io.jokester.raytracing1weekend

import java.io.File

import scala.concurrent.ExecutionContext

object Main extends App {
  private implicit val executor = ExecutionContext.global
  private val imgH              = 480
  private val imgW              = 640
  ImageWriter.drawToFile(imgW, imgH, new File("out.png")) { canvas =>
    new Scene(
      SceneMetrics(
        1,
        imgW,
        imgH
      ),
      3,
      Seq(
        Sphere(Vec3(0, -0.1, -1), 0.5),
        Sphere(Vec3(0, -100.5, -1), 100)
      )
    ).drawTo(canvas)
  }
}
