package io.blue.repository

import org.springframework.data.domain.{Pageable, Page}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import io.blue.model.Script

@Repository
trait ScriptRepository extends JpaRepository[Script, java.lang.Long] {
  def findByUserId(id: Long): java.util.List[Script]
}
