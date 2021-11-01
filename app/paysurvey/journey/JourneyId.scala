package paysurvey.journey

import play.api.libs.functional.syntax.toInvariantFunctorOps
import play.api.libs.json.Format

final case class JourneyId(value: String)

object JourneyId {
  implicit val format: Format[JourneyId] = implicitly[Format[String]].inmap(JourneyId(_), _.value)
  //  implicit val journeyIdBinder: PathBindable[JourneyId] = valueClassBinder(_.value)
}
