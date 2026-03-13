package com.sneaksanddata.arcane.framework.testkit
package settings

import iceberg.{TestCatalogInfo, TestSinkCatalogSettings}

import com.sneaksanddata.arcane.framework.models.settings.database.JdbcConnectionUrl
import com.sneaksanddata.arcane.framework.models.settings.{EmptyTablePropertiesSettings, TablePropertiesSettings}
import com.sneaksanddata.arcane.framework.models.settings.iceberg.IcebergCatalogSettings
import com.sneaksanddata.arcane.framework.models.settings.sink.{SinkSettings, TableMaintenanceSettings}
import com.sneaksanddata.arcane.framework.models.settings.staging.JdbcQueryRetryMode.Never
import com.sneaksanddata.arcane.framework.models.settings.staging.{JdbcMergeServiceClientSettings, JdbcQueryRetryMode}

class TestDynamicSinkSettings(name: String) extends SinkSettings:
  override val targetTableFullName: String                    = name
  override val maintenanceSettings: TableMaintenanceSettings  = EmptyTestTableMaintenanceSettings
  override val icebergCatalog: IcebergCatalogSettings         = TestSinkCatalogSettings
  override val targetTableProperties: TablePropertiesSettings = EmptyTablePropertiesSettings
  override val mergeServiceClient: JdbcMergeServiceClientSettings = new JdbcMergeServiceClientSettings {
    override val connectionUrl: JdbcConnectionUrl               = "jdbc:trino://localhost:8080/iceberg/test?user=test"
    override val extraConnectionParameters: Map[String, String] = Map.empty
    override val queryRetryMode: JdbcQueryRetryMode             = Never
    override val queryRetryBaseDuration: zio.Duration           = zio.Duration.fromSeconds(1)
    override val queryRetryScaleFactor: Double                  = 0.1
    override val queryRetryMaxAttempts: Int                     = 3
    override val queryRetryOnMessageContents: List[String]      = List.empty
  }
