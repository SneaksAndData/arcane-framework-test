package com.sneaksanddata.arcane.framework.testkit
package iceberg

import iceberg.TestCatalogInfo.*

import com.sneaksanddata.arcane.framework.models.settings.iceberg.IcebergCatalogSettings
import com.sneaksanddata.arcane.framework.services.iceberg.IcebergCatalogCredential

object TestSinkCatalogSettings extends IcebergCatalogSettings:
  override val namespace: String                         = defaultNamespace
  override val warehouse: String                         = defaultWarehouse
  override val catalogUri: String                        = defaultCatalogUri
  override val additionalProperties: Map[String, String] = IcebergCatalogCredential.oAuth2Properties
  override val maxCatalogInstanceLifetime: zio.Duration  = zio.Duration.fromSeconds(5)
