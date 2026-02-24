package com.sneaksanddata.arcane.framework.testkit
package metrics

import com.sneaksanddata.arcane.framework.services.base.DimensionsProvider
import zio.{ZIO, ZLayer}

import scala.collection.immutable.SortedMap

object VoidDimensionsProvider extends DimensionsProvider:
  override def getDimensions: SortedMap[String, String] = SortedMap()

  val layer =
    ZLayer {
      ZIO.succeed(VoidDimensionsProvider)
    }
