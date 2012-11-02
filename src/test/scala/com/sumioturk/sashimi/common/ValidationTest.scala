package com.sumioturk.sashimi.common

import org.specs2.mutable.Specification
import com.sumioturk.sashimi.common.Validation.Rule

class ValidationTest extends Specification {
  "Validation.isValid taking a Number" should {
    "return true" in {
      Validation.isValid(Rule.Number, "1234567") must_== true
    }
  }
  "Validation.isValid taking a nonNumber" should {
    "return false" in {
      Validation.isValid(Rule.Number, " 1234567") must_== false
    }
  }
  "Validation.isValid taking a Name" should {
    "return true" in {
      Validation.isValid(Rule.Name, "asfASCVEW_-f") must_== true
    }
  }
  "Validation.isValid taking a nonName" should {
    "return false" in {
      Validation.isValid(Rule.Name, "as3fASCVEW_-f") must_== false
    }
  }
  "Validation.validateAtOnce taking acceptables" should {
    "return true" in {
      Validation.validateAtOnce(Seq(
        (Rule.Name, "abcd-_a_"),
        (Rule.Number, "2342345"),
        (Rule.Pass, "abcd-_a_")
      )) must_== true
    }
  }
  "Validation.validateAtOnce taking some unacceptables" should {
    "return false" in {
      Validation.validateAtOnce(Seq(
        (Rule.Name, "abcd-_a_"),
        (Rule.Number, " 2342345"),
        (Rule.Pass, "abcd-_a_")
      )) must_== false
    }
  }
}
