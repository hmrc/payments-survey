package paysurvey.journey.ssj

import model.content.{ContentOptions, OriginToContentOptions}
import paysurvey.audit.AuditOptions
import paysurvey.journey.{JourneyId, SurveyJourney}
import paysurvey.origin.SurveyOrigin
import play.api.libs.json.Json
import uk.gov.hmrc.http.SessionId

import java.time.LocalDateTime

final case class SsjRequest
  (
    audit: AuditOptions
) {

  def toJourney(
      journeyId: JourneyId,
      origin:    SurveyOrigin,
      createdOn: LocalDateTime,
      sessionId: SessionId
  ): SurveyJourney = {
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
