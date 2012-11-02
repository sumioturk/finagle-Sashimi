package infrastructure

import domain.Sashimi
import com.twitter.util.{Time, Future}
import org.jboss.netty.buffer.ChannelBuffers._
import com.twitter.finagle.redis.protocol.ZInterval
import com.twitter.finagle.redis.Client

class SashimiFutureRepository(client: Client) extends SortedFutureRepository[Sashimi] {
  val redis = newRedisClient

  def sAdd(sashimi: Sashimi) = {
    redis.zAdd(
      copiedBuffer(RedisKeys.Sashimis),
      sashimi.ttl.inMillis,
      copiedBuffer(sashimi.toJsonString.getBytes)
    )
  }

  def sRem(sashimi: Sashimi) = {
    redis.zRem(
      copiedBuffer(RedisKeys.Sashimis),
      Seq(copiedBuffer(sashimi.toJsonString.getBytes))
    )
  }

  def sRange(min: Time, max: Time, maxRetries: Int) = {
    redis.zRangeByScore(
      copiedBuffer(RedisKeys.Sashimis),
      ZInterval(min.inMillis),
      ZInterval(max.inMillis),
      true,
      None
    ) flatMap {
      zRangeResult =>
        Future(
          zRangeResult
            .entries
            .map(a => Sashimi.fromJsonString(new String(a.array)))
            .filter(s => s.retries <= maxRetries)
            .toList
        )
    }
  }

  def newRedisClient = client
}
