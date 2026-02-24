package com.sneaksanddata.arcane.framework.testkit
package settings

import iceberg.{TestCatalogInfo, TestIcebergSinkSettings}

import com.sneaksanddata.arcane.framework.models.settings.{IcebergSinkSettings, SinkSettings, TableMaintenanceSettings}

class TestDynamicSinkSettings(name: String) extends SinkSettings:
  override val targetTableFullName: String                   = name
  override val maintenanceSettings: TableMaintenanceSettings = EmptyTestTableMaintenanceSettings
  override val icebergSinkSettings: IcebergSinkSettings      = TestIcebergSinkSettings.defaultSinkSettings
