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

package controllers

import play.api.http.Status
import play.api.test.FakeRequest
import support.AppSpec
import play.api.test.Helpers._

final class SurveyControllerSpec extends AppSpec {
  private val controller = app.injector.instanceOf[SurveyController]

  "route should send the user to pay-frontend CATTP if there is no Journey ID in the session" in {
    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.route()(fakeRequest)

    status(result) shouldBe Status.SEE_OTHER
    redirectLocation(result) shouldBe Some("http://localhost:9056/pay")
  }
}
