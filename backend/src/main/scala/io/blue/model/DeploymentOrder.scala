package io.blue.model

import scala.collection.JavaConversions._
import java.util.Date
import javax.persistence._
import javax.validation.constraints.{NotNull}
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Temporal
import scala.beans.BeanProperty
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.springframework.web.multipart.MultipartFile


@Entity(name="deployment_orders")
class DeploymentOrder {

  def this(id: Long) {
    this()
    this.id = id
  }

  @Id
  @GeneratedValue
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var status: String = _

  @BeanProperty
  @Fetch(value= FetchMode.SELECT)
  @ManyToOne
  var deployment: Deployment = _

  @BeanProperty
  @Fetch(value= FetchMode.SELECT)
  @OneToMany(mappedBy = "order")
  var steps: java.util.Set[DeploymentOrderStep] = _

  @ManyToOne
  @BeanProperty
  var connection: Connection = _

  @BeanProperty
  var message: String = _
}
