package com.sneaksanddata.arcane.framework.testkit
package iceberg

import com.sneaksanddata.arcane.framework.models.settings.staging.StagingSettings
import com.sneaksanddata.arcane.framework.services.iceberg.IcebergS3CatalogWriter

/** Test object for IcebergS3CatalogWriter
  */
object TestCatalogWriter:
  val defaultWriter: IcebergS3CatalogWriter =
    IcebergS3CatalogWriter(TestEntityManager.stagingEntityManager(), TestStagingSettings)
  def getWriter(stagingSettings: StagingSettings) =
    IcebergS3CatalogWriter(TestEntityManager.sinkEntityManager(), stagingSettings)
