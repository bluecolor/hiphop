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


@Entity(name="scripts")
class Script {

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
  var content: String = _

  @BeanProperty
  @Fetch(value= FetchMode.SELECT)
  @ManyToOne
  var user: User = _

  @BeanProperty
  var date: Date = new Date

  @BeanProperty
  var rank: Int = 0
}
