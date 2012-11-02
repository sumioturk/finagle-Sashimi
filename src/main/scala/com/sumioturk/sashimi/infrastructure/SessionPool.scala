package com.sumioturk.sashimi.infrastructure

import domain.User
import infrastructure.RedisKeys
import com.twitter.util.Future
import org.jboss.netty.buffer.ChannelBuffers._
import javax.xml.crypto.dsig.DigestMethod
import java.security.MessageDigest
import com.eaio.uuid.UUID
import sun.misc.BASE64Encoder
import com.twitter.finagle.redis.Client

class SessionPool(client: Client) {
  val redis = newRedisClient

  def resolve(sessionKey: String): Future[Option[String]] = {
    redis.hGet(
      copiedBuffer(RedisKeys.UserSessions),
      copiedBuffer(sessionKey.getBytes)
    ) flatMap {
      case Some(session) =>
        Future(Option(new String(session.array())))
      case None =>
        Future(None)
    }
  }

  def validate(sessionKey: String): Future[Boolean] = {
    redis.hGet(
      copiedBuffer(RedisKeys.UserSessions),
      copiedBuffer(sessionKey.getBytes)
    ) flatMap {
      case None => Future(false)
      case _ => Future(true)
    }
  }

  def store(sessionKey: String, userId: String) = {
    validate(sessionKey) flatMap {
      case false =>
        redis.hSet(
          copiedBuffer(RedisKeys.UserSessions),
          copiedBuffer(sessionKey.getBytes),
          copiedBuffer(userId.getBytes)
        )
      case true =>
        Future(Unit)
    }
  }

  def generateSessionKey(user: User) = {
    new BASE64Encoder().encode(MessageDigest.getInstance("SHA1")
      .digest(user.toString.getBytes)) + "_" + new UUID().toString + "_" + System.currentTimeMillis.toString
  }

  def newRedisClient = client
}
