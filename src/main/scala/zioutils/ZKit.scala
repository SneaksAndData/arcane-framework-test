package com.sneaksanddata.arcane.framework.testkit
package zioutils

import zio.test.{Live, TestSystem}
import zio.{Cause, Duration, Fiber, Task, ZIO}

/** */
object ZKit:
  extension (runner: Fiber.Runtime[Throwable, Unit])
    def runOrFail(timeout: Duration): zio.ZIO[Any, Cause[Throwable], Unit] =
      for
        result <- runner.join.timeout(timeout).exit
        _      <- ZIO.when(result.causeOption.isDefined)(ZIO.fail(result.causeOption.get))
      yield ()

  /** Seed system environment into ZIO's mock TestSystem. Use this to merge env variables from .env files and modify
    * those as needed at runtime. Enable via @@ TestAspect.before(liveSeed) to your test suite.
    * @return
    */
  def liveSeed: Task[Unit] = for
    // Get all current OS environment variables
    realEnvs <- Live.live(ZIO.succeed(sys.env))
    // Put them into the TestSystem mock map
    _ <- ZIO.foreachDiscard(realEnvs) { case (k, v) => TestSystem.putEnv(k, v) }
  yield ()
