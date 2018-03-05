package io.blue.service

import java.util.Date
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Pageable, Page}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper

import io.blue.repository.ConnectionRepository
import io.blue.model._
import io.blue.connector.Connector

import io.blue.exception._

@Service
@Transactional
class ConnectionService @Autowired()(val connectionRepository: ConnectionRepository) {

  def findAll = connectionRepository.findAll

  def findOne(id: Long) = connectionRepository.findOne(id)

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)


  def search(q: String) = {
    connectionRepository.findByNameContainingIgnoreCase(q)
  }

  @throws(classOf[Exception])
  def test(connection: Connection): Connection = {
    var result: Boolean = true
    try {
      val connector = new Connector(connection)
      result = connector.test
    }catch {
        case e: Exception =>
          updateStatus(connection.id, -1)
          throw e
    }
    updateStatus(connection.id, if(result) 1 else -1)
    return connection
  }

  def test(id: Long): Connection = test(findOne(id))


  def update(connection: Connection) = {
    var con = findOne(connection.id)
    con.name = connection.name
    con.url = connection.url
    con.username= connection.username
    con.password= connection.password //todo encrypt
    connectionRepository.save(con)
  }

  private def updateStatus(id:Long, status: Int = 0) = {
    var connection = findOne(id)
    if(connection != null) {
      log.debug(s"Update connection($id) status : $status")
      connection.status = status
      connectionRepository.save(connection)
    }
  }

  @throws(classOf[ConnectionNameExistException])
  def create(connection: Connection): Connection = {
    if(connectionRepository.findByNameIgnoreCase(connection.name) != null){
      throw new ConnectionNameExistException(connection.name)
    }
    connectionRepository.save(connection)
  }


  def delete(id: Long): Connection = {
    val connection = connectionRepository.findOne(id);
    connectionRepository.delete(id)
    connection
  }

}
