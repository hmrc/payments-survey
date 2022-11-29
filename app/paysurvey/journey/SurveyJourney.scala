package paysurvey.journey

import model.content.ContentOptions
import payapi.cardpaymentjourney.model.journey.{Journey, JourneySpecificData}
import paysurvey.audit.AuditOptions

import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Request

import java.time.LocalDateTime

final case class SurveyJourney
  (
    journeyId:  SurveyJourneyId,
    content:    ContentOptions,
    audit:      AuditOptions,
    origin:     String,
    returnMsg:  String,
    returnHref: String,
    createdOn:  LocalDateTime
)

object SurveyJourney {
  implicit val format: OFormat[SurveyJourney] = Json.format[SurveyJourney]
}
