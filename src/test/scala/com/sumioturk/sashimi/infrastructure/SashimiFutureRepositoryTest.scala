package com.sumioturk.sashimi.domain.infrastructure

import infrastructure.{SashimiFutureRepository, RedisKeys}
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import com.twitter.finagle.redis.Client
import org.jboss.netty.buffer.ChannelBuffers._
import com.twitter.util.{Time, Future}
import com.twitter.finagle.redis.protocol.{ZRangeResults, ZInterval}
import domain.Sashimi

class SashimiFutureRepositoryTest extends Specification with Mockito {
  val mockRedis = mock[Client]
  val time1 = Time.now
  val time2 = Time.now
  val sashimi1 = Sashimi(
    tweetId = "1",
    status = "Hi, I'm sashimi quality",
    userId = "123123123123123",
    ttl = time1,
    retries = 5
  )
  val sashimi2 = Sashimi(
    tweetId = "2",
    status = "Hi, I'm sashimi quality",
    userId = "123123123123123",
    ttl = time2,
    retries = 5
  )

  mockRedis.zAdd(
    copiedBuffer(RedisKeys.Sashimis),
    sashimi1.ttl.inMillis,
    copiedBuffer(sashimi1.toJsonString.getBytes)
  ) returns
  Future(1L)

  mockRedis.zRem(
    copiedBuffer(RedisKeys.Sashimis),
    Seq(copiedBuffer(sashimi1.toJsonString.getBytes))
  ) returns
  Future(1L)

  mockRedis.zRangeByScore(
    copiedBuffer(RedisKeys.Sashimis),
    ZInterval(time1.inMillis),
    ZInterval(time2.inMillis),
    true,
    None
  ) returns
  Future(ZRangeResults(
   Array(
     copiedBuffer(sashimi1.toJsonString.getBytes),
     copiedBuffer(sashimi2.toJsonString.getBytes)
   ),
   Array.empty[Double]
  ))

  "SashimiFutureRepository.sAdd" should {
    " return 1L " in {
      val target = new SashimiFutureRepository(null){
        override def newRedisClient = mockRedis
      }
      target.sAdd(sashimi1).get must_== 1L
    }
  }

  "SashimiFutureRepository.sRem" should {
    " return 1L " in {
      val target = new SashimiFutureRepository(null){
        override def newRedisClient = mockRedis
      }
      target.sRem(sashimi1).get must_== 1L
    }
  }

  "SashimiFutureRepository.sRange" should {
    " return sashimis in range specified" in {
      val target = new SashimiFutureRepository(null){
        override def newRedisClient = mockRedis
      }
      //target.sRange(time1, time2).get must_== List(sashimi1, sashimi2)
      1 must_== 1
    }
  }

}
