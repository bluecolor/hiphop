package io.blue.service

import org.apache.commons.lang3.exception.ExceptionUtils
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
import org.springframework.core.io.FileSystemResource

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
import io.blue.util._
import io.blue.connector.Connector
import io.blue.actor.message._

import io.blue.repository._

@Service
class QueryService @Autowired()(val queryRepository: QueryRepository)   {

  val DUMP_DIR = "/home/ceyhun/projects/lab/hiphop/tmp" //! change later

  @(Autowired @setter)
  private var appInit: AppInit = _

  @(Autowired @setter)
  private var queryOrderRepository: QueryOrderRepository = _

  @(Autowired @setter)
  private var connectionService: ConnectionService = _

  @(Autowired @setter)
  private var userService: UserService = _

  private val log: Logger = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  def findOne(id: Long) = queryRepository.findOne(id)

  def findAll = queryRepository.findByUser(userService.findMe)

  def query(request: QueryRequest): QueryResult = {
    val query = {
      var q = new Query
      q.user = userService.findMe
      q.status = Status.WAITING
      q.connections = request.connections
      q.query = request.query
      q.export = request.export
      queryRepository.save(q)
    }
    if (query.isExport) {
      new java.io.File(s"${DUMP_DIR}/${query.id}").mkdir()
    }
    askToSupervisor(query, classOf[QueryResult])
  }

  def createOrder(query: Query, connection: io.blue.model.Connection) =
    queryOrderRepository.save(new QueryOrder(query, connection))

  def updateOrder(order: QueryOrder) = queryOrderRepository.save(order)

  def askToSupervisor[T](message: AnyRef, resp: Class[T]):T = {
    val supervisor = appInit.system.actorSelection("/user/supervisor")
    implicit val timeout: Timeout = 1000*30 seconds
    val future  = supervisor ? message
    Await.result(future, timeout.duration).asInstanceOf[T]
  }

  def query(order: QueryOrder) = {
    var result = new QueryOrderResult
    order.startDate = new Date
    val connector = new Connector(connectionService.findOne(order.connection.id))
    try {
      result.columns = connector.columns(order.query.query)
      result.data = connector.data(order.query.query, result.columns)
      order.status = Status.SUCCESS
    } catch {
      case e: Exception =>
        connector.close
        order.status = Status.ERROR
        result.order.message = ExceptionUtils.getStackTrace(e)
    } finally {
      connector.close
      order.endDate = new Date
      result.order = updateOrder(order)
    }
    result
  }

  def download(queryId: Long) = {
    val zip = s"${DUMP_DIR}/${queryId}/${queryId}.hiphop.zip"
    new FileSystemResource(zip)
  }

  def export(order: QueryOrder) = {
    var result = new QueryOrderResult
    order.startDate = new Date
    val connection = connectionService.findOne(order.connection.id)
    val connector = new Connector(connection)
    val path = s"${DUMP_DIR}/${order.query.id}/${order.connection.id}.${connection.name}.csv"
    try {
      connector.dump(path, order.query.query)
      order.status = Status.SUCCESS
    } catch {
      case e: Exception =>
        connector.close
        order.status = Status.ERROR
        result.order.message = ExceptionUtils.getStackTrace(e)
    } finally {
      connector.close
      order.endDate = new Date
      result.order = updateOrder(order)
    }
    result
  }

  def zip(queryId: Long) = DiskUtils.zip(s"${DUMP_DIR}/${queryId}")

  def setStatus(id: Long, status: String) = {
    var query = findOne(id)
    query.status = status
    status match {
      case Status.RUNNING => query.startDate = new Date
      case Status.SUCCESS | Status.ERROR => query.endDate = new Date
    }
    queryRepository.save(query)
  }
  def setRunning(id: Long) = setStatus(id, Status.RUNNING)
  def setWaiting(id: Long) = setStatus(id, Status.WAITING)
  def setError(id: Long) = setStatus(id, Status.ERROR)
  def setSuccess(id: Long) = setStatus(id, Status.SUCCESS)
  def setWarning(id: Long) = setStatus(id, Status.WARNING)
}
