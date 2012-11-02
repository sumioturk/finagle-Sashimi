package com.sumioturk.sashimi.service.sushi

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response}
import org.slf4j.{Logger, LoggerFactory}

class LoggingFilter(logger: Logger) extends SimpleFilter[Request, Response] {
  def apply(request: Request, continue: Service[Request, Response]) = {
    logger.info(request.getUri)
    continue(request)
  }
}
