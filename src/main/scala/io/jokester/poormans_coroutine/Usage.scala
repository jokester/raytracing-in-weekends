package io.jokester.poormans_coroutine

import scala.concurrent.Future
import scala.util.Success
import AwaitSyntax._

class Usage {

  def useContext()(implicit ec: ExecutionContext): Unit = {
    val aFuture = Future.fromTry(Success(1))

    val a= aFuture.awaited

    val b = ec.wait(aFuture)
  }

}
