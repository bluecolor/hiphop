package io.blue.model.monitor

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import javax.validation.constraints.{NotNull}
import scala.beans.BeanProperty

class AvailabilityGroup {

  @BeanProperty
  var groupName: String = _

  @BeanProperty
  var replicaServerName: String = _

  @BeanProperty
  var nodeName: String = _

  @BeanProperty
  var roleDesc: String = _

  @BeanProperty
  var databaseName: String = _

  @BeanProperty
  var synchronizationStateDesc: String = _

  @BeanProperty
  var synchronizationHealthDesc: String = _
}
