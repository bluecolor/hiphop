package io.blue.controller

import org.springframework.security.access.annotation.Secured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation._
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.annotation._
import javax.servlet.http.HttpServletResponse

import io.blue.model._
import io.blue.model.query._
import io.blue.exception.UniqueConstraintViolationException
import io.blue.service._
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(Array("/api/v1/monitor"))
class MonitorController  @Autowired()(private val monitorService: MonitorService) {

  @RequestMapping(value = Array("/log-shipping"), method = Array(RequestMethod.GET))
  def logShipping = monitorService.logShipping
}
