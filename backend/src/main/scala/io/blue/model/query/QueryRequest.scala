package io.blue.model.query

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import javax.persistence._
import javax.validation.constraints.{NotNull}
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Temporal
import scala.beans.BeanProperty
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.springframework.web.multipart.MultipartFile

class QueryRequest {

  @BeanProperty
  var connections: java.util.Set[Connection] = _

  @BeanProperty
  var query: String = _

  @BeanProperty
  var export: Boolean = false
}
