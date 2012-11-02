package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Response, Request}
import com.sumioturk.sashimi.service.CommonService
import domain.User
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import infrastructure.UserFutureRepository
import com.sumioturk.sashimi.service.ServiceUtilities._

class GetUserProfileService(commons: CommonService) extends Service[Request, Response] {
  val userRepo = new UserFutureRepository(commons.redis)

  def apply(request: Request) = {
    val userId = request.getHeader(User.Identity)
    userRepo.resolve(userId) flatMap {
      user =>
        JsonResponse(user.toJson, OK)
    }
  }
}
