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
class DeploymentService @Autowired()(val deploymentRepository: DeploymentRepository) {

  private val log: Logger = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  @(Autowired @setter)
  private var userService: UserService = _

  def request(r: DeploymentRequest) = {
    var deployment = new Deployment
    deployment.status = Status.WAITING
    deployment.user = userService.findMe
    deployment.ignoreOnFail = r.ignoreOnFail
    deployment.files = new java.util.HashSet(r.files.map(io.blue.model.File.getFile(_)).toList.map{ f =>
      f.deployment = deployment
      f
    })
    deployment.connections = r.connections
    deployment.requestDate = new java.util.Date
    deploymentRepository.save(deployment)
  }

}
