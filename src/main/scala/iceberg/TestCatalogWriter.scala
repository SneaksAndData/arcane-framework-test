package com.sneaksanddata.arcane.framework.testkit
package iceberg

import com.sneaksanddata.arcane.framework.models.settings.staging.StagingSettings
import com.sneaksanddata.arcane.framework.services.iceberg.IcebergS3CatalogWriter
import zio.{Scope, ZIO, ZLayer}

/** Test object for IcebergS3CatalogWriter
  */
object TestCatalogWriter:
  val defaultWriter: ZIO[Scope, Throwable, IcebergS3CatalogWriter] =
    for entityManager <- TestEntityManager.stagingEntityManager
    yield IcebergS3CatalogWriter(entityManager, TestStagingSettings)

  val defaultWriterLayer: ZLayer[Any, Throwable, IcebergS3CatalogWriter] = ZLayer.scoped(defaultWriter)

  def getWriter(stagingSettings: StagingSettings): ZIO[Scope, Throwable, IcebergS3CatalogWriter] = for entityManager <-
      TestEntityManager.stagingEntityManager
  yield IcebergS3CatalogWriter(entityManager, stagingSettings)

  def getWriterLayer(stagingSettings: StagingSettings): ZLayer[Any, Throwable, IcebergS3CatalogWriter] =
    ZLayer.scoped(getWriter(stagingSettings))
