package com.sumioturk.sashimi.service

import com.twitter.finagle.redis.{TransactionalClient, Client, Redis}
import config.SashimiConfig
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.TwitterApi
import com.twitter.finagle.builder.ClientBuilder

case class CommonService(config: SashimiConfig) {

  val redis = TransactionalClient(
    ClientBuilder()
      .codec(new Redis())
      .hosts(config.redisHostPort)
      .hostConnectionLimit(1)
      .buildFactory())

  val twitter = new ServiceBuilder()
    .provider(classOf[TwitterApi])
    .apiKey(config.twitterApiKey)
    .apiSecret(config.twitterApiKeySecret)
    .callback(config.twitterApiCallbackURL)
    .build()

}
