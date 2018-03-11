package io.blue.model.query

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import javax.validation.constraints.{NotNull}
import scala.beans.BeanProperty
import io.blue.model._

class QueryOrderResult {

  @BeanProperty
  var order: QueryOrder = _

  @BeanProperty
  var data: List[List[String]] = _

  @BeanProperty
  var columns: List[Column] = _

  def queryId = order.query.id
}
