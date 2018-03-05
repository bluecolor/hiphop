package io.blue.service

import java.util.Date
import scala.io.Source
import scala.collection.JavaConversions._
import scala.annotation.meta.setter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

import java.sql.DriverManager
import java.sql.Connection
import java.sql.SQLException

import io.blue.model._
import io.blue.connector.Connector


@Service
@Transactional
class QueryService  {

  @(Autowired @setter)
  private var connectionService: ConnectionService = _

  private val log: Logger = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  def query(request: QueryRequest) = {
  }

  def query(query: String, connection: io.blue.model.Connection) = {
    var result = new io.blue.model.query.Result
    result.startDate = new Date
    val connector = new Connector(connectionService.findOne(connection.id))
    result.columns = connector.columns(query)
    result.endDate = new Date
    result.data = connector.data(query, result.columns)
    result.status = Status.SUCCESS
    connector.close
    result
  }


}
