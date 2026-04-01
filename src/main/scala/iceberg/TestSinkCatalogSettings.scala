package com.sneaksanddata.arcane.framework.testkit
package iceberg

import iceberg.TestCatalogInfo.*

import com.sneaksanddata.arcane.framework.models.settings.iceberg.IcebergCatalogSettings
import com.sneaksanddata.arcane.framework.services.iceberg.IcebergCatalogCredential
import com.sneaksanddata.arcane.framework.services.iceberg.base.S3CatalogFileIO

object TestSinkCatalogSettings extends IcebergCatalogSettings:
  override val namespace: String  = defaultNamespace
  override val warehouse: String  = defaultWarehouse
  override val catalogUri: String = defaultCatalogUri
  override val additionalProperties: Map[String, String] = sys.env.get("ARCANE_FRAMEWORK__CATALOG_NO_AUTH") match
    case Some(_) => IcebergCatalogCredential.oAuth2Properties
    case None    => S3CatalogFileIO.properties
  override val maxCatalogInstanceLifetime: zio.Duration = zio.Duration.fromSeconds(3600)
