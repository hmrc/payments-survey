/*
 * Copyright 2025 HM Revenue & Customs
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

package langswitch

import langswitch.LanguageSwitchController
import langswitch.Languages.{English, Welsh}
import org.jsoup.Jsoup
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, redirectLocation, status}
import play.mvc.Http.HeaderNames
import support.AppSpec

final class LanguageSwitchControllerSpec extends AppSpec {
  private val controller = app.injector.instanceOf[LanguageSwitchController]

  "switch to language should redirect to referer link" in {
    val fakeRequest = FakeRequest("GET", "/").withHeaders(HeaderNames.REFERER -> "some/referer/link")

    val result = controller.switchToLanguage(English)(fakeRequest)

    redirectLocation(result) shouldBe Some("some/referer/link")

    status(result) shouldBe Status.SEE_OTHER

  }

  "switch to language should redirect to not found link without referer link" in {
    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.switchToLanguage(Welsh)(fakeRequest)

    redirectLocation(result) shouldBe Some("/payments-survey/survey/404")

    status(result) shouldBe Status.SEE_OTHER

  }

  "notFound should lead to not found page" in {
    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.notFound(fakeRequest)

    val doc = Jsoup.parse(contentAsString(result))

    doc.select("h1").text shouldBe "This page can’t be found"
    doc.select("p").text shouldBe "Please check that you have entered the correct web address."
    doc.select("title").text shouldBe "Pay your tax GOV.UK"

    status(result) shouldBe Status.OK
  }

}
