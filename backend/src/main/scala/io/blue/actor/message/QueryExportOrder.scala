package io.blue.actor.message

import io.blue.model._

case class QueryExportOrder(queryId: Long, query: String, connection: Connection)
