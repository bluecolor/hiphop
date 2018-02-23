package io.blue.controller

import java.util.Date
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletResponse

import org.springframework.security.access.annotation.Secured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation._
import org.springframework.dao.DataIntegrityViolationException

import io.blue.model.Connection
import io.blue.service.ConnectionService
import io.blue.exception.UniqueConstraintViolationException

@RestController
@RequestMapping(Array("/api/v1/connections"))
class ConnectionController  @Autowired()(private val connectionService: ConnectionService) {


  @RequestMapping(method = Array(RequestMethod.GET) )
  def findAll = connectionService.findAll


  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  @RequestMapping(method = Array(RequestMethod.POST))
  def create(@RequestBody connection: Connection) = {
    var con:Connection = null;
    try{
      con = connectionService.create(connection)
    }catch{
      case integrityViolation:DataIntegrityViolationException =>
        throw new UniqueConstraintViolationException("Connection name already exists!")
    }
    con
  }

  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.PUT))
  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  def update(@PathVariable("id") id: Long, @RequestBody connection: Connection) =
    connectionService.update(connection)


  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.DELETE))
  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  def delete(@PathVariable("id") id: Long) =
    connectionService.delete(id)


  @RequestMapping(value = Array("/test"), method = Array(RequestMethod.POST))
  def test(@RequestBody connection: Connection) =
    connectionService.test(connection)

  @RequestMapping(value = Array("/test/{id}"), method = Array(RequestMethod.GET))
  def test(@PathVariable("id") id: Long) =
    connectionService.test(id)



}
