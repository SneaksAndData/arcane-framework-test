package com.sneaksanddata.arcane.framework.testkit
package iceberg

import com.sneaksanddata.arcane.framework.services.iceberg.{
  IcebergSinkTablePropertyManager,
  IcebergStagingTablePropertyManager
}
import com.sneaksanddata.arcane.framework.services.iceberg.base.{CatalogFactory, SinkPropertyManager}
import org.apache.iceberg.Schema
import zio.Task

object TestPropertyManager:
  def sinkPropertyManager(): IcebergSinkTablePropertyManager = new IcebergSinkTablePropertyManager(
    TestSinkCatalogSettings
  )
  def stagingPropertyManager(): IcebergStagingTablePropertyManager = new IcebergStagingTablePropertyManager(
    TestStagingCatalogSettings
  )
