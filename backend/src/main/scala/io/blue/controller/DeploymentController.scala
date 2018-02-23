package io.blue.controller

import org.springframework.security.access.annotation.Secured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation._
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.annotation._

import io.blue.model._
import io.blue.exception.UniqueConstraintViolationException
import io.blue.service.DeploymentService
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(Array("/api/v1/deployments"))
class DeplymentController  @Autowired()(private val deploymentService: DeploymentService) {

  @Secured(Array("ROLE_OPERATOR","ROLE_MASTER"))
  @PostMapping(value = Array("/orders"))
  def createOrder(
    @ModelAttribute order: DeploymentOrder
  ) = {
    println(order.files.size)
  }

}
