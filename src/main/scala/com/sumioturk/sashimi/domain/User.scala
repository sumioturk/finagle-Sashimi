package domain

import net.liftweb.json.JsonDSL._
import net.liftweb.json._

case class User
(
  id: String,
  twitterId: String,
  name: String,
  pass: String,
  lastTweetId: String,
  sashimi: Long,
  isPremium: Boolean,
  isActive: Boolean,
  is8th: Boolean,
  escapeTerm: String,
  requestToken: String,
  requestTokenSecret: String,
  accessToken: String,
  accessTokenSecret: String
  ) {
  def toJson = {
    val json =
      ("id" -> id) ~
        ("twitter_id" -> twitterId) ~
        ("name" -> name) ~
        ("pass" -> pass) ~
        ("last_tweet_id" -> lastTweetId) ~
        ("sashimi" -> sashimi.toString) ~
        ("escape_term" -> escapeTerm) ~
        ("is_premium" -> isPremium) ~
        ("is_active" -> isActive) ~
        ("is_8th" -> is8th) ~
        ("request_token" -> requestToken) ~
        ("request_token_secret" -> requestTokenSecret) ~
        ("access_token" -> accessToken) ~
        ("access_token_secret" -> accessTokenSecret)
    json
  }

  def toJsonString = compact(render(toJson))
}

object User {
  def fromJson(json: JValue) = {
    json match {
      case JObject(List(
      JField("id", JString(id)),
      JField("twitter_id", JString(twitterId)),
      JField("name", JString(name)),
      JField("pass", JString(pass)),
      JField("last_tweet_id", JString(lastTweetId)),
      JField("sashimi", JString(sashimi)),
      JField("escape_term", JString(escapeTerm)),
      JField("is_premium", JBool(isPremium)),
      JField("is_active", JBool(isActive)),
      JField("is_8th", JBool(is8th)),
      JField("request_token", JString(requestToken)),
      JField("request_token_secret", JString(requestTokenSecret)),
      JField("access_token", JString(accessToken)),
      JField("access_token_secret", JString(accessTokenSecret))
      )) =>
        User(
          id = id,
          twitterId = twitterId,
          name = name,
          pass = pass,
          lastTweetId = lastTweetId,
          sashimi = sashimi.toLong,
          escapeTerm = escapeTerm,
          isPremium = isPremium,
          isActive = isActive,
          is8th = is8th,
          requestToken = requestToken,
          requestTokenSecret = requestTokenSecret,
          accessToken = accessToken,
          accessTokenSecret = accessTokenSecret
        )
      case _ =>
        throw new IllegalArgumentException("Malformed Json")
    }
  }

  def fromJsonString(s: String) = fromJson(parse(s))

  val Name = "name"
  val Sashimi = "sashimi"
  val Pass = "pass"
  val Id = "id"
  val LastTweetId = "last_tweet_id"
  val EscapeTerm = "escape_term"
  val IsPremium = "is_premium"
  val RequestToken = "request_token"
  val RequestTokenSecret = "request_token_secret"
  val AccessToken = "access_token"
  val AccessTokenSecret = "access_token_secret"

  val UserIdSeq = "UserIdSeq"
  val Cookie = "Cookie"
  val DummyCookie = "Dummy"
  val Key = "key"
  val Identity = "Identity"
  val OAuthToken = "oauth_token"
  val OAuthVerifier = "oauth_verifier"
  val Status = "status"
  val SessionKey = "session_key"
  val Message = "message"
}
