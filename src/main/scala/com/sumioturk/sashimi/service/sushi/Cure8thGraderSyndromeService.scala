package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.http.{Request, Response}
import domain.User
import infrastructure.UserFutureRepository
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.twitter.finagle.Service
import com.sumioturk.sashimi.common.Message._
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.sumioturk.sashimi.infrastructure.IdRepository
import com.sumioturk.sashimi.service.CommonService

class Cure8thGraderSyndromeService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)
  val idRepo = new IdRepository(commons.redis)

  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    userRepo.resolve(userId) flatMap {
      user =>
        if (user.isActive == 1 && user.isPremium == 1) {
          val eighthGrader = User(
            id = user.id,
            twitterId = user.twitterId,
            is8th = 1,
            name = user.name,
            sashimi = user.sashimi,
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
          userRepo.store(eighthGrader) flatMap {
            _ =>
              JsonResponse(eighthGrader.toJson, OK)
          }
        } else {
          JsonResponse(toJson(PremiumOnly), FORBIDDEN)
        }
    }
  }
}
