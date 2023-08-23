/*
 * Copyright 2023 HM Revenue & Customs
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

package paysurvey.journey

import play.api.libs.functional.syntax.toInvariantFunctorOps
import play.api.libs.json.Format
import play.api.mvc.PathBindable

final case class SurveyJourneyId(value: String)

object SurveyJourneyId {
  implicit val format: Format[SurveyJourneyId] = implicitly[Format[String]].inmap(SurveyJourneyId(_), _.value)
  implicit def surveyJourneyIdPathBinder(implicit stringBinder: PathBindable[String]): PathBindable[SurveyJourneyId] = new PathBindable[SurveyJourneyId] {
    override def bind(key: String, value: String): Either[String, SurveyJourneyId] = {
      for {
        id <- stringBinder.bind(key, value)
      } yield SurveyJourneyId(id)
    }
    override def unbind(key: String, surveyId: SurveyJourneyId): String = {
      surveyId.value
    }
  }
}
