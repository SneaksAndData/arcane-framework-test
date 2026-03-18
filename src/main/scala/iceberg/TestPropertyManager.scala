package com.sneaksanddata.arcane.framework.testkit
package iceberg

import com.sneaksanddata.arcane.framework.services.iceberg.{
  IcebergCatalogFactory,
  IcebergSinkTablePropertyManager,
  IcebergStagingTablePropertyManager
}
import com.sneaksanddata.arcane.framework.services.iceberg.base.{CatalogFactory, SinkPropertyManager}
import org.apache.iceberg.Schema
import zio.{Scope, Task, ZIO, ZLayer}

object TestPropertyManager:
  def sinkPropertyManager: ZIO[Scope, Throwable, IcebergSinkTablePropertyManager] = for
    factory <- IcebergCatalogFactory.live(TestSinkCatalogSettings)
    result = IcebergSinkTablePropertyManager(TestSinkCatalogSettings, factory)
  yield result

  val sinkPropertyManagerLayer: ZLayer[Any, Throwable, IcebergSinkTablePropertyManager] =
    ZLayer.scoped(sinkPropertyManager)

  def stagingPropertyManager: ZIO[Scope, Throwable, IcebergStagingTablePropertyManager] = for
    factory <- IcebergCatalogFactory.live(TestStagingCatalogSettings)
    result = IcebergStagingTablePropertyManager(TestStagingCatalogSettings, factory)
  yield result

  val stagingPropertyManagerLayer: ZLayer[Any, Throwable, IcebergStagingTablePropertyManager] =
    ZLayer.scoped(stagingPropertyManager)
