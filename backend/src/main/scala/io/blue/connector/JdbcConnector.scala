package io.blue.connector

import io.blue.exception._
import io.blue.model.{Connection}
import java.sql.{Connection => JDBCConnection}
import java.sql.{DriverManager => JDBCDriverManager}

object Dbms {
  val ORACLE      = "oracle"
  val POSTGRESQL  = "postgresql"
  val SQLSERVER   = "sqlserver"
  val HSQLDB      = "hsqldb"

  def name(url: String): String = {
    if (url.toLowerCase contains "oracle") Dbms.ORACLE
    else if (url.toLowerCase contains "postgresql") Dbms.POSTGRESQL
    else if (url.toLowerCase contains "sqlserver")  Dbms.SQLSERVER
    else if (url.toLowerCase contains "hsqldb")  Dbms.HSQLDB
    else throw new UnSupportedConnectionUrlException(s"${url} rdbms not supported!")
  }

  def className(url: String) : String = {
    if (url.toLowerCase contains "oracle") "oracle.jdbc.driver.OracleDriver"
    else if (url.toLowerCase contains "postgresql") "org.postgresql.Driver"
    else if (url.toLowerCase contains "sqlserver") "com.microsoft.jdbc.sqlserver.SQLServerDriver"
    else if (url.toLowerCase contains "hsqldb") "org.hsqldb.jdbcDriver"
    else throw new UnSupportedConnectionUrlException(s"${url} rdbms not supported!")
  }

}

class JdbcConnector(private val connection: Connection) extends Connector {


  @throws(classOf[Exception])
  def test: Boolean = {
    val con =
      JDBCDriverManager.getConnection(connection.url,connection.username,connection.password)
    con.close
    true
  }

  @throws(classOf[Exception])
  def connect: java.sql.Connection = {
    JDBCDriverManager.getConnection(connection.url,connection.username,connection.password)
  }


}
