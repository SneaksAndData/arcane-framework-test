package com.sneaksanddata.arcane.framework.testkit
package zioutils

import zio.{Cause, Duration, Fiber, ZIO}

/** */
object ZKit:
  extension (runner: Fiber.Runtime[Throwable, Unit])
    def runOrFail(timeout: Duration): zio.ZIO[Any, Cause[Throwable], Unit] =
      for
        result <- runner.join.timeout(timeout).exit
        _      <- ZIO.when(result.causeOption.isDefined)(ZIO.fail(result.causeOption.get))
      yield ()
