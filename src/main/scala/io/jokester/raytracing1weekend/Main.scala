package io.jokester.raytracing1weekend

import java.io.File

object Main extends App {
  ImageWriter.drawToFile(96, 96, new File("out.png")) { image =>
    new Scene().drawOn(image)
  }
}
