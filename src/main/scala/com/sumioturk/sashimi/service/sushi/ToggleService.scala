package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.http.{Response, Request}
import domain.User
import infrastructure.UserFutureRepository
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.sumioturk.sashimi.service.CommonService
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.twitter.finagle.Service

class ToggleService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)

  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    userRepo.resolve(userId) flatMap {
      user =>
        val newUser = User(
          id = user.id,
          twitterId = user.twitterId,
          is8th = user.is8th,
          name = user.name,
          sashimi = user.sashimi,
          lastTweetId = user.lastTweetId,
          pass = user.pass,
          isPremium = user.isPremium,
          isActive =
            if (user.isActive == 1) {
              0
            } else {
              1
            },
          escapeTerm = user.escapeTerm,
          requestToken = user.requestToken,
          requestTokenSecret = user.requestTokenSecret,
          accessToken = user.accessToken,
          accessTokenSecret = user.accessTokenSecret
        )
        userRepo.store(newUser) flatMap {
          _ =>
            JsonResponse(newUser.toJson, OK)
        }
    }
  }
}
