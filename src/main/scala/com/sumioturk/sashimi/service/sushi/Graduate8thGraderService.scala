package com.sumioturk.sashimi.service.sushi

import com.sumioturk.sashimi.service.CommonService
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Response, Request}
import domain.User
import infrastructure.UserFutureRepository
import com.sumioturk.sashimi.infrastructure.IdRepository
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.sumioturk.sashimi.service.CommonService
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.sumioturk.sashimi.service.CommonService
import com.sumioturk.sashimi.common.Message._
import com.sumioturk.sashimi.service.CommonService

class Graduate8thGraderService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)
  val idRepo = new IdRepository(commons.redis)

  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    userRepo.resolve(userId) flatMap {
      user =>
        if (user.is8th) {
          val eighthGrader = User(
            id = user.id,
            twitterId = user.twitterId,
            name = user.name,
            sashimi = user.sashimi,
            lastTweetId = user.lastTweetId,
            pass = user.pass,
            isPremium = user.isPremium,
            isActive = user.isActive,
            is8th = false,
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
          JsonResponse(toJson(InvalidParams), FORBIDDEN)
        }
    }
  }
}
