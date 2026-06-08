package com.sneaksanddata.arcane.framework.testkit
package settings

import com.sneaksanddata.arcane.framework.models.settings.sink.{
  AnalyzeSettings,
  OptimizeSettings,
  OrphanFilesExpirationSettings,
  SnapshotExpirationSettings,
  TableMaintenanceSettings
}

object EmptyTestTableMaintenanceSettings extends TableMaintenanceSettings:
  override val targetOptimizeSettings: OptimizeSettings = new OptimizeSettings {
    override val batchThreshold: Int       = 999
    override val fileSizeThreshold: String = "128MB"
  }
  override val targetSnapshotExpirationSettings: SnapshotExpirationSettings = new SnapshotExpirationSettings {
    override val batchThreshold: Int        = 999
    override val retentionThreshold: String = "6h"
  }
  override val targetOrphanFilesExpirationSettings: OrphanFilesExpirationSettings = new OrphanFilesExpirationSettings {
    override val batchThreshold: Int        = 999
    override val retentionThreshold: String = "6h"
  }
  override val targetAnalyzeSettings: AnalyzeSettings = new AnalyzeSettings {
    override val batchThreshold: Int          = 999
    override val includedColumns: Seq[String] = Seq()
  }
