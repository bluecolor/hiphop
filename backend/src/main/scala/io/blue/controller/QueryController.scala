package io.blue.controller

import org.springframework.security.access.annotation.Secured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation._
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.annotation._

import io.blue.model._
import io.blue.exception.UniqueConstraintViolationException
import io.blue.service._
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(Array("/api/v1/queries"))
class QueryController  @Autowired()(private val queryService: QueryService) {

  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  @RequestMapping(method = Array(RequestMethod.GET))
  def findAll= queryService.findAll

  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  @RequestMapping(method = Array(RequestMethod.POST))
  def query (@RequestBody queryRequest: QueryRequest) =
    queryService.query(queryRequest)

}
