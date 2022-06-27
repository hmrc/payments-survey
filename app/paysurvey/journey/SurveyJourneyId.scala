package paysurvey.journey

import payapi.corcommon.internal.ValueClassBinder.valueClassBinder
import play.api.libs.functional.syntax.toInvariantFunctorOps
import play.api.libs.json.Format
import play.api.mvc.PathBindable

final case class SurveyJourneyId(value: String)

object SurveyJourneyId {
  implicit val format: Format[SurveyJourneyId] = implicitly[Format[String]].inmap(SurveyJourneyId(_), _.value)
  implicit val surveyJourneyIdPathBinder: PathBindable[SurveyJourneyId] = valueClassBinder(_.toString)

}
