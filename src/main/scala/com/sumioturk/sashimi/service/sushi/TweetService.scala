package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.Service
import domain.User
import com.sumioturk.sashimi.common.Validation
import com.sumioturk.sashimi.common.Validation.Rule
import infrastructure.UserFutureRepository
import com.sumioturk.sashimi.service.CommonService
import org.scribe.model.{Token, Verb, OAuthRequest}
import com.twitter.util.Future
import net.liftweb.json._
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.sumioturk.sashimi.common.Message._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._

class TweetService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)
  val twitter = commons.twitter

  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    val status = request.getParam(User.Status)
    Validation.isValid(Rule.Status, status) match {
      case true =>
        userRepo.resolve(userId) flatMap {
          user =>
            val accessToken = new Token(
              user.accessToken,
              user.accessTokenSecret
            )
            val twitterRequest = new OAuthRequest(Verb.POST, commons.config.twitterApiUpdateURL)
            twitterRequest.addBodyParameter(User.Status, status)
            twitter.signRequest(accessToken, twitterRequest)
            Future(twitterRequest.send().getBody) flatMap {
              response =>
                val json = parse(response)
                val twitterUserId = (json \ "user" \ "id").values.toString
                val tweetId = (json \ "id_str").values.toString
                Validation.validateAtOnce(Seq(
                  (Rule.Number, twitterUserId),
                  (Rule.Number, tweetId)
                )) match {
                  case true =>
                    val newUser = User(
                      id = user.id,
                      twitterId = twitterUserId,
                      name = user.name,
                      sashimi = user.sashimi,
                      lastTweetId = (tweetId.toLong - 1).toString,
                      pass = user.pass,
                      isPremium = user.isPremium,
                      isActive = user.isActive,
                      escapeTerm = user.escapeTerm,
                      requestToken = user.requestToken,
                      requestTokenSecret = user.requestTokenSecret,
                      accessToken = user.accessToken,
                      accessTokenSecret = user.accessTokenSecret
                    )
                    userRepo.store(newUser) flatMap {
                      _ =>
                        JsonResponse(toJson(Tweeted), OK)
                    }
                  case false =>
                    JsonResponse(toJson(InvalidParams), FORBIDDEN)
                }
            }
        }
      case false =>
        JsonResponse(toJson(InvalidParams), FORBIDDEN)
    }
  }
}
