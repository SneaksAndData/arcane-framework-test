package com.sneaksanddata.arcane.framework.testkit
package appbuilder

import metrics.VoidDimensionsProvider

import com.sneaksanddata.arcane.framework.models.app.StreamContext
import com.sneaksanddata.arcane.framework.models.schemas.ArcaneSchema
import com.sneaksanddata.arcane.framework.models.settings.*
import com.sneaksanddata.arcane.framework.services.app.base.{
  InterruptionToken,
  StreamLifetimeService,
  StreamRunnerService
}
import com.sneaksanddata.arcane.framework.services.app.{GenericStreamRunnerService, PosixStreamLifetimeService}
import com.sneaksanddata.arcane.framework.services.base.SchemaProvider
import com.sneaksanddata.arcane.framework.services.caching.schema_cache.MutableSchemaCache
import com.sneaksanddata.arcane.framework.services.filters.FieldsFilteringService
import com.sneaksanddata.arcane.framework.services.iceberg.{IcebergS3CatalogWriter, IcebergTablePropertyManager}
import com.sneaksanddata.arcane.framework.services.merging.JdbcMergeServiceClient
import com.sneaksanddata.arcane.framework.services.metrics.{ArcaneDimensionsProvider, DataDog, DeclaredMetrics}
import com.sneaksanddata.arcane.framework.services.streaming.base.{
  BackfillOverwriteBatchFactory,
  HookManager,
  StreamDataProvider
}
import com.sneaksanddata.arcane.framework.services.streaming.data_providers.backfill.{
  GenericBackfillStreamingMergeDataProvider,
  GenericBackfillStreamingOverwriteDataProvider
}
import com.sneaksanddata.arcane.framework.services.streaming.graph_builders.{
  GenericGraphBuilderFactory,
  GenericStreamingGraphBuilder
}
import com.sneaksanddata.arcane.framework.services.streaming.processors.GenericGroupingTransformer
import com.sneaksanddata.arcane.framework.services.streaming.processors.batch_processors.backfill.{
  BackfillApplyBatchProcessor,
  BackfillOverwriteWatermarkProcessor
}
import com.sneaksanddata.arcane.framework.services.streaming.processors.batch_processors.streaming.{
  DisposeBatchProcessor,
  MergeBatchProcessor,
  WatermarkProcessor
}
import com.sneaksanddata.arcane.framework.services.streaming.processors.transformers.{
  FieldFilteringTransformer,
  StagingProcessor
}
import com.sneaksanddata.arcane.framework.services.synapse.base.{SynapseLinkDataProvider, SynapseLinkReader}
import com.sneaksanddata.arcane.framework.services.synapse.{
  SynapseBackfillOverwriteBatchFactory,
  SynapseHookManager,
  SynapseLinkStreamingDataProvider
}
import zio.metrics.connectors.MetricsConfig
import zio.metrics.connectors.datadog.DatadogPublisherConfig
import zio.metrics.connectors.statsd.DatagramSocketConfig
import zio.{Tag, ZIO, ZLayer}

/** Builder for the test plugin application
  * @tparam Environment
  */
trait TestAppBuilder[
    Environment <: StreamContext & GroupingSettings & VersionedDataGraphBuilderSettings & IcebergStagingSettings &
      JdbcMergeServiceClientSettings & SinkSettings & TablePropertiesSettings & FieldSelectionRuleSettings &
      BackfillSettings & StagingDataSettings & SynapseSourceSettings & SourceBufferingSettings & MetricsConfig &
      DatagramSocketConfig & DatadogPublisherConfig
](implicit tag: Tag[Environment]):

  type StreamLifeTimeServiceLayer = ZLayer[Any, Nothing, StreamLifetimeService & InterruptionToken]
  type StreamContextLayer         = ZLayer[Any, Nothing, Environment]

  val appLayer: ZIO[StreamRunnerService, Throwable, Unit]

  /** Builds the test application from the provided layers.
    *
    * @param lifetimeService
    *   The lifetime service layer.
    * @param streamContextLayer
    *   The stream context layer.
    * @return
    *   The test application.
    */
  def buildTestApp(
      lifetimeService: StreamLifeTimeServiceLayer,
      streamContextLayer: StreamContextLayer,
      streamDataProvider: ZLayer[Any, Throwable, StreamDataProvider],
      hookManager: ZLayer[Any, Throwable, HookManager],
      backfillOverwrite: ZLayer[Any, Throwable, BackfillOverwriteBatchFactory],
      schemaProvider: ZLayer[Any, Throwable, SchemaProvider[ArcaneSchema]]
  ): ZIO[Any, Throwable, Unit] =
    appLayer.provide(
      GenericStreamRunnerService.layer,
      GenericGraphBuilderFactory.composedLayer,
      GenericGroupingTransformer.layer,
      DisposeBatchProcessor.layer,
      FieldFilteringTransformer.layer,
      MergeBatchProcessor.layer,
      StagingProcessor.layer,
      FieldsFilteringService.layer,
      PosixStreamLifetimeService.layer,
      streamContextLayer,
      streamDataProvider,
      hookManager,
      backfillOverwrite,
      schemaProvider,
      IcebergS3CatalogWriter.layer,
      JdbcMergeServiceClient.layer,
      BackfillApplyBatchProcessor.layer,
      GenericBackfillStreamingOverwriteDataProvider.layer,
      GenericBackfillStreamingMergeDataProvider.layer,
      GenericStreamingGraphBuilder.backfillSubStreamLayer,
      ZLayer.succeed(MutableSchemaCache()),
      DeclaredMetrics.layer,
      VoidDimensionsProvider.layer,
      DataDog.UdsPublisher.layer,
      WatermarkProcessor.layer,
      BackfillOverwriteWatermarkProcessor.layer,
      IcebergTablePropertyManager.layer
    )
