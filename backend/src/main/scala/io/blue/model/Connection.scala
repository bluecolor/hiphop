package io.blue.model

import java.util.Date
import javax.persistence._
import javax.validation.constraints.{NotNull}
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Temporal
import scala.beans.BeanProperty
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode


@Entity(name="connections")
class Connection {

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
  @Column(unique = true)
  var name: String = _

  @BeanProperty
  @NotNull
  @Type(`type`="yes_no")
  var disabled: Boolean = _

  @BeanProperty
  var url: String = _

  @BeanProperty
  var username: String = _

  @BeanProperty
  var password: String = _

  @BeanProperty
  var status: Int = _

  @BeanProperty
  var lastChecked: Date = _


  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "connection_label",
    joinColumns = Array(new JoinColumn(name = "connection_id", nullable = false, updatable = false)),
    inverseJoinColumns = Array(new JoinColumn(name = "label_id",nullable = false, updatable = false))
  )
  var labels: java.util.List[Label] = new java.util.ArrayList[Label]

}
