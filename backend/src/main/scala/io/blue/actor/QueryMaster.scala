package io.blue.actor

import java.util.Date
import scala.collection.mutable.ListBuffer
import scala.annotation.meta.setter
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.Actor
import akka.routing.Router
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import scala.collection.JavaConversions._
import akka.actor.{Props, ActorRef}

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

import io.blue.ext.SpringExtension
import io.blue.actor.message._
import io.blue.model._
import io.blue.model.query._
import io.blue.service._

@Component(value="queryMaster")
@Scope("prototype")
class QueryMaster extends Actor {

  private val log: Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  @Autowired
  private var springExtension: SpringExtension = _

  @(Autowired @setter)
  private var queryService: QueryService = _


  case class QueryResultContainer(sender: ActorRef, result: QueryResult = new QueryResult )

  private var results: Map[Long, QueryResultContainer] = Map()

  def receive = {
    case m: Query => onQuery(m)
    case m: QueryOrderResult => onQueryOrderResult(m)
    case _ => log.debug("Opps ?")
  }

  def onQuery(query: Query) = {
    var container = QueryResultContainer(sender)
    container.result.query = query
    results += (query.id -> container)
    queryService.setRunning(query.id)
    query.connections.foreach{connection =>
      val actor = context.actorOf(springExtension.props("queryWorker"))
      actor ! queryService.createOrder(query, connection)
    }
  }

  def onQueryOrderResult(r: QueryOrderResult) = {
    log.debug("Received query order result")
    results get r.queryId match {
      case Some(container: QueryResultContainer) =>
        container.result.results ::= r
        if(container.result.isDone) {
          queryService.setSuccess(container.result.query.id)
          sendQueryResult(container)
        }
      case _ =>
    }
  }

  def sendQueryResult(container: QueryResultContainer) {
    container.result.query = queryService.findOne(container.result.query.id)
    container.sender ! container.result
  }

}
