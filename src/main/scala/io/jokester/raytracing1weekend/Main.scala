package io.jokester.raytracing1weekend

import java.io.File

object Main extends App {
  ImageWriter.drawToFile(640, 480, new File("out.png")) { canvas =>
    new Scene(1, 640, 480)
      .addModel(
        Sphere(
          Vec3(0, 0, -1),
          0.5
        )
      )
      .addModel(Sphere(Vec3(0, -0.1, -0.6), 0.2))
      .drawTo(canvas)
  }
}
