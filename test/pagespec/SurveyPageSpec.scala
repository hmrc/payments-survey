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

    webDriver.getTitle shouldBe "How was our payment service? - Pay your Self Assessment - GOV.UK"

    find("were-you-able-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "1. Were you able to do what you needed to do today?")
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(3) > div > fieldset > div > div:nth-child(1) > label"))
      .getText shouldBe "Yes"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(3) > div > fieldset > div > div:nth-child(2) > label"))
      .getText shouldBe "No"

    find("how-easy-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "2. How easy was it for you to do what you needed to do today?")
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(1) > label"))
      .getText shouldBe "Very easy"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(2) > label"))
      .getText shouldBe "Easy"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(3) > label"))
      .getText shouldBe "Neither easy or difficult"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(4) > label"))
      .getText shouldBe "Difficult"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(4) > div > fieldset > div > div:nth-child(5) > label"))
      .getText shouldBe "Very difficult"

    find("why-score-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "2b. Why did you give this score?")
    webDriver.findElementById("comments").getAttribute("value") shouldBe ""

    find("overall-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "3. Overall, how did you feel about the service you received today?")
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(1) > label"))
      .getText shouldBe "Very satisfied"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(2) > label"))
      .getText shouldBe "Satisfied"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(3) > label"))
      .getText shouldBe "Neither satisfied or dissatisfied"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(4) > label"))
      .getText shouldBe "Dissatisfied"
    webDriver.findElement(By.cssSelector("#main-content > div > div > div.sign-out-survey > form > div:nth-child(6) > div > fieldset > div > div:nth-child(5) > label"))
      .getText shouldBe "Very dissatisfied"

    webDriver.findElementById("thank-you-header").getText shouldBe "Thank you for your feedback"
    find("thank-you-message")
      .fold(fail)(elem => elem.underlying.getText shouldBe "We will use your feedback to make our services better.")

    webDriver.findElementById("continue-button").getText shouldBe "Send feedback"

    PayApiStubFindJourneyBySessionId.findBySessionIdVerify(tdJourney)
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

    webDriver.findElementById("error-summary-title").getText shouldBe "There's a problem"
    webDriver.findElementById("wereYouAble-error").getText shouldBe "Error: This field is required"
    webDriver.findElementById("howEasy-error").getText shouldBe "Error: This field is required"
    webDriver.findElementById("overallRate-error").getText shouldBe "Error: This field is required"
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

  "ensure goes to timeout page when session not found" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId404(tdJourney)
    goTo(baseUrl + pagePath)
    pageTitle shouldBe "For your security, we timed you out"
  }

  "ensure can switch to Welsh and back" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)
    goTo(baseUrl + pagePath)
    eventually { webDriver.findElement(By.xpath("/html/body/div/div/nav/ul/li[2]/a/span[2]")).click() }
    pageTitle shouldBe "Sut oedd eich gwasanaeth talu? - Talu eich Hunanasesiad - GOV.UK"
    eventually { webDriver.findElement(By.xpath("/html/body/div/div/nav/ul/li[1]/a/span[2]")).click() }
    pageTitle shouldBe "How was our payment service? - Pay your Self Assessment - GOV.UK"
  }

}
