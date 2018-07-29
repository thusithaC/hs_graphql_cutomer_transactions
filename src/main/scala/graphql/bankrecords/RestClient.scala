package graphql.bankrecords

/**
  * Created by thusitha on 7/26/18.
  */

import dispatch._
import Defaults._
import org.asynchttpclient.Response



import scala.concurrent.Await
import scala.language.postfixOps

class HTTPGetRequest(val urlString : String, queryParams : Map[String, String]) {

  private val request = url(urlString)
  private val requestAsGet = request.GET //not required but lets be explicit


  def queryBuilder(queryParams : List[(String, String)], request: Req) : Req = queryParams match {
    case Nil => request
    case head::tail => request.addQueryParameter(head._1, head._2)
  }

  private val builtRequest = queryBuilder(queryParams.toList, requestAsGet)

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
