package io.blue.model

import java.util.Date
import javax.persistence._
import javax.validation.constraints.{NotNull}
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Temporal
import scala.beans.BeanProperty
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode


@Entity(name="deployments")
class Deployment {

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
  var name: String = _

  @BeanProperty
  var status: String = Status.WAITING

  @BeanProperty
  var message: String = _

  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @OneToMany(fetch = FetchType.EAGER)
  var connections: java.util.List[Connection] = _

  @BeanProperty
  var startDate: Date = _

  @BeanProperty
  var endDate: Date = _

  @BeanProperty
  var requestDate: Date = new Date

  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "deployment")
  var orders: java.util.Set[DeploymentOrder] = _

  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "deployment", cascade=Array(CascadeType.ALL) )
  var files: java.util.Set[io.blue.model.File] = _

  @BeanProperty
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  var user: User = _

  @BeanProperty
  var ignoreOnFail: Boolean = _

}
