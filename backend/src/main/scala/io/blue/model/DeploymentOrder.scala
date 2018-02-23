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
  @Fetch(value= FetchMode.JOIN)
  @ManyToOne(optional = true, fetch = FetchType.EAGER)
  var user: User = _

  @BeanProperty
  var orderDate: Date = _

  @BeanProperty
  @Fetch(value= FetchMode.SELECT)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade=Array(CascadeType.REMOVE))
  var deployments: java.util.List[Deployment] = _

  @Transient
  @BeanProperty
  var connections: java.util.List[Connection] = _

  @BeanProperty
  var ignoreOnFail: Boolean = _

  @BeanProperty
  var folder: String = _

  @Transient
  @BeanProperty
  var files: java.util.List[MultipartFile] = _

  @BeanProperty
  var fileNames: String = _

  @PrePersist
  @BeanProperty
  def prePersist = {
    if(! files.isEmpty){
      fileNames = files.map(_.getOriginalFilename).mkString
    }
  }

}
