package io.blue.actor

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
import akka.actor.{Props, ActorRef, PoisonPill}

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

import io.blue.ext.SpringExtension
import io.blue.actor.message._
import io.blue.model._
import io.blue.service._

object QueryWorker {
  def props: Props = {
    Props(new QueryWorker)
  }
}


@Component(value="queryWorker")
@Scope("prototype")
class QueryWorker extends Actor {

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  @Autowired
  private var springExtension: SpringExtension = _

  @(Autowired @setter)
  private var queryService: QueryService = _

  def receive = {
    case order: QueryOrder => onQueryOrder(order)
    case _ => log.debug("Opps ?")
  }

  def onQueryOrder(order: QueryOrder) = {
    sender ! { if(order.query.isExport) queryService.export(order) else queryService.query(order) }
    self ! PoisonPill
  }

}
