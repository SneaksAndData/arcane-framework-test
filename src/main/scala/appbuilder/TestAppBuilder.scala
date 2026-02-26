package com.sneaksanddata.arcane.framework.testkit
package appbuilder

import com.sneaksanddata.arcane.framework.services.app.base.{
  InterruptionToken,
  StreamLifetimeService,
  StreamRunnerService
}
import zio.{ZIO, ZLayer}

import scala.quoted.*

type StreamLifeTimeServiceLayer = ZLayer[Any, Nothing, StreamLifetimeService & InterruptionToken]

/** Builds the test application from the provided layers.
  *
  * This is a macro method and thus requires all DI to be resolved at compile time. Automatic app layer construction is
  * not yet available.
  *
  * `frameworkLayers` are services required by the framework stream graph builder. They might include, but are not
  * limited to::
  *
  * GenericGraphBuilderFactory.composedLayer, GenericGroupingTransformer.layer, DisposeBatchProcessor.layer,
  * FieldFilteringTransformer.layer, MergeBatchProcessor.layer, StagingProcessor.layer, FieldsFilteringService.layer,
  * PosixStreamLifetimeService.layer, IcebergS3CatalogWriter.layer, JdbcMergeServiceClient.layer,
  * BackfillApplyBatchProcessor.layer, GenericBackfillStreamingOverwriteDataProvider.layer,
  * GenericBackfillStreamingMergeDataProvider.layer, GenericStreamingGraphBuilder.backfillSubStreamLayer,
  * ZLayer.succeed(MutableSchemaCache()), DeclaredMetrics.layer, VoidDimensionsProvider.layer,
  * DataDog.UdsPublisher.layer, WatermarkProcessor.layer, BackfillOverwriteWatermarkProcessor.layer,
  * IcebergTablePropertyManager.layer
  *
  * `pluginLayers` are plugin-specific services, that are only relevant for the plugin. Usually includes plugin
  * implementations of StreamingDataProvider and related functionality
  *
  * @return
  *   The test application.
  */

object TestAppBuilder:

  inline def buildTestApp(
      app: ZIO[StreamRunnerService, Throwable, Unit],
      lifetimeService: StreamLifeTimeServiceLayer,
      pluginLayers: ZLayer[?, Nothing, ?]*
  )(frameworkLayers: ZLayer[?, Throwable, ?]*): ZIO[Any, Throwable, Unit] = ${
    buildTestAppImpl('pluginLayers, 'frameworkLayers, 'app)
  }

  private def remapZLayers(layers: Expr[Seq[ZLayer[?, Throwable, ?]]])(using
      Quotes
  ): Seq[Expr[ZLayer[?, Throwable, ?]]] =
    import quotes.reflect.*

    layers.asTerm.underlyingArgument match
      case Typed(Repeated(elements, _), tpt) => elements.map(_.asExprOf[ZLayer[?, Throwable, ?]])
      case _ =>
        report.errorAndAbort(
          s"Invalid argument literal ${layers.asTerm.underlyingArgument} provided, expected Typed(Repeated)"
        )

  private def buildTestAppImpl(
      plugin: Expr[Seq[ZLayer[?, Throwable, ?]]],
      framework: Expr[Seq[ZLayer[?, Throwable, ?]]],
      app: Expr[ZIO[StreamRunnerService, Throwable, Unit]]
  )(using Quotes): Expr[ZIO[Any, Throwable, Unit]] =

    val remappedFramework = remapZLayers(framework)

    val remapped = remapZLayers(plugin)

    '{ ${ app }.provide(${ Varargs(remapped ++ remappedFramework) }*) }
