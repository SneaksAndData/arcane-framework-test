package com.sneaksanddata.arcane.framework.testkit
package verifications

import com.sneaksanddata.arcane.framework.services.streaming.base.{
  JsonWatermark,
  SourceWatermark,
  TimestampOnlyWatermark
}
import upickle.ReadWriter
import zio.{Task, ZIO}

import java.sql.{DriverManager, ResultSet}

/** Test utilities for framework objects
  */
object FrameworkVerificationUtilities:

  val IntStrDecoder: ResultSet => (Int, String) = (rs: ResultSet) => (rs.getInt(1), rs.getString(2))
  val IntStrStrDecoder: ResultSet => (Int, String, String) = (rs: ResultSet) =>
    (rs.getInt(1), rs.getString(2), rs.getString(3))
  val StrStrDecoder: ResultSet => (String, String) = (rs: ResultSet) => (rs.getString(1), rs.getString(2))

  /** Read data present in the target table
    * @param targetTableName
    *   Name of the target table
    * @param columnList
    *   List of columns
    * @param decoder
    *   Field decoder to use
    * @tparam Result
    *   Row type
    * @return
    */
  def readTarget[Result](
      targetTableName: String,
      columnList: String,
      decoder: ResultSet => Result
  ): ZIO[Any, Throwable, List[Result]] = ZIO.scoped {
    for
      connection <- ZIO.attempt(DriverManager.getConnection(sys.env("ARCANE_FRAMEWORK__MERGE_SERVICE_CONNECTION_URI")))
      statement  <- ZIO.attempt(connection.createStatement())
      resultSet <- ZIO.fromAutoCloseable(
        ZIO.attemptBlocking(statement.executeQuery(s"SELECT $columnList from $targetTableName"))
      )
      data <- ZIO.attempt {
        Iterator
          .continually((resultSet.next(), resultSet))
          .takeWhile(_._1)
          .map { case (_, rs) => decoder(rs) }
          .toList
      }
    yield data
  }

  /** Read target's current watermark value
    * @param targetTableName
    * @return
    */
  def getWatermark(
      targetTableName: String
  )[Watermark <: SourceWatermark[String]](implicit rw: ReadWriter[Watermark]): ZIO[Any, Throwable, Watermark] =
    ZIO.scoped {
      for
        connection <- ZIO.attempt(
          DriverManager.getConnection(sys.env("ARCANE_FRAMEWORK__MERGE_SERVICE_CONNECTION_URI"))
        )
        statement <- ZIO.attempt(connection.createStatement())
        resultSet <- ZIO.fromAutoCloseable(
          ZIO.attemptBlocking(
            statement.executeQuery(
              s"SELECT value FROM iceberg.test.\"$targetTableName$$properties\" WHERE key = 'comment'"
            )
          )
        )
        _         <- ZIO.attemptBlocking(resultSet.next())
        watermark <- ZIO.attempt(upickle.read(resultSet.getString("value")))
      yield watermark
    }

  /** Clears target table
    * @param targetFullName
    *   Name of the target table, catalog.schema.table
    * @return
    */
  def clearTarget(targetFullName: String): Task[Unit] = ZIO.scoped {
    for
      connection <- ZIO.attempt(DriverManager.getConnection(sys.env("ARCANE_FRAMEWORK__MERGE_SERVICE_CONNECTION_URI")))
      query = s"drop table if exists $targetFullName"
      statement <- ZIO.attemptBlocking(connection.createStatement())
      _         <- ZIO.attemptBlocking(statement.executeUpdate(query))
    yield ()
  }
