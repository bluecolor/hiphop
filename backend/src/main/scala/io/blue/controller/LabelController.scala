package io.blue.controller

import org.springframework.security.access.annotation.Secured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation._
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.annotation._

import io.blue.model._
import io.blue.exception.UniqueConstraintViolationException
import io.blue.service.LabelService
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(Array("/api/v1/labels"))
class LabelController  @Autowired()(private val labelService: LabelService) {

  @RequestMapping(method = Array(RequestMethod.GET) )
  def findAll = labelService.findAll

  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  @RequestMapping(method = Array(RequestMethod.POST))
  def create (@RequestBody label: Label) =
    labelService.create(label)

  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.PUT))
  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  def update(@PathVariable("id") id: Long, @RequestBody label: Label) =
    labelService.update(id, label)

  @RequestMapping(value = Array("/{id}"), method = Array(RequestMethod.DELETE))
  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  def delete(@PathVariable("id") id: Long) =
    labelService.delete(id)

}
