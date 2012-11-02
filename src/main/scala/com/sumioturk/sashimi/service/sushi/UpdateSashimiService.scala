package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.http.{Response, Request}
import com.sumioturk.sashimi.common.Validation
import domain.User
import infrastructure.UserFutureRepository
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.sumioturk.sashimi.service.CommonService
import com.sumioturk.sashimi.common.Validation.Rule
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.sumioturk.sashimi.common.Message._
import com.twitter.finagle.Service

class UpdateSashimiService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)
  val twitter = commons.twitter

  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    val userSashimi = request.getParam(User.Sashimi)
    Validation.isValid(Rule.Number, userSashimi) match {
      case true =>
        userRepo.resolve(userId) flatMap {
          user =>
            val newUser = User(
              id = user.id,
              twitterId = user.twitterId,
              name = user.name,
              sashimi = userSashimi.toLong,
              lastTweetId = user.lastTweetId,
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
              JsonResponse(newUser.toJson, OK)
            }
        }
      case false =>
        JsonResponse(toJson(InvalidParams), FORBIDDEN)
    }
  }
}
