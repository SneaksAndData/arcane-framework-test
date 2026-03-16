package com.sneaksanddata.arcane.framework.testkit
package settings

import com.sneaksanddata.arcane.framework.models.settings.{TableFormat, TablePropertiesSettings}

object TestTablePropertiesSettings extends TablePropertiesSettings:
  override val format: TableFormat                      = TableFormat.PARQUET
  override val sortedBy: Array[String]                  = Array()
  override val parquetBloomFilterColumns: Array[String] = Array()

object CustomTablePropertiesSettings:
  def apply: TablePropertiesSettings = new TablePropertiesSettings {
    override val parquetBloomFilterColumns: Array[String] = Array.empty
    override val format: TableFormat                      = TableFormat.PARQUET
    override val sortedBy: Array[String]                  = Array.empty
  }
