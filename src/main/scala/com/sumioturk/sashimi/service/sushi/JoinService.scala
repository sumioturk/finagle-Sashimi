package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.http.{Request, Response}
import domain.User
import infrastructure.UserFutureRepository
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.twitter.finagle.Service
import com.sumioturk.sashimi.common.Message._
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.sumioturk.sashimi.infrastructure.IdRepository
import com.sumioturk.sashimi.common.Validation
import com.sumioturk.sashimi.service.CommonService

class JoinService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)
  val idRepo = new IdRepository(commons.redis)
  val twitterAPI = commons.twitter
  def apply(request: Request) = {
    val userName = request.getParam(User.Name)
    val userPass = request.getParam(User.Pass)
    val userSashimi = request.getParam(User.Sashimi)

    Validation.validateAtOnce(Seq(
      (Validation.Rule.Name, userName),
      (Validation.Rule.Pass, userPass),
      (Validation.Rule.Number, userSashimi)
    )) match {
      case true =>
        userRepo.isUniqueName(userName) flatMap {
          case false =>
            JsonResponse(toJson(UserAlreadyExists), CONFLICT)
          case true =>
            idRepo.getNewUserId flatMap {
              userId =>
                val requestToken = twitterAPI.getRequestToken
                val newUser = User(
                  id = userId.toString,
                  twitterId = "",
                  name = userName,
                  pass = userPass,
                  sashimi = userSashimi.toLong,
                  isPremium = 0,
                  isActive = 0,
                  lastTweetId = "",
                  requestToken = requestToken.getToken,
                  requestTokenSecret = requestToken.getSecret,
                  accessToken = "",
                  accessTokenSecret = "",
                  escapeTerm = ""
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
