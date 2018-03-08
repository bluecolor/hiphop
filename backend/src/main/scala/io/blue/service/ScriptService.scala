package io.blue.service

import scala.io.Source
import scala.collection.JavaConversions._
import scala.annotation.meta.setter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

import io.blue.model._
import io.blue.repository._


@Service
@Transactional
class ScriptService @Autowired()(val scriptRepository: ScriptRepository) {

  private val log: Logger = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  @(Autowired @setter)
  private var userService: UserService = _

  def findMyScripts = scriptRepository.findByUserId(userService.findMe.id)

  def create(script: String) = {
    var s = new Script()
    s.content = script
    s.user = userService.findMe
    scriptRepository.save(s)
  }

}
