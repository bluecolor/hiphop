package io.blue.model

import java.util.Date
import org.hibernate.validator.constraints._
import scala.beans.BeanProperty
import com.fasterxml.jackson.annotation._


class UserOptions {

  @BeanProperty
  var footer: Boolean = true

  @BeanProperty
  var miniVariant: Boolean = true

  // desktop notifications
  @BeanProperty
  var deno: Boolean = true

  @BeanProperty
  var sound: Boolean = true

}
