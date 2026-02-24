package com.sneaksanddata.arcane.framework.testkit
package settings

import com.sneaksanddata.arcane.framework.models.settings.{
  AnalyzeSettings,
  OptimizeSettings,
  OrphanFilesExpirationSettings,
  SnapshotExpirationSettings,
  TableMaintenanceSettings
}

object EmptyTestTableMaintenanceSettings extends TableMaintenanceSettings:
  override val targetOptimizeSettings: Option[OptimizeSettings]                           = None
  override val targetSnapshotExpirationSettings: Option[SnapshotExpirationSettings]       = None
  override val targetOrphanFilesExpirationSettings: Option[OrphanFilesExpirationSettings] = None
  override val targetAnalyzeSettings: Option[AnalyzeSettings]                             = None
