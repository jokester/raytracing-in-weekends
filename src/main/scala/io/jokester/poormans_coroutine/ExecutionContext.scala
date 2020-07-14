package io.jokester.poormans_coroutine

import scala.concurrent.Future

trait ExecutionContext {
  def wait[T](f: Future[T])(): T
  def cps[T](f: Future[T])(continuation: T => Unit): Unit
}

class ThreadLocalExecutionContext {

}

object AwaitSyntax {
  implicit class RichFuture[T](val v: Future[T])(implicit executionContext: ExecutionContext) {
    def awaited: T = executionContext.wait(v)
  }

}
