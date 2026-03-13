package com.sneaksanddata.arcane.framework.testkit
package iceberg

import com.sneaksanddata.arcane.framework.services.iceberg.{IcebergSinkEntityManager, IcebergStagingEntityManager}

object TestEntityManager:
  def sinkEntityManager(): IcebergSinkEntityManager       = new IcebergSinkEntityManager(TestSinkCatalogSettings)
  def stagingEntityManager(): IcebergStagingEntityManager = new IcebergStagingEntityManager(TestStagingCatalogSettings)
