package com.sumioturk.sashimi.infrastructure

import domain.User
import org.jboss.netty.buffer.ChannelBuffers._
import com.twitter.finagle.redis.Client

class IdRepository(client: Client) {
  val redis = newRedisClient

  def getNewUserId = {
    redis.decrBy(copiedBuffer(User.UserIdSeq.getBytes), -1)
  }

  def newRedisClient = client
}
