package io.blue.model

import scala.collection.JavaConversions._
import com.fasterxml.jackson.annotation._
import javax.persistence._
import javax.validation.constraints.{NotNull}
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Temporal
import scala.beans.BeanProperty
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.springframework.web.multipart.MultipartFile


@Entity(name="labels")
class Label {

  def this(id: Long) {
    this()
    this.id = id
  }

  @Id
  @GeneratedValue
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var name: String = _

  @BeanProperty
  var color: String = _

  @BeanProperty
  var description: String = _

  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "connection_label",
    joinColumns = Array(new JoinColumn(name = "label_id", nullable = false, updatable = false)),
    inverseJoinColumns = Array(new JoinColumn(name = "connection_id",nullable = false, updatable = false))
  )
  @JsonIgnore
  var connections: java.util.Set[Connection] = _

}
