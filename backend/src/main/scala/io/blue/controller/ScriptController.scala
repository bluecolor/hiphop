package io.blue.controller

import java.util.Date
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletResponse

import org.springframework.security.access.annotation.Secured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation._
import org.springframework.dao.DataIntegrityViolationException

import io.blue.model.Script
import io.blue.service.ScriptService
import io.blue.exception.UniqueConstraintViolationException

@RestController
@RequestMapping(Array("/api/v1/scripts"))
class ScriptController @Autowired()(private val scriptService: ScriptService) {

  @RequestMapping(method = Array(RequestMethod.GET) )
  def findMyScripts = scriptService.findMyScripts

  @RequestMapping(method = Array(RequestMethod.POST))
  def create(@RequestBody script: String) = scriptService.create(script)

  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.DELETE))
  def delete(@PathVariable("id") id: Long) = scriptService.delete(id)

}
