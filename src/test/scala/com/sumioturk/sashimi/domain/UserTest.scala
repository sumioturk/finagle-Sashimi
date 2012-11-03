package com.sumioturk.sashimi.domain

import domain.User


class UserTest extends org.specs2.mutable.Specification {
  "User" should {
    "be Serializable." in {
      val user =
        User(
          id = "id",
          is8th = false,
          twitterId = "1",
          name = "name",
          pass = "pass",
          sashimi = 1L,
          isPremium = false,
          isActive = false,
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
