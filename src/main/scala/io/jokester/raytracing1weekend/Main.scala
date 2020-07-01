package io.jokester.raytracing1weekend

import java.io.File

object Main extends App {
  ImageWriter.drawToFile(640, 480, new File("out.png")) { canvas =>
    new Scene(1).drawTo(640, 480, canvas)
  }
}
