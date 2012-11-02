package com.sumioturk.sashimi.domain.infrastructure

import infrastructure.{UserFutureRepository, RedisKeys}
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import com.twitter.finagle.redis.Client
import org.jboss.netty.buffer.ChannelBuffers._
import domain.User
import com.twitter.util.Future


class UserFutureRepositoryTest extends Specification with Mockito {
  val userId = copiedBuffer("1".getBytes)
  val user1 =
    User(
      id = "1",
      twitterId = "1",
      name = "name",
      pass = "pass",
      sashimi = 1L,
      isPremium = 0,
      isActive = 1,
      escapeTerm = "et",
      lastTweetId = "1234",
      requestToken = "rt",
      requestTokenSecret = "rts",
      accessToken = "at",
      accessTokenSecret = "ats"
    )

  val user2 =
    User(
      id = "2",
      twitterId = "1",
      name = "name",
      pass = "pass",
      sashimi = 1L,
      isPremium = 0,
      isActive = 1,
      escapeTerm = "",
      lastTweetId = "123123",
      requestToken = "rt",
      requestTokenSecret = "rts",
      accessToken = "at",
      accessTokenSecret = "ats"
    )

  val mockRedis = mock[Client]

  mockRedis.hGet(
    copiedBuffer(RedisKeys.Users),
    userId
  ) returns
    Future(Option(copiedBuffer(user1.toJsonString.getBytes)))

  mockRedis.hGetAll(
    copiedBuffer(RedisKeys.Users)
  ) returns
    Future(Seq(
      (userId, copiedBuffer(user1.toJsonString.getBytes)),
      (copiedBuffer("2".getBytes), copiedBuffer(user2.toJsonString.getBytes))
    ))

  mockRedis.hSet(
    copiedBuffer(RedisKeys.Users),
    userId,
    copiedBuffer(user1.toJsonString.getBytes)
  ) returns
    Future.value(1L)

  mockRedis.hDel(
    copiedBuffer(RedisKeys.Users),
    Seq(userId)
  ) returns
    Future.value(1L)

  "UserFutureRepository.resolve" should {
    " return the user identified by given id " in {
      val target = new UserFutureRepository(null) {
        override def newRedisClient = mockRedis
      }
      target.resolve("1").get must_== user1
    }
  }

  "UserFutureRepository.resolveAll" should {
    " return all users in the repository " in {
      val target = new UserFutureRepository(null) {
        override def newRedisClient = mockRedis
      }
      target.resolveAll.get must_== List(user1, user2)
    }
  }

  "UserFutureRepository.store" should {
    " return 1L " in {
      val target = new UserFutureRepository(null) {
        override def newRedisClient = mockRedis
      }
      target.store(user1).get must_== 1L
    }
  }

  "UserFutureRepository.purge" should {
    " return 1L " in {
      val target = new UserFutureRepository(null) {
        override def newRedisClient = mockRedis
      }
      target.purge("1").get must_== 1L
    }
  }
}
