package com.sneaksanddata.arcane.framework.testkit
package setups

import iceberg.{TestEntityManager, TestPropertyManager}

import com.sneaksanddata.arcane.framework.models.ddl.CreateTableRequest
import com.sneaksanddata.arcane.framework.models.schemas.ArcaneSchema
import com.sneaksanddata.arcane.framework.services.iceberg.base.{SinkEntityManager, SinkPropertyManager}
import com.sneaksanddata.arcane.framework.services.iceberg.given_Conversion_ArcaneSchema_Schema
import com.sneaksanddata.arcane.framework.services.streaming.base.JsonWatermark
import zio.{Task, ZIO}

object FrameworkTestSetup:
  def prepareWatermark(tableName: String, schema: ArcaneSchema, watermark: JsonWatermark): Task[Unit] = ZIO
    .scoped {
      for
        sink     <- ZIO.service[SinkEntityManager]
        property <- ZIO.service[SinkPropertyManager]
        _        <- sink.createTable(CreateTableRequest(tableName, schema, true))
        _        <- property.comment(tableName, watermark.toJson)
      yield ()
    }
    .provide(TestPropertyManager.sinkPropertyManagerLayer, TestEntityManager.sinkEntityManagerLayer)
