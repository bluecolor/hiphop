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

import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import akka.pattern.{Patterns,ask}

import java.sql.DriverManager
import java.sql.Connection
import java.sql.SQLException

import io.blue.AppInit
import io.blue.model._
import io.blue.model.query._
import io.blue.connector.Connector
import io.blue.actor.message._

import io.blue.repository._

@Service
@Transactional
class QueryService @Autowired()(val queryRepository: QueryRepository)   {

  @(Autowired @setter)
  private var appInit: AppInit = _

  @(Autowired @setter)
  private var connectionService: ConnectionService = _

  @(Autowired @setter)
  private var userService: UserService = _

  private val log: Logger = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  def findAll = queryRepository.findByUser(userService.findMe)

  def query(request: QueryRequest): QueryResult = {
    askToSupervisor(toQuery(request), classOf[QueryResult])
  }

  private def toQuery(request: QueryRequest): Query = {
    var q = new Query
    q.user = userService.findMe
    q.status = Status.WAITING
    q.connections = request.connections
    q.query = request.query
    queryRepository.save(q)
  }

  def askToSupervisor[T](message: AnyRef, resp: Class[T]):T = {
    val supervisor = appInit.system.actorSelection("/user/supervisor")
    implicit val timeout: Timeout = 1000*30 seconds
    val future  = supervisor ? message
    Await.result(future, timeout.duration).asInstanceOf[T]
  }


  def query(queryId: Long, query: String, connection: io.blue.model.Connection) = {
    var result = new io.blue.model.query.QueryOrderResult
    result.queryId = queryId
    result.connectionId = connection.id
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
