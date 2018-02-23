package io.blue.connector

import io.blue.model.{Connection}

trait Connector {

  def test: Boolean

}