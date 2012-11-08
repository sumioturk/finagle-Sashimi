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

