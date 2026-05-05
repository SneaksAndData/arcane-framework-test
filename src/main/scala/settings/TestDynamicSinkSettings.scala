package com.sneaksanddata.arcane.framework.testkit
package settings

import iceberg.TestSinkCatalogSettings

import com.sneaksanddata.arcane.framework.models.settings.database.JdbcConnectionUrl
import com.sneaksanddata.arcane.framework.models.settings.iceberg.IcebergCatalogSettings
import com.sneaksanddata.arcane.framework.models.settings.sink.{SinkSettings, TableMaintenanceSettings}
import com.sneaksanddata.arcane.framework.models.settings.staging.{
  BasicCredential,
  BasicCredentialImpl,
  JdbcCredentialType,
  JdbcMergeServiceClientSettings,
  JdbcQueryRetryMode,
  Never,
  NeverImpl
}
import com.sneaksanddata.arcane.framework.models.settings.{EmptyTablePropertiesSettings, TablePropertiesSettings}

class TestDynamicSinkSettings(name: String) extends SinkSettings:
  override val targetTableFullName: String                    = name
  override val maintenanceSettings: TableMaintenanceSettings  = EmptyTestTableMaintenanceSettings
  override val icebergCatalog: IcebergCatalogSettings         = TestSinkCatalogSettings
  override val targetTableProperties: TablePropertiesSettings = EmptyTablePropertiesSettings
  override val mergeServiceClient: JdbcMergeServiceClientSettings = new JdbcMergeServiceClientSettings {
    override val connectionUrl: JdbcConnectionUrl = "jdbc:trino://localhost:8080"
    override val credentialType: JdbcCredentialType = BasicCredentialImpl(
      BasicCredential(
        userSetting = Some("test"),
        passwordSetting = None
      )
    )
    override val extraConnectionParameters: Map[String, String] = Map.empty
    override val queryRetryMode: JdbcQueryRetryMode             = NeverImpl(Never())
    override val queryRetryBaseDuration: zio.Duration           = zio.Duration.fromSeconds(1)
    override val queryRetryScaleFactor: Double                  = 0.1
    override val queryRetryMaxAttempts: Int                     = 3
    override val queryRetryOnMessageContents: List[String]      = List.empty
  }
