package io.blue.model.query

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import javax.validation.constraints.{NotNull}
import scala.beans.BeanProperty


class QueryOrderResult {

  @BeanProperty
  var connectionId: Long = _

  @BeanProperty
  var queryId: Long = _

  @BeanProperty
  var status: String = _

  @BeanProperty
  var startDate: Date = _

  @BeanProperty
  var endDate: Date = _

  @BeanProperty
  var columns: List[Column] = _

  @BeanProperty
  var data: List[List[String]] = _

  @BeanProperty
  var message: String = _
}
