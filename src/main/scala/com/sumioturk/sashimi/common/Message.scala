package com.sumioturk.sashimi.common

import net.liftweb.json.JsonAST.{JObject, JField, JString}

object Message {
  val UserNotFound = "User Not Found"
  val UserAlreadyExists = "User Already Exists"
  val InvalidParams = "Invalid Params"
  val AuthenticationRequired = "Authentication Required"
  val CouldNotAuthorize = "Could Not Authorize"
  val ExpiredCookie = "Expired Cookie"
  val TokenNotFound = "Token Not Found"
  val MalformedJson = "Malformed Json"
  val SashimiQualityMessage = "Hi, I'm Sashimi-Quality"
  val LoggedIn = "You Logged In"
  val LoggedOut = "You Logged Out"
  val Tweeted = "Tweeted"
  val SashimiUpdated = "Sashimi Updated"
  val InternalServerError = "Internal Server Error"

  def toJson(s: String) = {
    val json = JObject(JField("message", JString(s)) :: Nil)
    json
  }
}
