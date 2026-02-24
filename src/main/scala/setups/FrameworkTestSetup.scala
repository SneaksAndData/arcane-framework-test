package com.sneaksanddata.arcane.framework.testkit
package setups

import iceberg.TestCatalogWriter.defaultWriter
import settings.TestDynamicSinkSettings

import com.sneaksanddata.arcane.framework.models.schemas.ArcaneSchema
import com.sneaksanddata.arcane.framework.services.iceberg.{
  IcebergTablePropertyManager,
  given_Conversion_ArcaneSchema_Schema
}
import com.sneaksanddata.arcane.framework.services.streaming.base.JsonWatermark
import zio.Task

object FrameworkTestSetup:
  def prepareWatermark(tableName: String, schema: ArcaneSchema, watermark: JsonWatermark): Task[Unit] =
    val propertyManager = IcebergTablePropertyManager(TestDynamicSinkSettings(tableName))
    for
      _ <- defaultWriter.createTable(tableName, schema, true)
      _ <- propertyManager.comment(tableName, watermark.toJson)
    yield ()
