package io.jokester.raytracing1weekend

import java.io.File
import java.util.concurrent.Executors

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext

object Main extends App with LazyLogging {
  private val imgH = 216
  private val imgW = 384

  private val scene = new Scene(
    SceneMetrics(
      1,
      imgW,
      imgH
    ),
    400,
    Seq(
      Sphere(Vec3(0, 0, -1), 0.5),
      Sphere(Vec3(0, -100.5, -1), 100)
    )
  )

  private val threadCount = Math.max(1, Runtime.getRuntime.availableProcessors() / 2)

  private implicit val executionContext = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(threadCount)
  )

  private def singleThreaded(): Unit = {
    logger.debug("rendering in 1 thread")
    ImageWriter.drawToFile(imgW, imgH, new File("out.png")) { scene.renderCanvas }
    logger.debug("rendering in 1 thread done")
  }

  private def multiThreaded(): Unit = {
    logger.debug(s"rendering in ${threadCount} threads")
    ImageWriter.drawToFile(imgW, imgH, new File("out.png")) {
      scene.renderCanvasThreaded
    }
    logger.debug(s"rendering in ${threadCount} threads done")
  }

  multiThreaded()

  executionContext.shutdown()
}
