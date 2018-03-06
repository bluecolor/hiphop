package io.blue.model.query

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import scala.beans.BeanProperty
import io.blue.model._

class QueryResult {

  @BeanProperty
  var startDate: Date = _

  @BeanProperty
  var endDate: Date = _

  @BeanProperty
  var results: List[QueryOrderResult] = List()

  @BeanProperty
  var query: Query = _

  @JsonIgnore
  def isDone = query != null && results.length == query.connections.length

}
