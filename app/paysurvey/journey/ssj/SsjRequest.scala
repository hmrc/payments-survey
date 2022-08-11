package paysurvey.journey.ssj

import model.content.ContentOptions
import paysurvey.audit.AuditOptions
import paysurvey.journey.{SurveyJourneyId, SurveyJourney}
import play.api.libs.json.Json
import uk.gov.hmrc.http.SessionId

import java.time.LocalDateTime

final case class SsjRequest
  (
    audit: AuditOptions
)

object SsjRequest {
  implicit val format = Json.format[SsjRequest]
}

final case class SsjJourneyRequest
  (
    origin:         String,
    returnMsg:      String,
    returnHref:     String,
    auditName:      String,
    audit:          AuditOptions,
    contentOptions: ContentOptions
) {
  def toSurveyJourney(
      journeyId:      SurveyJourneyId,
      createdOn:      LocalDateTime,
      contentOptions: ContentOptions
  ): SurveyJourney = {

    SurveyJourney(
      journeyId,
      contentOptions,
      audit,
      origin,
      returnMsg,
      returnHref,
      createdOn
    )
  }

}

object SsjJourneyRequest {
  implicit val format = Json.format[SsjJourneyRequest]
}

