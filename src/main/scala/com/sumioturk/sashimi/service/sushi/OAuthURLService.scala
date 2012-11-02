package com.sumioturk.sashimi.service.sushi

import config.SashimiConfig
import com.twitter.finagle.http.{Request, Response}
import domain.User
import infrastructure.UserFutureRepository
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.twitter.finagle.Service
import com.sumioturk.sashimi.service.ServiceUtilities._
import org.scribe.model.Token
import net.liftweb.json.JsonAST.{JString, JField, JObject}
import com.sumioturk.sashimi.service.CommonService

class OAuthURLService(commons: CommonService) extends Service[Request, Response] {
  val twitterAPI = commons.twitter
  val userRepo = new UserFutureRepository(commons.redis)
  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    userRepo.resolve(userId) flatMap {
      user =>
        val requestToken = new Token(
          user.requestToken,
          user.requestTokenSecret
        )
        val authURL = twitterAPI.getAuthorizationUrl(requestToken)
        val json = JObject(JField("auth_url", JString(authURL)) :: Nil)
        JsonResponse(json, OK)
    }
  }
}
