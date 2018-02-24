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


@Entity(name="deployment_order_steps")
class DeploymentOrderStep {

  def this(id: Long) {
    this()
    this.id = id
  }

  @Id
  @GeneratedValue
  @BeanProperty
  var id: Long = _

  @BeanProperty
  @Fetch(value= FetchMode.SELECT)
  @ManyToOne
  var order: DeploymentOrder = _

  @BeanProperty
  var status: String = _

  @BeanProperty
  @Fetch(value= FetchMode.SELECT)
  @ManyToOne()
  var file: io.blue.model.File = _

  @BeanProperty
  var message: String = _

}
