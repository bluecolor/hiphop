package io.blue.model.monitor

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import javax.validation.constraints.{NotNull}
import scala.beans.BeanProperty
import io.blue.model._

class LogShipping {

  def this(m: Map[String, String]) {
    this()
    status = m("status").toInt
    isPrimary = m("is_primary") == "1"
    server = m("server")
    databaseName = m("database_name")
    timeSinceLastBackup = m("time_since_last_backup").toInt
    lastBackupFile = m("last_backup_file")
    backupThreshold = m("backup_thres_hold")
    isBackupAlertEnabled = m("is_backup_alert_enabled") == "1"
    timeSinceLastCopy = m("time_since_last_copy").toInt
    lastCopiedFile = m("last_copied_file")
    timeSinceLastRestore = m("time_since_last_restore").toInt
    lastRestoredFile = m("last_restored_file")
    lastRestoredLatency = m("last_restored_latency").toInt
    restoreThreshold = m("restore_threshold").toInt
    isRestoreAlertEnabled = m("is_restore_alert_enabled") == "1"
  }

  @BeanProperty
  var time: Date = new java.util.Date

  @BeanProperty
  var connection: Connection = _

  @BeanProperty
  var status: Int = _

  @BeanProperty
  var isPrimary: Boolean = _

  @BeanProperty
  var server: String = _

  @BeanProperty
  var databaseName: String = _

  @BeanProperty
  var timeSinceLastBackup: Int = _

  @BeanProperty
  var lastBackupFile: String = _

  @BeanProperty
  var backupThreshold: String = _

  @BeanProperty
  var isBackupAlertEnabled: Boolean = _

  @BeanProperty
  var timeSinceLastCopy: Int = _

  @BeanProperty
  var lastCopiedFile: String = _

  @BeanProperty
  var timeSinceLastRestore: Int = _

  @BeanProperty
  var lastRestoredFile: String = _

  @BeanProperty
  var lastRestoredLatency: Int = _

  @BeanProperty
  var restoreThreshold: Int = _

  @BeanProperty
  var isRestoreAlertEnabled: Boolean = _
}
