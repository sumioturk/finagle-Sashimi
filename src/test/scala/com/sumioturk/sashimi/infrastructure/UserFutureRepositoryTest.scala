package com.sumioturk.sashimi.domain.infrastructure

import config.FakeSashimiConfig
import infrastructure.UserFutureRepository
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import domain.User
import com.sumioturk.sashimi.service.CommonService
import com.twitter.util.Duration
import java.util.concurrent.TimeUnit


class UserFutureRepositoryTest extends Specification with Mockito {
  sequential
  val userId = "1"
  val user1 =
    User(
      id = "t1",
      twitterId = "1",
      is8th = false,
      name = "name",
      pass = "pass",
      sashimi = 1L,
      isPremium = false,
      isActive = true,
      escapeTerm = "et",
      lastTweetId = "1234",
      requestToken = "rt",
      requestTokenSecret = "rts",
      accessToken = "at",
      accessTokenSecret = "ats"
    )

  val user2 =
    User(
      id = "t2",
      is8th = false,
      twitterId = "1",
      name = "name",
      pass = "pass",
      sashimi = 1L,
      isPremium = true,
      isActive = false,
      escapeTerm = "",
      lastTweetId = "123123",
      requestToken = "rt",
      requestTokenSecret = "rts",
      accessToken = "at",
      accessTokenSecret = "ats"
    )

  val repo = new UserFutureRepository(new CommonService(FakeSashimiConfig).redis)

  "store" should {
    "return ()" in {
      repo.store(user1).get must_==()
    }
  }

  "store" should {
    "return ()" in {
      repo.store(user2).get must_==()
    }
  }

  "resolve" should {
    "return user identified by gevin id" in {
      repo.resolve("t1").get must_== user1
    }
  }

  "resolve" should {
    "return user identified by gevin id" in {
      repo.resolve("t2").get must_== user2
    }
  }

  "resolveAll" should {
    "return all users in the db" in {
      repo.resolveAll.get.filter(u => u.id.contains("t")) must_== List(user1, user2)
    }
  }

  "resolveAllActive" should {
    "return all active users" in {
      repo.resolveAllActive.get.filter(u => u.id.contains("t")) must_== List(user1)
    }
  }

  "resolveByName" should {
    "return users with given name" in {
      repo.resolveByName("name").get.filter(u => u.id.contains("t")) must_== List(user1, user2)
    }
  }

  "update" should {
    "timedout" in {
      try {
        repo.update("t2") {
          user: User =>
            val past = System.currentTimeMillis()
            repo.store(user1).get(Duration(2000, TimeUnit.MILLISECONDS))
            val now = System.currentTimeMillis()
            if (now - past > 2000) {
              throw new Exception()
            }
            User(
              id = user.id,
              twitterId = user.twitterId,
              name = user.name,
              sashimi = user.sashimi,
              lastTweetId = user.lastTweetId,
              pass = user.pass,
              isPremium = user.isPremium,
              isActive = true,
              is8th = user.is8th,
              escapeTerm = user.escapeTerm,
              requestToken = user.requestToken,
              requestTokenSecret = user.requestTokenSecret,
              accessToken = user.accessToken,
              accessTokenSecret = user.accessTokenSecret
            )
        }.get
        1 must_== 0
      } catch {
        case e: Exception =>
          1 must_== 1
      }
    }
  }

  "update" should {
    "return ()" in {
      repo.update("t1") {
        user: User =>
          User(
            id = user.id,
            twitterId = user.twitterId,
            name = user.name,
            sashimi = user.sashimi,
            lastTweetId = user.lastTweetId,
            pass = user.pass,
            isPremium = user.isPremium,
            isActive = user.isActive,
            is8th = true,
            escapeTerm = user.escapeTerm,
            requestToken = user.requestToken,
            requestTokenSecret = user.requestTokenSecret,
            accessToken = user.accessToken,
            accessTokenSecret = user.accessTokenSecret
          )
      }.get must_== ()
    }
  }
}
