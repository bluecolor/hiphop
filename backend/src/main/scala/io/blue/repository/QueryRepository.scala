package io.blue.repository

import org.springframework.data.domain.{Pageable, Page}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import io.blue.model._

@Repository
trait QueryRepository extends JpaRepository[Query, java.lang.Long] {
  def findByUser(user: User): java.util.List[Query]
}
