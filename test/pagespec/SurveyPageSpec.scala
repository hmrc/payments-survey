/*
 * Copyright 2020 HM Revenue & Customs
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
import payapi.cardpaymentjourney.model.journey.Journey
import payapi.corcommon.model.JourneyId
import stubs.{ PayApiStubFindJourneyBySessionId, PayApiStubGetJourney }
import support.PageSpec
import testdata.TestData._

class SurveyPageSpec extends PageSpec {
  val pagePath = s"/payments-survey/survey"
  val surveyThanksPath = s"/payments-survey/survey-thanks"

  "should render correctly" in new TestWithSession {

    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)

    goTo(baseUrl + pagePath)

    cssSelector(".header__menu__proposition-name")
      .element.text shouldEqual journey.contentOptions.title.englishValue

    webDriver.getTitle shouldBe "How was our payment service? - Pay your Self Assessment - GOV.UK"

    find("were-you-able-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "1. Were you able to do what you needed to do today?")
    find("were-you-able-yes-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Yes")
    find("were-you-able-no-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "No")

    find("how-easy-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "2. How easy was it for you to do what you needed to do today?")
    find("how-easy-very-easy-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Very easy")
    find("how-easy-easy-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Easy")
    find("how-easy-neutral-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Neither easy or difficult")
    find("how-easy-difficult-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Difficult")
    find("how-easy-very-difficult-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Very difficult")

    find("why-score-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "2b. Why did you give this score?")
    textArea("why-score-field").value shouldBe ""

    find("overall-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "3. Overall, how did you feel about the service you received today?")
    find("overall-very-good-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Very satisfied")
    find("overall-good-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Satisfied")
    find("overall-neutral-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Neither satisfied or dissatisfied")
    find("overall-poor-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Dissatisfied")
    find("overall-very-poor-label")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Very dissatisfied")

    find("thank-you-header")
      .fold(fail)(elem => elem.underlying.getText shouldBe "Thank you for your feedback")
    find("thank-you-message")
      .fold(fail)(elem => elem.underlying.getText shouldBe "We will use your feedback to make our services better.")

    find("submit-survey-button")
      .fold(fail)(elem => elem.underlying.getAttribute("value") shouldBe "Send feedback")

    PayApiStubFindJourneyBySessionId.findBySessionIdVerify(tdJourney)
  }

  "should show no errors if the user hasn't clicked submit" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)

    goTo(baseUrl + pagePath)

    find("were-you-able-error")
      .fold(fail)(elem => elem.underlying.getText shouldBe "")

    find("how-easy-error")
      .fold(fail)(elem => elem.underlying.getText shouldBe "")

    find("overall-error")
      .fold(fail)(elem => elem.underlying.getText shouldBe "")
  }

  "should show an error if the user tries to submit without filling in any fields" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)

    goTo(baseUrl + pagePath)

    click.on("submit-survey-button")

    currentUrl shouldBe (baseUrl + pagePath)

    find("were-you-able-error")
      .fold(fail)(elem => elem.underlying.getText shouldBe "This field is required")

    find("how-easy-error")
      .fold(fail)(elem => elem.underlying.getText shouldBe "This field is required")

    find("overall-error")
      .fold(fail)(elem => elem.underlying.getText shouldBe "This field is required")
  }

  "should proceed to the survey thanks page if the user submits after filling in all necessary fields" in new TestWithSession {
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)

    goTo(baseUrl + pagePath)

    click.on("able-yes")
    click.on("difficulty-radio-very-easy")
    click.on("rate-very-poor")
    click.on("submit-survey-button")

    currentUrl shouldBe (baseUrl + surveyThanksPath)
  }
}
