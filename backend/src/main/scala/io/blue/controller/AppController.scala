package io.blue.controller

import java.io.ByteArrayInputStream

import org.springframework.web.multipart.MultipartFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation._
import org.springframework.dao.DataIntegrityViolationException
import org.apache.commons.io.IOUtils
import com.fasterxml.jackson.databind.ObjectMapper

import io.blue.service.AppService

@RestController
@RequestMapping(Array("/api/v1/app"))
class SchedulerController @Autowired()(private val appService: AppService) {

  @RequestMapping(value = Array("/version"), method = Array(RequestMethod.GET) )
  def version = appService.version

}
