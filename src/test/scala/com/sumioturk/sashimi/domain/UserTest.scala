package com.sumioturk.sashimi.domain

import domain.User


class UserTest extends org.specs2.mutable.Specification {
  "User" should {
    "be Serializable." in {
      val user =
        User(
          id = "id",
          twitterId = "1",
          name = "name",
          pass = "pass",
          sashimi = 1L,
          isPremium = 0,
          isActive = 2,
          escapeTerm = "",
          lastTweetId = "123123",
          requestToken = "rt",
          requestTokenSecret = "rts",
          accessToken = "at",
          accessTokenSecret = "ats"
        )
      user must_== User.fromJsonString(user.toJsonString)
    }
  }
}
