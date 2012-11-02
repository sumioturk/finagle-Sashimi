package wasabi


import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.redis.{Redis, Client}
import com.twitter.util.{Eval, Time, Future}
import infrastructure.{SashimiFutureRepository, UserFutureRepository, RedisKeys}
import domain.Sashimi
import maguro.Maguro
import org.scribe.model.{Verb, OAuthRequest, Token}
import net.liftweb.json._
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.TwitterApi
import java.text.SimpleDateFormat
import com.twitter.finagle.redis.protocol.ZInterval
import scala.Some
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import config.SashimiConfig
import java.io.File
import java.util.Date
import com.sumioturk.sashimi.service.CommonService
import org.slf4j.LoggerFactory


object Wasabi extends App {

  val config = Eval[SashimiConfig](new File(args(0)))
  val logger = LoggerFactory.getLogger(Maguro.getClass)
  val commons = new CommonService(config)
  val userRepo = new UserFutureRepository(commons.redis)
  val sashimiRepo = new SashimiFutureRepository(commons.redis)
  val twitter = commons.twitter

  private def wait(f: Unit, wtime: Long) = {
    Thread.sleep(wtime)
    f
  }

  private def loop(): Unit = {
    sashimiRepo.sRange(
      Time.fromMilliseconds(0),
      Time.now,
      config.numberOfMaxRetries
    ) flatMap {
      sashimis =>
        Future.join(
          sashimis map {
            sashimi =>
              userRepo.resolve(sashimi.userId) flatMap {
                user =>
                  val accessToken = new Token(
                    user.accessToken,
                    user.accessTokenSecret
                  )
                  val twitterRequest = new OAuthRequest(
                    Verb.POST,
                    config.twitterApiDestroyURL.format(sashimi.tweetId)
                  )
                  twitter.signRequest(accessToken, twitterRequest)
                  Future(twitterRequest.send().getBody) onSuccess {
                    response =>
                      val json = parse(response)
                      if ((json \\ "id_str").children.length == 0) {
                        logger.info("failed to delete tweet: " + sashimi.toJsonString)
                        val newSashimi = Sashimi(
                          userId = sashimi.userId,
                          retries = sashimi.retries + 1,
                          ttl = Time.fromMilliseconds(Time.now.inMillis
                            + config.retryInterval),
                          tweetId = sashimi.tweetId
                        )
                        Future.join(Seq(
                          sashimiRepo.sAdd(newSashimi),
                          sashimiRepo.sRem(sashimi)
                        ))
                      } else {
                        logger.info("deleted tweet: " + sashimi.toJsonString)
                        sashimiRepo.sRem(sashimi)
                      }
                  } onFailure {
                    e =>
                      e.printStackTrace
                  }
              }
          }
        )
    } ensure wait(loop, config.wasabiPollingInterval)
  }

  loop()
}
