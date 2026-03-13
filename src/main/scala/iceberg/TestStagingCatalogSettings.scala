package com.sneaksanddata.arcane.framework.testkit
package iceberg

import iceberg.TestCatalogInfo.*

import com.sneaksanddata.arcane.framework.models.settings.iceberg.IcebergCatalogSettings
import com.sneaksanddata.arcane.framework.services.iceberg.IcebergCatalogCredential
import com.sneaksanddata.arcane.framework.services.iceberg.base.S3CatalogFileIO

object TestStagingCatalogSettings extends IcebergCatalogSettings:
  override val namespace: String  = defaultNamespace
  override val warehouse: String  = defaultWarehouse
  override val catalogUri: String = defaultCatalogUri
  override val additionalProperties: Map[String, String] =
    S3CatalogFileIO.properties ++ IcebergCatalogCredential.oAuth2Properties
