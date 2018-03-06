package io.blue.model.query

import scala.collection.JavaConversions._
import java.util.Date
import com.fasterxml.jackson.annotation._
import javax.validation.constraints.{NotNull}
import scala.beans.BeanProperty

import java.sql.ResultSetMetaData

object Column {
  def columns(md: ResultSetMetaData) = {
    (1 to md.getColumnCount).map{ i =>
      var column = new Column
      column.columnLabel = md.getColumnLabel(i)
      column.columnName = md.getColumnName(i)
      column.columnType = md.getColumnType(i)
      column.columnTypeName = md.getColumnTypeName(i)
      column
    }.toList
  }
}


class Column {

  @BeanProperty
  var columnLabel: String = _

  @BeanProperty
  var columnName: String = _

  @BeanProperty
  var columnType: Int = _

  @BeanProperty
  var columnTypeName: String = _


}
