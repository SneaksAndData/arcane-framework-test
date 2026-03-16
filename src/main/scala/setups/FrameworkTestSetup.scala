package com.sneaksanddata.arcane.framework.testkit
package setups

import iceberg.{TestEntityManager, TestPropertyManager}

import com.sneaksanddata.arcane.framework.models.ddl.CreateTableRequest
import com.sneaksanddata.arcane.framework.models.schemas.ArcaneSchema
import com.sneaksanddata.arcane.framework.services.iceberg.given_Conversion_ArcaneSchema_Schema
import com.sneaksanddata.arcane.framework.services.streaming.base.JsonWatermark
import zio.Task

object FrameworkTestSetup:
  def prepareWatermark(tableName: String, schema: ArcaneSchema, watermark: JsonWatermark): Task[Unit] =
    val propertyManager = TestPropertyManager.sinkPropertyManager()
    val entityManager   = TestEntityManager.sinkEntityManager()
    for
      _ <- entityManager.createTable(CreateTableRequest(tableName, schema, true))
      _ <- propertyManager.comment(tableName, watermark.toJson)
    yield ()
