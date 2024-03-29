package graphql.bankrecords

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import Console._
import scala.concurrent.Await
import scala.language.postfixOps

import com.typesafe.config.ConfigFactory

object Server extends App {

  val conf = ConfigFactory.load()

  val PORT = conf.getInt("web.port")

  implicit val actorSystem = ActorSystem("graphql-server")
  implicit val materializer = ActorMaterializer()

  import actorSystem.dispatcher
  import scala.concurrent.duration._

  Logger.info("Starting GRAPHQL server...")

  //shutdown Hook
  scala.sys.addShutdownHook(() -> shutdown())

  val route: Route =
    (post & path("graphql")){
      entity(as[JsValue]){ requestJson =>
        GraphQLServer.endpoint(requestJson)
      }
    } ~ {
      getFromResource("graphiql.html")
    }

  Http().bindAndHandle(route, "0.0.0.0", PORT)

  Logger.info(s"open a browser with URL: http://localhost:$PORT")
  Logger.info(s"or POST queries to http://localhost:$PORT/graphql")

  def shutdown(): Unit = {

    Logger.info("Terminating...", YELLOW)
    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, 30 seconds)
    Logger.info("Terminated... Bye", YELLOW)
  }


}

