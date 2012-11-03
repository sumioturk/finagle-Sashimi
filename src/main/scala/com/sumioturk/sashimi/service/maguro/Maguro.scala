package maguro


import com.twitter.util.{Eval, Time, Future}
import infrastructure.{SashimiFutureRepository, UserFutureRepository}
import domain.{Sashimi, User}
import org.scribe.model.{Verb, OAuthRequest, Token}
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import java.text.SimpleDateFormat
import config.SashimiConfig
import java.io.File
import com.sumioturk.sashimi.service.CommonService
import org.slf4j.LoggerFactory


object Maguro extends App {
  val logger = LoggerFactory.getLogger(Maguro.getClass)
  val config = Eval[SashimiConfig](new File(args(0)))
  val commons = new CommonService(config)
  val userRepo = new UserFutureRepository(commons.redis)
  val sashimiRepo = new SashimiFutureRepository(commons.redis)
  val twitter = commons.twitter

  private def wait(f: Unit, wtime: Long) = {
    Thread.sleep(wtime)
    f
  }

  private def getDateOfDisposal(s: String, sashimi: Long) = {
    val dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").parse(s)
    dateFormat.getTime + sashimi
  }

  private def loop(): Unit = {
    userRepo.resolveAllActive flatMap {
      users =>
        Future.join(
          users map {
            user =>
              val twitterRequest = new OAuthRequest(Verb.GET, config.twitterApiTimelineURL)
              val accessToken = new Token(user.accessToken, user.accessTokenSecret)
              if (!user.is8th) {
                twitterRequest.addQuerystringParameter("since_id", user.lastTweetId)
              }
              twitterRequest.addQuerystringParameter("user_id", user.twitterId)
              twitter.signRequest(accessToken, twitterRequest)
              Future(twitterRequest.send().getBody) onSuccess {
                response =>
                  val json = parse(response)
                  if ((json \\ "error").children.length != 0) {
                    logger.info(compact(render(json)))
                    Future.None
                  } else {
                    val sashimis: List[Sashimi] = for {
                      JArray(tweets) <- json
                      JObject(tweet) <- tweets
                      JField("id_str", JString(tweet_id)) <- tweet
                      JField("created_at", JString(created_at)) <- tweet
                      JField("id_str", JString(user_id)) <- (tweet \ "user")
                    } yield Sashimi(
                        userId = user.id,
                        tweetId = tweet_id,
                        ttl =
                          if (user.is8th) {
                            Time.now
                          } else {
                            Time.fromMilliseconds(
                              getDateOfDisposal(
                                created_at,
                                user.sashimi * config.sashimiMaginification
                              )
                            )
                          },
                        retries = 0
                      )
                    val newUser = User(
                      id = user.id,
                      is8th = user.is8th,
                      twitterId = user.twitterId,
                      name = user.name,
                      pass = user.pass,
                      lastTweetId = if (sashimis.isEmpty) {
                        user.lastTweetId
                      } else {
                        sashimis.maxBy(_.tweetId).tweetId
                      },
                      sashimi = user.sashimi,
                      isPremium = user.isPremium,
                      isActive = user.isActive,
                      escapeTerm = user.escapeTerm,
                      requestToken = user.requestToken,
                      requestTokenSecret = user.requestTokenSecret,
                      accessToken = user.accessToken,
                      accessTokenSecret = user.accessTokenSecret
                    )
                    sashimis.map(s => logger.info(s.toString))
                    Future.join(
                      sashimis.map(s => sashimiRepo.sAdd(s)).toSeq ++ Seq(userRepo.store(newUser))
                    ) flatMap {
                      _ =>
                        Future.None
                    }
                  }
              } onFailure {
                e =>
                  e.printStackTrace()
              }
          })
    } ensure wait(loop, config.maguroPollingInterval)
  }

  loop()
}
