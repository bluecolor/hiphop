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



@Entity(name="query_orders")
class QueryOrder {

  def this(id: Long) {
    this()
    this.id = id
  }

  def this(query: Query, connection: Connection) {
    this()
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

  @BeanProperty
  @NotNull
  @Fetch(value= FetchMode.SELECT)
  @ManyToOne
  var connection: Connection = _

  /**
    statuses: [WAITING, RUNNING, ERROR, WARNING, UNKNOWN]
  */
  @BeanProperty
  var status: String = _

  @BeanProperty
  var startDate: Date = _

  @BeanProperty
  var endDate: Date = _

  @BeanProperty
  @Column(columnDefinition = "varchar(max)")
  var message: String = _

  // for export orders
  @BeanProperty
  var exportFile: String = _
}
