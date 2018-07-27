package graphql.bankrecords

/**
  * Created by thusitha on 7/26/18.
  */

import dispatch._
import Defaults._
import org.asynchttpclient.Response



import scala.concurrent.Await
import scala.language.postfixOps

object RestClient {

}


class HTTPGetRequest(val urlString : String, queryParams : Map[String, String]) {

  private val request = url(urlString)
  private val requestAsGet = request.GET //not required but lets be explicit


  def queryBuilder(queryParams : List[(String, String)], request: Req) : Req = queryParams match {
    case Nil => request
    case head::tail => request.addQueryParameter(head._1, head._2)
  }

  private val builtRequest = queryBuilder(queryParams.toList, requestAsGet)

  def get(f: String=>Any) = {
    val content = Http.default(builtRequest)

    content onSuccess {

      case x if x.getStatusCode() == 200 =>
        Logger.info("Rest api call success")
        f(x.getResponseBody)
      case y =>
        Logger.error(s"Failed with status code ${y.getStatusCode()}" )
    }

    content onFailure {
      case x =>
        Logger.error("Failed but")
        Logger.error(x.getMessage)
    }
  }

  def getSync(waitMiliSec: Long): Option[String] = {
    import scala.concurrent.duration._
    Logger.info(s"sending request ${builtRequest.toString}")
    val content = Http.default(builtRequest)
    val response = content map {
      data => Some(data.getResponseBody)
    } recover {
      case timeout: java.util.concurrent.TimeoutException => None
    }
    Await.result(response, waitMiliSec millis)
  }


}
