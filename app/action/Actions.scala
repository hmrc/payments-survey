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

package action

import com.google.inject.Inject
import paysurvey.journey.SurveyJourneyId
import play.api.mvc.{ActionBuilder, AnyContent, DefaultActionBuilder}
import requests.{MaybeJourneyRequest, SurveyRequest}

final class Actions @Inject() (
    getJourneyActionFactory: GetJourneyActionFactory,
    actionBuilder:           DefaultActionBuilder
) {
  import getJourneyActionFactory._

  def maybeSurveyAction: ActionBuilder[SurveyRequest, AnyContent] =
    actionBuilder andThen maybeSurveyActionRefiner

  def maybeSurveyJourneyAction(id: SurveyJourneyId): ActionBuilder[SurveyRequest, AnyContent] =
    actionBuilder andThen maybeSurveyJourneyActionRefiner(id)

}
