package io.jokester.raytracing1weekend

import java.io.File
import java.util.concurrent.Executors

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext

object Main extends App with LazyLogging {
  private val imgH = 216
  private val imgW = 384

  private val materialGround = Lambertian(Color3(0.8, 0.8, 0.0))
  private val materialCenter = Lambertian(Color3(0.7, 0.3, 0.3))
  private val materialLeft   = Metal(Color3(0.8, 0.8, 0.8))
  private val materialRight  = Metal(Color3(0.8, 0.6, 0.2))

  private val scene = new Scene(
    SceneMetrics(
      1,
      imgW,
      imgH
    ),
    400,
    Seq(
      Sphere(Vec3(0, -100.5, -1), 100, materialGround),
      Sphere(Vec3(0, 0, -1), 0.5, materialCenter),
      Sphere(Vec3(-1.0, 0.0, -1.0), 0.5, materialLeft),
      Sphere(Vec3(1.0, 0.0, -1.0), 0.5, materialRight)
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
