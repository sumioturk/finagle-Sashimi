package com.sumioturk.sashimi.service.sushi

import com.sumioturk.sashimi.common.Validation.Rule
import config.SashimiConfig
import com.twitter.finagle.http.{Request, Response}
import domain.User
import infrastructure.UserFutureRepository
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.twitter.finagle.Service
import com.sumioturk.sashimi.common.Message._
import com.sumioturk.sashimi.service.ServiceUtilities._
import com.sumioturk.sashimi.infrastructure.SessionPool
import com.sumioturk.sashimi.common.Validation
import com.sumioturk.sashimi.service.CommonService
import net.liftweb.json.JsonAST.{JString, JField, JObject}

class LoginService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)
  val sessionPool = new SessionPool(commons.redis)

  def apply(request: Request) = {
    val sessionKey = User.SessionKeyRegex.findFirstIn(
      Option(request.getHeader(User.Cookie)).getOrElse(User.DummyCookie)
    )
    sessionKey match {
      case Some(sk) =>
        sessionPool.purge(sk.substring(4, sk.length - 1)) flatMap {
         _ =>
           ExpCookieJsonResponse(JObject(JField(User.Message, JString(LoggedOut)) :: Nil), OK)
        }
      case None =>
        val userName = request.getParam(User.Name)
        val userPass = request.getParam(User.Pass)
        Validation.validateAtOnce(Seq(
          (Rule.Name, userName),
          (Rule.Pass, userPass)
        )) match {
          case true =>
            userRepo.resolveByName(userName) flatMap {
              users =>
                (users.isEmpty) match {
                  case true =>
                    JsonResponse(toJson(CouldNotAuthorize), FORBIDDEN)
                  case false =>
                    (users(0).pass == userPass) match {
                      case false =>
                        JsonResponse(toJson(CouldNotAuthorize), FORBIDDEN)
                      case true =>
                        val sessionPool = new SessionPool(commons.redis)
                        val sessionKey = sessionPool.generateSessionKey(users(0))
                        sessionPool.store(sessionKey, users(0).id)
                        SetCookieJsonResponse(
                          JObject(JField(User.SessionKey, JString(sessionKey)) :: Nil),
                          OK,
                          sessionKey,
                          users(0))
                    }
                }
            }
          case false =>
            JsonResponse(toJson(InvalidParams), FORBIDDEN)
        }
    }
  }
}
