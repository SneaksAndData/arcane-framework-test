package com.sneaksanddata.arcane.framework.testkit
package iceberg

import com.sneaksanddata.arcane.framework.services.iceberg.{
  IcebergCatalogFactory,
  IcebergSinkEntityManager,
  IcebergStagingEntityManager
}
import zio.{Scope, ZIO, ZLayer}

object TestEntityManager:
  def sinkEntityManager: ZIO[Scope, Throwable, IcebergSinkEntityManager] = for
    factory <- IcebergCatalogFactory.live(TestSinkCatalogSettings)
    entityManager = IcebergSinkEntityManager(TestSinkCatalogSettings, factory)
  yield entityManager

  val sinkEntityManagerLayer: ZLayer[Any, Throwable, IcebergSinkEntityManager] = ZLayer.scoped(sinkEntityManager)

  def stagingEntityManager: ZIO[Scope, Throwable, IcebergStagingEntityManager] = for
    factory <- IcebergCatalogFactory.live(TestStagingCatalogSettings)
    entityManager = IcebergStagingEntityManager(TestStagingCatalogSettings, factory)
  yield entityManager

  val stagingEntityManagerLayer: ZLayer[Any, Throwable, IcebergStagingEntityManager] =
    ZLayer.scoped(stagingEntityManager)
