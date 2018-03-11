package io.blue.model.query

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import javax.validation.constraints.{NotNull}
import scala.beans.BeanProperty


class QueryExportOrderResult extends QueryOrderResult{

  @BeanProperty
  var fileName: String = _

  @BeanProperty
  var recordCount: Long = _

}
