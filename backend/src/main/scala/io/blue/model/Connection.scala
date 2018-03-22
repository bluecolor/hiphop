package io.blue.model

import java.util.Date
import javax.persistence._
import javax.validation.constraints.{NotNull}
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Temporal
import scala.beans.BeanProperty
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import com.fasterxml.jackson.databind.annotation.JsonSerialize

object Vendor {
  val MARIADB = "MARIADB"
  val MYSQL = "MYSQL"
  val POSTGRE_SQL = "POSTGRE_SQL"
  val MS_SQL = "MS_SQL"
  val ORACLE = "ORACLE"
  val UNKNOWN= "UNKNOWN"
}

case class ConnectionProvider(vendor: String, className: String = null)

@Entity(name="connections")
class Connection {

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
  @Column(unique = true)
  var name: String = _

  @BeanProperty
  @NotNull
  @Type(`type`="yes_no")
  var disabled: Boolean = _

  @BeanProperty
  var url: String = _

  @BeanProperty
  var username: String = _

  @BeanProperty
  var password: String = _

  @BeanProperty
  var status: Int = _

  @BeanProperty
  var lastChecked: Date = _

  @BeanProperty
  @Fetch(value= FetchMode.JOIN)
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "connection_label",
    joinColumns = Array(new JoinColumn(name = "connection_id", nullable = false, updatable = false)),
    inverseJoinColumns = Array(new JoinColumn(name = "label_id",nullable = false, updatable = false))
  )
  var labels: java.util.List[Label] = new java.util.ArrayList[Label]

  @Transient
  @BeanProperty
  def provider: ConnectionProvider = {
    if(url == null || url.trim.isEmpty){
      ConnectionProvider(Vendor.UNKNOWN)
    } else if(url contains "jdbc:postgresql") {
      ConnectionProvider(Vendor.POSTGRE_SQL, "org.postgresql.Driver")
    } else if(url contains "jdbc:mysql") {
      ConnectionProvider(Vendor.MYSQL, "com.mysql.jdbc.Driver")
    } else if(url contains "jdbc:sqlserver") {
      ConnectionProvider(Vendor.MS_SQL, "com.microsoft.sqlserver.jdbc.SQLServerDriver")
    } else if(url contains "jdbc:oracle") {
      ConnectionProvider(Vendor.ORACLE, "oracle.jdbc.driver.OracleDriver")
    } else if(url contains "jdbc:mariadb") {
      ConnectionProvider(Vendor.MARIADB, "org.mariadb.jdbc.Driver")
    } else {
      ConnectionProvider(Vendor.UNKNOWN)
    }
  }

  @Transient
  @BeanProperty
  @JsonSerialize
  def vendor: String = provider.vendor

}
