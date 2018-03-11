package io.blue.actor

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

@Component(value="supervisor")
@Scope("prototype")
class Supervisor extends Actor {

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  @Autowired
  private var springExtension: SpringExtension = _

  private var mailActor:  ActorRef = _
  private var queryMaster: ActorRef = _

  override def preStart: Unit = {
    mailActor   = context.system.actorOf(springExtension.props("mail"),"mail")
    queryMaster = context.system.actorOf(springExtension.props("queryMaster"),"queryMaster")
    hearthBeat
  }

  override def postStop: Unit = {
  }

  def receive = {
    case order: Query => queryMaster.forward(order)
    case Tick => tick
    case _ => println("Opps ?")
  }

  def tick = {
    log.info("Received Tick")
  }


  def hearthBeat = {
    log.info("Start hearth beat")
    val cancellable =
      context.system.scheduler.schedule(0 milliseconds, 15000 milliseconds,self,Tick)
  }

}
