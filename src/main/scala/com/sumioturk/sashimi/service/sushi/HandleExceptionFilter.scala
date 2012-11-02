package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response}
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.sumioturk.sashimi.common.Message._
import com.sumioturk.sashimi.service.ServiceUtilities._

class HandleExceptionFilter extends SimpleFilter[Request, Response] {
  def apply(request: Request, service: Service[Request, Response]) = {
    service(request) handle {
      case error =>
        val statusCode = error match {
          case _: IllegalArgumentException =>
            error.printStackTrace()
            FORBIDDEN
          case _ =>
            error.printStackTrace()
            INTERNAL_SERVER_ERROR
        }
      JsonResponse(toJson(InternalServerError), statusCode).get()
    }
  }
}
