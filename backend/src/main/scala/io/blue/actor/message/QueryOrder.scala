package io.blue.actor.message

import io.blue.model._

case class QueryOrder(queryId: Long, query: String, connection: Connection)