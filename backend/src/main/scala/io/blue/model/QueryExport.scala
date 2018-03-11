package io.blue.model

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

@Entity(name="query_exports")
class QueryExport {

  def this(id: Long) {
    this()
    this.id = id
  }

  @Id
  @GeneratedValue
  @BeanProperty
  var id: Long = _

  @BeanProperty
  @NotNull
  @Fetch(value= FetchMode.SELECT)
  @ManyToOne
  var query: Query = _

  /**
    statuses: [WAITING, RUNNING, ERROR, WARNING, UNKNOWN]
  */
  @BeanProperty
  var status: String = _

  @BeanProperty
  var startDate: Date = _

  @BeanProperty
  var endDate: Date = _

}
