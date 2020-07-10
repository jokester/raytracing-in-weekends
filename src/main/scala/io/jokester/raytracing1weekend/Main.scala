package io.jokester.raytracing1weekend

import java.io.File

object Main extends App {
  private val imgH = 160
  private val imgW = 240
  ImageWriter.drawToFile(imgW, imgH, new File("out.png")) { canvas =>
    new Scene(1, imgW, imgH)
      .addModel(Sphere(Vec3(0, -0.1, -1), 0.5))
      .addModel(Sphere(Vec3(0, -100.5, -1), 100))
      .drawTo(canvas)
  }
}
