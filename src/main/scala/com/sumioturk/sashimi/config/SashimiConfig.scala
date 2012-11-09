package config

import com.twitter.finagle.redis.{Redis, Client}
import org.scribe.oauth.OAuthService

trait SashimiConfig {
  val wasabiPollingInterval: Int
  val maguroPollingInterval: Int
  val sushiPort: Int
  val redisHostPort: String
  val twitterApiKey: String
  val twitterApiKeySecret: String
  val twitterApiCallbackURL: String
  val redisHostConnectionLimit: Int

  val twitterApiDestroyURL: String
  val twitterApiUpdateURL: String
  val twitterApiTimelineURL: String
  val sashimiMaginification: Int
  val contentTypeEncoding: String
  val contentType: String
  val disposalTimeInMillis: Long
  val retryInterval: Long
  val maxRetries: Int
}

object FakeSashimiConfig extends SashimiConfig {
  val wasabiPollingInterval = 1000
  val maguroPollingInterval = 10000
  val sushiPort = 9000
  val redisHostPort = "localhost:6379"
  val twitterApiKey = "3yMyK3UJJYVTgnZIPWkqUA"
  val twitterApiKeySecret = "0qoaluSJg4MoRIncQoMR049ZZnuKkQhpI1rWtUJu2Q"
  val twitterApiCallbackURL = "http://localhost:9000/oauth"
  val redisHostConnectionLimit = 50
  val twitterApiDestroyURL = "https://api.twitter.com/1.1/statuses/destroy/%s.json"
  val twitterApiUpdateURL = "https://api.twitter.com/1.1/statuses/update.json"
  val twitterApiTimelineURL = "https://api.twitter.com/1.1/statuses/user_timeline.json"
  val sashimiMaginification = 1000 * 60
  val contentTypeEncoding = "UTF-8"
  val contentType = "application/json"
  val disposalTimeInMillis = 5 * 60 * 1000L
  val retryInterval = 60 * 1000L
  val maxRetries = 4
}
