package io.blue.service

import scala.beans.BeanProperty
import scala.collection.JavaConversions._
import scala.annotation.meta.setter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import io.blue.model.{Slack=>SlackOptions, User}
import com.github.seratch.jslack._
import com.github.seratch.jslack.api.webhook._

import java.lang.InterruptedException
import java.util.concurrent.Future
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

// Ref - http://www.emoji-cheat-sheet.com/

@Service
class SlackService {

  @(Autowired @setter)
  private var settingService: SettingService = _

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  def test(options: SlackOptions): Boolean = {

      val payload = Payload.builder()
        .channel(options.channel)
        .username("octopus")
        .text(s":octopus: *Hello from Octopus*")
        .build();

      val slack = Slack.getInstance();
      val response = slack.send(options.url, payload);
      return true
  }

}
