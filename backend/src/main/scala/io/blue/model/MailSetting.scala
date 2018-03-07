package io.blue.model

import scala.beans.BeanProperty

class MailSetting {

  @BeanProperty
  var active: Boolean = _

  @BeanProperty
  var host: String = _

  @BeanProperty
  var port: Int = _

  @BeanProperty
  var username: String = _

  @BeanProperty
  var password: String = _

  @BeanProperty
  var sendFrom: String = _

  @BeanProperty
  var connectionSecurity: String = "ssl"

  def isActive = active

}
