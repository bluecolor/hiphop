package io.blue.connector

import scala.beans.BeanProperty
import io.blue.exception._
import java.sql.Connection
import java.sql.DriverManager
import io.blue.model.query._

class Connector(private val c: io.blue.model.Connection) {

  private val DATA_LIMIT = 20

  @BeanProperty
  var connection: Connection = null

  @throws(classOf[Exception])
  def test: Boolean = {
    val con = DriverManager.getConnection(c.url, c.username, c.password)
    con.close
    true
  }

  @throws(classOf[Exception])
  def connect = {
    if(!isConnected) {
      Class.forName(c.provider.className)
      connection = DriverManager.getConnection(c.url, c.username, c.password)
    }
  }

  def columns(query: String) = {
    connect
    val stmt = connection.createStatement
    val rs = stmt.executeQuery(query)
    val md = rs.getMetaData
    Column.columns(md)
  }

  def data(query: String, columns: List[Column], limit: Boolean = true) = {
    connect
    val stmt = connection.createStatement
    val rs = stmt.executeQuery(query)
    var d = List[List[String]]()
    val c = columns.zipWithIndex
    while(rs.next && (limit == false || d.length < DATA_LIMIT)) {
      d ::= c.map{case (_, index) => rs.getString(index+1)}
    }
    connection.close
    d
  }

  def isConnected = connection != null

  def close = {
    if (connection != null) {
      connection.close
      connection = null
    }
  }


}
