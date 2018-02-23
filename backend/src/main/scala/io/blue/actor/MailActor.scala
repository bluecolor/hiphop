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
import akka.actor.{Props, ActorRef}

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

import io.blue.ext.SpringExtension
import io.blue.actor.message._
import io.blue.model._
import io.blue.service.MailService


@Component(value="mail")
@Scope("prototype")
class MailActor extends Actor {

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  @Autowired
  private var springExtension: SpringExtension = _

  @(Autowired @setter)
  private var mailService: MailService = _

  def receive = {
    case SendUserPasswordMail(user: User, password: String) => sendUserPasswordMail(user,password)
    case SendNewPasswordMail(user: User, password: String) => sendNewPasswordMail(user,password)
    case _ => println("Opps ?")
  }

  def sendUserPasswordMail(user: User, password: String) =
    mailService.sendUserPasswordMail(user, password)

  def sendNewPasswordMail(user: User, password: String) =
    mailService.sendNewPasswordMail(user, password)


}
