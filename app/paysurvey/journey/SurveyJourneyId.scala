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
        id <- stringBinder.bind(key, value).right
      } yield SurveyJourneyId(id)
    }
    override def unbind(key: String, surveyId: SurveyJourneyId): String = {
      surveyId.value
    }
  }
}
