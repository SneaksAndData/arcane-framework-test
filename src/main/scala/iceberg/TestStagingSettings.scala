package com.sneaksanddata.arcane.framework.testkit
package iceberg

import iceberg.TestCatalogInfo.*

import com.sneaksanddata.arcane.framework.models.settings.iceberg.IcebergCatalogSettings
import com.sneaksanddata.arcane.framework.models.settings.staging.{StagingSettings, StagingTableSettings}

/** Test settings for staging catalog connections
  */
object TestStagingSettings extends StagingSettings:
  override val table: StagingTableSettings = new StagingTableSettings {
    override val stagingCatalogName: String  = "demo"
    override val stagingSchemaName: String   = "test"
    override val isUnifiedSchema: Boolean    = false
    override val maxRowsPerFile: Option[Int] = Some(10000)
  }
  override val icebergCatalog: IcebergCatalogSettings = TestStagingCatalogSettings
