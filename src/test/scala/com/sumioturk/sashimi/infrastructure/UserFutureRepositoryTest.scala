package com.sumioturk.sashimi.domain.infrastructure

import infrastructure.{UserFutureRepository, RedisKeys}
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import com.twitter.finagle.redis.{TransactionalClient, Client}
import org.jboss.netty.buffer.ChannelBuffers._
import domain.User
import com.twitter.util.Future


class UserFutureRepositoryTest extends Specification with Mockito {
  val userId = "1"
  val user1 =
    User(
      id = "1",
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
      id = "2",
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

  val mockRedis = mock[TransactionalClient]

  mockRedis.get(
    copiedBuffer(RedisKeys.Users(userId))
  ) returns
    Future(Option(copiedBuffer(user1.toJsonString.getBytes)))

  mockRedis.keys(
    copiedBuffer(RedisKeys.Users("*"))
  ) returns
    Future(Seq(
      copiedBuffer(user1.toJsonString.getBytes),
      copiedBuffer("2".getBytes), copiedBuffer(user2.toJsonString.getBytes)
    ))

  mockRedis.set(
    copiedBuffer(RedisKeys.Users(userId)),
    copiedBuffer(user1.toJsonString.getBytes)
  ) returns
    Future.value(1L)

  mockRedis.del(
    copiedBuffer(RedisKeys.Users(userId)) :: Nil
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
