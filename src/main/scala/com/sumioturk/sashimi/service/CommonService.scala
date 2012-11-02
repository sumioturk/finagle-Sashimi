package com.sumioturk.sashimi.service

import com.twitter.finagle.redis.{Client, Redis}
import config.SashimiConfig
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.TwitterApi
import com.twitter.finagle.builder.ClientBuilder

case class CommonService(config: SashimiConfig) {

  val redis = Client(
    ClientBuilder()
      .hosts(config.redisHostPort)
      .hostConnectionLimit(config.redisHostConnectionLimit)
      .codec(Redis())
      .build()
  )

  val twitter = new ServiceBuilder()
    .provider(classOf[TwitterApi])
    .apiKey(config.twitterApiKey)
    .apiSecret(config.twitterApiKeySecret)
    .callback(config.twitterApiCallbackURL)
    .build()

}
