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
import payapi.corcommon.model.JourneyId
import play.api.http.Status
import WiremockStub._
import play.api.libs.json.Json

object PayApiStubGetJourney {

  private def path(journeyId: JourneyId): String = {
    s"/pay-api/journey/${journeyId.value}"
  }

  def getJourney2xx(journey: Journey[JourneySpecificData], sequence: Int): StubMapping = stubFor(
    get(urlPathEqualTo(path(journey._id)))
      .inScenario("pay-api-getJourney")
      .whenScenarioStateIs(state(sequence))
      .willReturn(
        aResponse()
          .withStatus(Status.OK)
          .withBody(Json.prettyPrint(Journey.formats.writes(journey))))
      .willSetStateTo(nextState(sequence)))

  def getJourney2xx(journey: Journey[JourneySpecificData]): StubMapping = stubFor(
    get(urlPathEqualTo(path(journey._id)))
      .willReturn(
        aResponse()
          .withStatus(Status.OK)
          .withBody(Json.prettyPrint(Journey.formats.writes(journey)))))

  def getJourney404(journey: Journey[JourneySpecificData], wireMockState: Int = 0): StubMapping = stubFor(
    get(urlPathEqualTo(path(journey._id)))
      .inScenario("pay-api-getJourney")
      .whenScenarioStateIs(state(wireMockState))
      .willReturn(
        aResponse()
          .withStatus(Status.NOT_FOUND)
          .withBody(s"""{"statusCode":${Status.NOT_FOUND},"message":"Journey not found [${journey._id}]"}"""))
      .willSetStateTo(nextState(wireMockState)))

  def getJourney5xx(journey: Journey[JourneySpecificData], wireMockState: Int): StubMapping = stubFor(
    get(urlPathEqualTo(path(journey._id)))
      .inScenario("pay-api-getJourney")
      .whenScenarioStateIs(state(wireMockState))
      .willReturn(
        aResponse()
          .withStatus(Status.SERVICE_UNAVAILABLE)
          .withBody(s"""{"statusCode":${Status.SERVICE_UNAVAILABLE},"message":"some error at pay-api"}"""))
      .willSetStateTo(nextState(wireMockState)))

  def getJourney5xx(journey: Journey[JourneySpecificData]): StubMapping = stubFor(
    get(urlPathEqualTo(path(journey._id)))
      .willReturn(
        aResponse()
          .withStatus(Status.SERVICE_UNAVAILABLE)
          .withBody(s"""{"statusCode":${Status.SERVICE_UNAVAILABLE},"message":"some error at pay-api"}""")))

  def getJourneyVerify(journey: Journey[JourneySpecificData], times: Int = 1): Unit = verify(
    times,
    getRequestedFor(urlEqualTo(path(journey._id))))

}
