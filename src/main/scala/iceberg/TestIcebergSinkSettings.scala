package com.sneaksanddata.arcane.framework.testkit
package iceberg

import iceberg.TestCatalogInfo.*

import com.sneaksanddata.arcane.framework.models.settings.IcebergSinkSettings
import com.sneaksanddata.arcane.framework.services.iceberg.IcebergCatalogCredential

/** Test settings for Iceberg Sink
  */
object TestIcebergSinkSettings:
  val defaultSinkSettings: IcebergSinkSettings = new IcebergSinkSettings:
    override val namespace: String                         = defaultNamespace
    override val warehouse: String                         = defaultWarehouse
    override val catalogUri: String                        = defaultCatalogUri
    override val additionalProperties: Map[String, String] = IcebergCatalogCredential.oAuth2Properties
