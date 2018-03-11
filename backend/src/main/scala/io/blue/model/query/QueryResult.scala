package io.blue.model.query

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import scala.beans.BeanProperty
import io.blue.model._

class QueryResult {

  @BeanProperty
  var results: List[QueryOrderResult] = List()

  @BeanProperty
  var query: Query = _

  @JsonIgnore
  def isDone = query != null && results.length == query.connections.size

  @BeanProperty
  @JsonSerialize
  def startDate = if(query != null) query.startDate else new Date

  @BeanProperty
  @JsonSerialize
  def endDate = if (query != null) query.endDate else new Date

}
