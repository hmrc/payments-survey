/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pagespec

import model._
import org.openqa.selenium.By
import org.scalatest.concurrent.Eventually.eventually
import stubs.PayApiStubFindJourneyBySessionId
import support.PageSpec

class SurveyPageSpec extends PageSpec {
  val pagePath = s"/payments-survey/survey"
  val surveyThanksPath = s"/payments-survey/survey-thanks"
  "should render correctly" in new TestWithSession {

    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)

    goTo(baseUrl + pagePath)

    cssSelector(".govuk-header__link--service-name")
      .element.text shouldEqual journey.contentOptions.title.englishValue

    pageTitle shouldBe "How was our payment service? - Pay your Self Assessment - GOV.UK"

    id("were-you-able-label").element.text shouldBe "1. Were you able to do what you needed to do today?"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(3) > div > fieldset > div > div:nth-child(1) > label")
      .element.text shouldBe "Yes"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(3) > div > fieldset > div > div:nth-child(2) > label")
      .element.text shouldBe "No"

    id("how-easy-label")
      .element.text shouldBe "2. How easy was it for you to do what you needed to do today?"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(1) > label")
      .element.text shouldBe "Very easy"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(2) > label")
      .element.text shouldBe "Easy"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(3) > label")
      .element.text shouldBe "Neither easy or difficult"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(4) > label")
      .element.text shouldBe "Difficult"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(5) > label")
      .element.text shouldBe "Very difficult"

    id("why-score-label").element.text shouldBe "2b. Why did you give this score?"
    id("comments").element.attribute("value") shouldBe Some("")

    id("overall-label")
      .element.text shouldBe "3. Overall, how did you feel about the service you received today?"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(1) > label")
      .element.text shouldBe "Very satisfied"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(2) > label")
      .element.text shouldBe "Satisfied"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(3) > label")
      .element.text shouldBe "Neither satisfied or dissatisfied"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(4) > label")
      .element.text shouldBe "Dissatisfied"
    cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(5) > label")
      .element.text shouldBe "Very dissatisfied"

    id("thank-you-header").element.text shouldBe "Thank you for your feedback"
    id("thank-you-message").element.text shouldBe "We will use your feedback to make our services better."
    id("continue-button").element.text shouldBe "Send feedback"

    PayApiStubFindJourneyBySessionId.findBySessionIdVerify()
  }

  "should show no errors if the user hasn't clicked submit" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)
    goTo(baseUrl + pagePath)
    webDriver.findElementsById("error-summary-title").isEmpty shouldBe true
    webDriver.findElementsById("wereYouAble-error").isEmpty shouldBe true
    webDriver.findElementsById("howEasy-error").isEmpty shouldBe true
    webDriver.findElementsById("overallRate-error").isEmpty shouldBe true
  }

  "should show an error if the user tries to submit without filling in any fields" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)
    goTo(baseUrl + pagePath)
    click.on("continue-button")
    currentUrl shouldBe (baseUrl + pagePath)
    id("error-summary-title").element.text shouldBe "There's a problem"
    id("wereYouAble-error").element.text shouldBe "Error: This field is required"
    id("howEasy-error").element.text shouldBe "Error: This field is required"
    id("overallRate-error").element.text shouldBe "Error: This field is required"
  }

  "should proceed to the survey thanks page if the user submits after filling in all necessary fields" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)
    goTo(baseUrl + pagePath)
    click.on("wereYouAble")
    click.on("howEasy")
    click.on("overallRate")
    click.on("continue-button")
    currentUrl shouldBe (baseUrl + surveyThanksPath)
  }

  "ensure can switch to Welsh and back" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)
    goTo(baseUrl + pagePath)
    click.on(xpath("/html/body/div/div/nav/ul/li[2]/a/span[2]"))
    pageTitle shouldBe "Sut oedd eich gwasanaeth talu? - Talu eich Hunanasesiad - GOV.UK"
    click.on(xpath("/html/body/div/div/nav/ul/li[1]/a/span[2]"))
    pageTitle shouldBe "How was our payment service? - Pay your Self Assessment - GOV.UK"
  }

}
