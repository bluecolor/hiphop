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
  @Fetch(value= FetchMode.JOIN)
  @ManyToOne(optional = true, fetch = FetchType.EAGER)
  var connection: Connection = _

  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @ManyToOne(optional = true, fetch = FetchType.EAGER)
  var order: DeploymentOrder = _

  @BeanProperty
  var startDate: Date = _

  @BeanProperty
  var endDate: Date = _

  @BeanProperty
  var status: String = _

  @BeanProperty
  var message: String = _

}
