package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.Service
import net.liftweb.json._
import com.twitter.finagle.http.{Request, Response}
import domain.User
import com.sumioturk.sashimi.common.Validation
import com.sumioturk.sashimi.common.Validation.Rule
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.sumioturk.sashimi.common.Message._
import infrastructure.UserFutureRepository
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.sumioturk.sashimi.service.CommonService
import org.scribe.model.{Verb, OAuthRequest, Verifier, Token}
import com.twitter.util.Future

class RegisterOAuthTokenService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)
  val twitter = commons.twitter

  def apply(request: Request) = {
    val oAuthToken = request.getParam(User.OAuthToken)
    val oAuthVerifier = request.getParam(User.OAuthVerifier)
    val userId = request.getHeader(User.Identity)

    Validation.validateAtOnce(Seq(
      (Rule.OAuthToken, oAuthToken),
      (Rule.OAuthVerifier, oAuthVerifier)
    )) match {
      case true =>
        userRepo.resolve(userId) flatMap {
          user =>
            val requestToken = new Token(
              user.requestToken,
              user.requestTokenSecret
            )
            val verifier = new Verifier(oAuthVerifier)
            val accessToken = twitter.getAccessToken(requestToken, verifier)
            val userTwitterId = accessToken.getToken.split("-")(0)
            val twitterRequest = new OAuthRequest(Verb.GET, commons.config.twitterApiTimelineURL)
            twitterRequest.addQuerystringParameter("user_id", user.twitterId)
            twitterRequest.addQuerystringParameter("count", "1")
            twitter.signRequest(accessToken, twitterRequest)
            Future(twitterRequest.send().getBody) flatMap {
              response =>
                val json = parse(response)
                val lastTweetId = (json \ "id_str").values.toString
                val newUser = User(
                  id = user.id,
                  twitterId = userTwitterId,
                  is8th = user.is8th,
                  lastTweetId = lastTweetId,
                  name = user.name,
                  pass = user.pass,
                  isPremium = user.isPremium,
                  isActive = true,
                  sashimi = user.sashimi,
                  escapeTerm = user.escapeTerm,
                  requestToken = user.requestToken,
                  requestTokenSecret = user.requestTokenSecret,
                  accessToken = accessToken.getToken,
                  accessTokenSecret = accessToken.getSecret
                )
                userRepo.store(newUser) flatMap {
                  _ =>
                    JsonResponse(newUser.toJson, OK)
                }
            }
        }
      case false =>
        JsonResponse(toJson(InvalidParams), FORBIDDEN)
    }
  }
}
