package com.sumioturk.sashimi.domain

import domain.Sashimi
import org.specs2.mutable.Specification
import com.twitter.util.Time

class SashimiTest extends Specification {
  "Sashimi" should {
    "be Serializable." in {
      val sashimi = Sashimi(
        tweetId = "12123123",
        status = "Hi, I'm sashimi quality",
        userId = "123123123123123",
        ttl = Time.fromMilliseconds(System.currentTimeMillis),
        retries = 5
      )
      Sashimi.fromJsonString(sashimi.toJsonString) must_== sashimi
      1 must_== 1
    }
  }

}
