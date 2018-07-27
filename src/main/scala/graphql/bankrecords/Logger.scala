package graphql.bankrecords

import scala.Console.{GREEN, RED, println}

/**
  * Created by thusitha on 7/27/18.
  */
object Logger {
  def info(message: String, color: String = GREEN): Unit = {
    println(color + message)
  }

  def error(message: String, color: String = RED): Unit = {
    println(color + message)
  }
}

