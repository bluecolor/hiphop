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
class DeploymentService @Autowired()(val deploymentOrderRepository: DeploymentOrderRepository) {

  private val log:Logger  = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  def createOrder (order: DeploymentOrder) = {
    // deploymentOrderRepository.save(order)
    println(order.files.length)
  }

}
