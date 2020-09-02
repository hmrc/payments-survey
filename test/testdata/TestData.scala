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

package testdata

import java.time.{Clock, LocalDateTime}

import payapi.cardpaymentjourney.model.journey.{Journey, JsdPfSa, NavigationOptions, SessionId, Url}
import payapi.corcommon.model.{JourneyId, PaymentStatuses}

object TestData {
  //  val testJourneyId: JourneyId = JourneyId("123456")
  //  val testSessionId: SessionId = SessionId("TestSession-4b87460d-6f43-4c4c-b810-d6f87c774854")

  //  def testJourney(implicit clock: Clock): Journey[JsdPfSa] = Journey(
  //    _id = testJourneyId,
  //    sessionId = Some(testSessionId),
  //    amountInPence = None,
  //    emailTemplateOptions = None,
  //    navigation = NavigationOptions(Url("http://localhost:9056/pay"), Url("http://localhost:9056/pay"), Url("http://localhost:9056/pay"), None),
  //    order = None,
  //    status = PaymentStatuses.Created,
  //    createdOn = LocalDateTime.now(clock),
  //    journeySpecificData = JsdPfSa(utr = None))
}
