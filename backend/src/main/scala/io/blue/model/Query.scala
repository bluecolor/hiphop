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

@Entity(name="queries")
class Query {

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
  var query: String = _

  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "query_connection",
    joinColumns = Array(new JoinColumn(name = "query_id", nullable = false, updatable = false)),
    inverseJoinColumns = Array(new JoinColumn(name = "connection_id",nullable = false, updatable = false))
  )
  var connections: java.util.Set[Connection] = _

  @BeanProperty
  @Fetch(value= FetchMode.SELECT)
  @ManyToOne
  var user: User = _

  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "query")
  var orders: java.util.Set[QueryOrder] = _

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
  var export: Boolean = false

  @Transient
  def isExport = export

}
