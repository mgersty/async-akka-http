package org.gersty

import java.nio.charset.StandardCharsets

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{PredefinedFromEntityUnmarshallers, Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.{Failure, Success}



object HttpClientSingleRequest extends App {
  override def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem()

    implicit val materializer = ActorMaterializer()

    import system.dispatcher

    for(i <- 0 to 20){
      val uri = s"http://localhost:8080/buckets?id=${i}"
      val httpRequest = HttpRequest(
        uri = uri,
        method = HttpMethods.GET
      )
      val httpFutureResponse = Http().singleRequest(httpRequest)
      httpFutureResponse
        .onComplete {
          case Success(httpFutureResponse) =>  httpFutureResponse.entity.toStrict(1.millis).map(_.data.utf8String).foreach(println)
          case Failure(e) => println(s"Failed to HTTP GET $uri, error = ${e.getMessage}")
        }
    }
  }

}