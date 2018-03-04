package io.blue.actor

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


@Component(value="queryMaster")
@Scope("prototype")
class QueryMaster extends Actor {

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  @Autowired
  private var springExtension: SpringExtension = _

  case class QueryOrderResultContainer(query: Query, results: ListBuffer[QueryOrderResult] = ListBuffer() )

  private var results: Map[Long, QueryOrderResultContainer] = Map()

  def receive = {
    case query: Query => onQuery(query)
    case queryOrderResult: QueryOrderResult => onQueryOrderResult(queryOrderResult)
    case _ => println("Opps ?")
  }

  def onQuery(query: Query) = {
    var container = QueryOrderResultContainer(query)
    results += (query.id -> container)
    query.connections.foreach{connection =>
      val actor = context.actorOf(QueryWorker.props, name=s"query-worker.${connection.id}")
      actor ! QueryOrder(query.id, query.query, connection)
    }
  }

  def onQueryOrderResult(r: QueryOrderResult) = {
    val queryId = r.order.queryId
    results get queryId match {
      case Some(container: QueryOrderResultContainer) =>
        container.results += r
      case _ =>
    }
  }

}
