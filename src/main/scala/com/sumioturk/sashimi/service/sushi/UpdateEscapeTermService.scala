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

class UpdateEscapeTermService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)

  private def isValidRegex(s: String) = {
    try {
      s.r
      true
    } catch {
      case e: Throwable =>
        false
    }
  }

  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    val escapeTerm = request.getParam(User.EscapeTerm)
    Validation.isValid(Rule.NotEmpty, escapeTerm) && (isValidRegex(escapeTerm)) match {
      case true =>
        userRepo.resolve(userId) flatMap {
          user =>
            val newUser = User(
              id = user.id,
              is8th = user.is8th,
              twitterId = user.twitterId,
              name = user.name,
              sashimi = user.sashimi,
              lastTweetId = user.lastTweetId,
              pass = user.pass,
              isPremium = user.isPremium,
              isActive = user.isActive,
              escapeTerm = escapeTerm,
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

