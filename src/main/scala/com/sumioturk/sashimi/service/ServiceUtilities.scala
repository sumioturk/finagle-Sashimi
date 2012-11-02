package com.sumioturk.sashimi.service

import domain.User
import net.liftweb.json._
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import com.twitter.util.Future
import com.twitter.finagle.http.Response
import org.jboss.netty.handler.codec.http.HttpVersion._
import org.jboss.netty.buffer.ChannelBuffers._

object ServiceUtilities {
  val contentTypeEncoding = "UTF-8"
  val contentType = "application/json"
  def JsonResponse(json: JValue, statusCode: HttpResponseStatus): Future[Response] = {
    val response = Response(HTTP_1_1, statusCode)
    response.setContentType(contentType, contentTypeEncoding)
    response.setContent(copiedBuffer(pretty(render(json)).getBytes))
    Future(response)
  }

  def SetCookieJsonResponse(json: JValue, statusCode: HttpResponseStatus, sessionKey: String, user: User): Future[Response] = {
    val response = Response(HTTP_1_1, statusCode)
    response.setContentType(contentType, contentTypeEncoding)
    response.addHeader("Set-Cookie", "key=" + sessionKey + ";")
    response.addHeader("Set-Cookie", "user=" + user.toJsonString + ";")
    response.setContent(copiedBuffer(compact(render(json)).getBytes))
    Future(response)
  }

  def ExpCookieJsonResponse(json: JValue, statusCode: HttpResponseStatus): Future[Response] = {
    val response = Response(HTTP_1_1, statusCode)
    response.setContentType(contentType, contentTypeEncoding)
    response.addHeader("Set-Cookie", "key=foo; expires=Tue, 1-Jan-2000 00:00:00 GMT;")
    response.addHeader("Set-Cookie", "user=bar; expires=Tue, 1-Jan-2000 00:00:00 GMT;")
    response.setContent(copiedBuffer(compact(render(json)).getBytes))
    Future(response)
  }

}
