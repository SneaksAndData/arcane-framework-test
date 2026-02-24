package com.sneaksanddata.arcane.framework.testkit
package iceberg

import com.sneaksanddata.arcane.framework.models.settings.{IcebergSinkSettings, IcebergStagingSettings}
import com.sneaksanddata.arcane.framework.services.iceberg.IcebergCatalogCredential
import com.sneaksanddata.arcane.framework.services.iceberg.base.S3CatalogFileIO

/** Test catalog connection information
  */
object TestCatalogInfo:
  val defaultNamespace  = "test"
  val defaultWarehouse  = "demo"
  val defaultCatalogUri = "http://localhost:20001/catalog"
