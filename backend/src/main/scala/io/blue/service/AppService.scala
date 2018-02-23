package io.blue.service

import scala.io.Source
import scala.collection.JavaConversions._
import scala.annotation.meta.setter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import io.blue.model._

@Service
@Transactional
class AppService {

  def version = {
    case class Version(date: String, major: String, minor: String, versionCode: String, patch: String)
    val lines = Source.fromFile("version.properties").getLines.toList
    Version(lines(0),lines(1),lines(2),lines(3),lines(4))
  }

}
