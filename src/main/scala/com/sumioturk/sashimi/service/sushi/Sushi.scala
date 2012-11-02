package sushi

import com.twitter.finagle.http.path._
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Response, Request, Http, RichHttp}
import com.twitter.util.Eval
import java.net.InetSocketAddress
import com.twitter.finagle.builder.{Server, ServerBuilder}
import config.SashimiConfig
import java.io.File
import com.sumioturk.sashimi.service.sushi._
import com.sumioturk.sashimi.service.CommonService
import com.twitter.finagle.http.path./
import org.slf4j.LoggerFactory
import com.twitter.finagle.Service
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter

object Sushi extends App {
  val logger = LoggerFactory.getLogger(Sushi.getClass)
  StatusPrinter.print((LoggerFactory.getILoggerFactory).asInstanceOf[LoggerContext])
  val config = Eval[SashimiConfig](new File(args(0)))
  val commons = new CommonService(config)

  val handleExceptions = new HandleExceptionFilter
  val joinService = new JoinService(commons)
  val loginService = new LoginService(commons)
  val authorize = new AuthorizationFilter(commons)
  val oAuthURL = new OAuthURLService(commons)
  val oAuthTokenRegisterService = new RegisterOAuthTokenService(commons)
  val tweetService = new TweetService(commons)
  val updateSashimiService = new UpdateSashimiService(commons)
  val getUserProfileService = new GetUserProfileService(commons)
  val toggleService = new ToggleService(commons)
  val loggingFilter = new LoggingFilter(logger)
  val cureEighthGraderService = new CureEighthGraderSyndromeService(commons)

  private def logWithAuth(service: Service[Request, Response]) = {
    loggingFilter andThen handleExceptions andThen authorize andThen service
  }

  private def log(service: Service[Request, Response]) = {
    loggingFilter andThen handleExceptions andThen service
  }

  val routingService =
    RoutingService.byPathObject {
      case Root / "join" => log(joinService)
      case Root / "login" => log(loginService)
      case Root / "oauth_url" => logWithAuth(oAuthURL)
      case Root / "oauth" => logWithAuth(oAuthTokenRegisterService)
      case Root / "tweet" => logWithAuth(tweetService)
      case Root / "update_sashimi" => logWithAuth(updateSashimiService)
      case Root / "get_user_profile" => logWithAuth(getUserProfileService)
      case Root / "toggle" => logWithAuth(toggleService)
      case Root / "8" => logWithAuth(cureEighthGraderService)
    }


  val server: Server = ServerBuilder()
    .codec(RichHttp[Request](Http()))
    .bindTo(new InetSocketAddress(config.sushiPort))
    .name("sushid")
    .build(routingService)
}


