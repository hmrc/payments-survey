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

package stubs

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import payapi.cardpaymentjourney.model.journey.{ Journey, JourneySpecificData }
import play.api.http.Status
import stubs.WiremockStub._
import testdata.TdJourney

object PayApiStubFindJourneyBySessionId {

  private val path = s"/pay-api/journey/find-latest-by-session-id"

  def findBySessionId2xx[Jsd <: JourneySpecificData](tdJourney: TdJourney[Jsd]): StubMapping = stubFor(
    get(urlPathEqualTo(path))
      .willReturn(
        aResponse()
          .withStatus(Status.OK)
          .withBody(tdJourney.jsonPrettyPrint)))

  def findBySessionId404[Jsd <: JourneySpecificData](tdJourney: TdJourney[Jsd], wireMockState: Int = 0): StubMapping = stubFor(
    get(urlPathEqualTo(path))
      .inScenario("pay-api-getJourney")
      .whenScenarioStateIs(state(wireMockState))
      .willReturn(
        aResponse()
          .withStatus(Status.NOT_FOUND)
          .withBody(s"""{"statusCode":${Status.NOT_FOUND},"message":"Journey not found [${tdJourney.journey._id}]"}"""))
      .willSetStateTo(nextState(wireMockState)))

  def findBySessionId5xx[Jsd <: JourneySpecificData](tdJourney: TdJourney[Jsd], wireMockState: Int): StubMapping = stubFor(
    get(urlPathEqualTo(path))
      .inScenario("pay-api-getJourney")
      .whenScenarioStateIs(state(wireMockState))
      .willReturn(
        aResponse()
          .withStatus(Status.SERVICE_UNAVAILABLE)
          .withBody(s"""{"statusCode":${Status.SERVICE_UNAVAILABLE},"message":"some error at pay-api"}"""))
      .willSetStateTo(nextState(wireMockState)))

  def findBySessionId5xx[Jsd <: JourneySpecificData](tdJourney: TdJourney[Jsd]): StubMapping = stubFor(
    get(urlPathEqualTo(path))
      .willReturn(
        aResponse()
          .withStatus(Status.SERVICE_UNAVAILABLE)
          .withBody(s"""{"statusCode":${Status.SERVICE_UNAVAILABLE},"message":"some error at pay-api"}""")))

  def findBySessionIdVerify[Jsd <: JourneySpecificData](tdJourney: TdJourney[Jsd], times: Int = 1): Unit = verify(
    times,
    getRequestedFor(urlEqualTo(path)))
}

