package io.blue.repository

import org.springframework.data.domain.{Pageable, Page}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import io.blue.model.Connection

@Repository
trait ConnectionRepository extends JpaRepository[Connection, java.lang.Long] {
  def findByNameContainingIgnoreCase(q: String): java.util.List[Connection]
  def findByNameIgnoreCase(name: String): Connection 
}