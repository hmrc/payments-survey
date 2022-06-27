package paysurvey.journey.ssj

import model.content.{ContentOptions, OriginToContentOptions}
import paysurvey.audit.AuditOptions
import paysurvey.journey.{SurveyJourneyId, SurveyJourney}
import paysurvey.origin.SurveyOrigin
import play.api.libs.json.Json
import uk.gov.hmrc.http.SessionId

import java.time.LocalDateTime

final case class SsjRequest
  (
    audit: AuditOptions
) {

  def toJourney(
      journeyId: SurveyJourneyId,
      origin:    SurveyOrigin,
      createdOn: LocalDateTime,
      sessionId: SessionId
  ): SurveyJourney = {
    //todo this is for one tax regime
    val content: ContentOptions = OriginToContentOptions.toContentOptions(origin).getOrElse {
      throw new Exception(s"Couldn't resolve content for $origin")
    }

    SurveyJourney(
      journeyId,
      sessionId,
      origin,
      content,
      audit,
      createdOn
    )
  }
}

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
      origin:         SurveyOrigin,
      createdOn:      LocalDateTime,
      contentOptions: ContentOptions,
      sessionId:      SessionId
  ): SurveyJourney = {

    SurveyJourney(
      journeyId,
      sessionId,
      origin,
      contentOptions,
      audit,
      createdOn
    )
  }

}

object SsjJourneyRequest {
  implicit val format = Json.format[SsjJourneyRequest]
}

