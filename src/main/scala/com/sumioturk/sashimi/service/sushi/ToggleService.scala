package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.http.{Response, Request}
import domain.User
import infrastructure.UserFutureRepository
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.sumioturk.sashimi.service.CommonService
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.twitter.finagle.Service
import org.scribe.model.{Token, Verb, OAuthRequest}
import net.liftweb.json._

class ToggleService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)

  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    userRepo.update(userId) {
      user =>
        val twitterRequest = new OAuthRequest(Verb.GET, commons.config.twitterApiTimelineURL)
        val accessToken = new Token(user.accessToken, user.accessTokenSecret)
        twitterRequest.addQuerystringParameter("user_id", user.twitterId)
        twitterRequest.addQuerystringParameter("count", "1")
        commons.twitter.signRequest(accessToken, twitterRequest)
        // This is really blocking...
        val latestTweetId = (parse(twitterRequest.send().getBody) \ "id_str").values.toString
        User(
          id = user.id,
          twitterId = user.twitterId,
          is8th = user.is8th,
          name = user.name,
          sashimi = user.sashimi,
          lastTweetId = latestTweetId,
          pass = user.pass,
          isPremium = user.isPremium,
          isActive =
            if (user.isActive) {
              false
            } else {
              true
            },
          escapeTerm = user.escapeTerm,
          requestToken = user.requestToken,
          requestTokenSecret = user.requestTokenSecret,
          accessToken = user.accessToken,
          accessTokenSecret = user.accessTokenSecret
        )
    } flatMap {
      user =>
        JsonResponse(user.toJson, OK)
    }
  }
}
