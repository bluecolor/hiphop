package io.blue.model

import scala.collection.JavaConversions._
import java.util.Date
import javax.validation.constraints.{NotNull}
import org.springframework.data.jpa.repository.Temporal
import scala.beans.BeanProperty
import org.springframework.web.multipart.MultipartFile


class DeploymentRequest {

  @BeanProperty
  var connections: java.util.List[Connection] = _

  @BeanProperty
  var ignoreOnFail: Boolean = _

  @BeanProperty
  var files: java.util.List[MultipartFile] = _

}
