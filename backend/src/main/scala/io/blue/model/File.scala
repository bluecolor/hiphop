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

object File {
  def getFile(f: MultipartFile) = {
    var file = new File
    file.name = f.getOriginalFilename
    file.data = f.getBytes
    file
  }
}

@Entity(name="files")
class File {

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
  @JsonIgnore
  @Column(columnDefinition = "binary(max)")
  var data: Array[Byte]  = _

  @BeanProperty
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JsonIgnore
  var deployment: Deployment = _
}
