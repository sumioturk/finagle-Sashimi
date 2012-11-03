package domain

import com.twitter.util.Time
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import java.text.SimpleDateFormat

case class Sashimi
(
  userId: String,
  tweetId: String,
  status: String,
  ttl: Time,
  retries: Int
  ) {
  def toJson = {
    val json =
      ("user_id" -> userId) ~
        ("tweet_id" -> tweetId) ~
        ("status" -> status) ~
        ("ttl" -> ttl.toDate.toString) ~
        ("retries" -> retries)
    json
  }

  def toJsonString = compact(render(toJson))
}

object Sashimi {
  def fromJson(json: JValue) = {
    val dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
    json match {
      case JObject(List(
      JField("user_id", JString(userId)),
      JField("tweet_id", JString(tweetId)),
      JField("status", JString(status)),
      JField("ttl", JString(ttl)),
      JField("retries", JInt(retries))
      )) =>
        Sashimi(
          userId = userId,
          tweetId = tweetId,
          status = status,
          ttl = Time.fromMilliseconds(dateFormat.parse(ttl).getTime),
          retries = retries.toInt
        )
      case _ =>
        throw new IllegalArgumentException("Malformed Json")
    }
  }

  def fromJsonString(s: String) = fromJson(parse(s))
}



