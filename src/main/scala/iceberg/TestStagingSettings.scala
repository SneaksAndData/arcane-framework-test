package com.sneaksanddata.arcane.framework.testkit
package iceberg

import iceberg.TestCatalogInfo.*

import com.sneaksanddata.arcane.framework.models.settings.IcebergStagingSettings
import com.sneaksanddata.arcane.framework.services.iceberg.IcebergCatalogCredential
import com.sneaksanddata.arcane.framework.services.iceberg.base.S3CatalogFileIO

/** Test settings for staging catalog connections
  */
object TestStagingSettings:
  val defaultStagingSettings: IcebergStagingSettings = new IcebergStagingSettings:
    override val namespace: String  = defaultNamespace
    override val warehouse: String  = defaultWarehouse
    override val catalogUri: String = defaultCatalogUri
    override val additionalProperties: Map[String, String] =
      S3CatalogFileIO.properties ++ IcebergCatalogCredential.oAuth2Properties
    override val s3CatalogFileIO: S3CatalogFileIO = S3CatalogFileIO
    override val stagingLocation: Option[String]  = None
    override val maxRowsPerFile: Option[Int]      = Some(1000)
