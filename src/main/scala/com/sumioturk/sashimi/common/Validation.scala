package com.sumioturk.sashimi.common


object Validation {

  object Rule extends Enumeration {
    val Number = """^[1-9]+[0-9]*$"""
    val Name = """^[a-zA-Z-_]+$"""
    val Pass = """^[a-zA-Z-_]+$"""
    val OAuthToken = "^.+$"
    val OAuthVerifier = "^.+$"
    val Status = ".+"

  }

  def isValid(rule: String, s: String): Boolean = {
    if (rule == null || s == null) {
      false
    } else {
      rule.toString.r.findFirstIn(s) match {
        case None => false
        case Some(sth) => true
      }
    }
  }

  def validateAtOnce(prs: Seq[(String, String)]): Boolean = {
    prs.map {
      pr =>
        isValid(pr._1, pr._2)
    } exists (f => f == false) match {
      case true => false
      case false => true
    }
  }
}
