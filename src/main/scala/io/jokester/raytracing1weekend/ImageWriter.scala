package io.jokester.raytracing1weekend
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File

import javax.imageio.ImageIO

object ImageWriter {

  def drawToFile(width: Int, height: Int, target: File)(draw: Graphics2D => Unit) = {
    val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    val g2d = bufferedImage.createGraphics
    draw(g2d)
    g2d.dispose();

    ImageIO.write(bufferedImage, "png", target)
  }
}
