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

import io.blue.connector._
import io.blue.model._
import io.blue.model.monitor._
import io.blue.repository._


@Service
@Transactional
class MonitorService {

  private val log: Logger = LoggerFactory.getLogger(MethodHandles.lookup.lookupClass)

  @(Autowired @setter)
  private var connectionService: ConnectionService = _

  def logShipping = {
    connectionService.findAll.filter(_.vendor == Vendor.MS_SQL).map{connection=>
      val c = new Connector(connection)
      c.data("sp_help_log_shipping_monitor", false).map(new LogShipping(_))
    }.flatten
  }

}
