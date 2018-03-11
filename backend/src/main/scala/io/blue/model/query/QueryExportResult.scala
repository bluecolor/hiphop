package io.blue.model.query

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import scala.beans.BeanProperty
import io.blue.model._

class QueryExportResult {

  @BeanProperty
  var results: List[QueryExportOrderResult] = List()

  @BeanProperty
  var query: Query = _

  @JsonIgnore
  def isDone = query != null && results.length == query.connections.size

  @BeanProperty
  @JsonSerialize
  def startDate = query.startDate

  @BeanProperty
  @JsonSerialize
  def endDate = query.endDate

}
