package paysurvey.journey

import model.content.{ContentOptions, JsdToContentOptions}
import payapi.cardpaymentjourney.model.journey.{Journey, JourneySpecificData}
import paysurvey.audit.AuditOptions
import paysurvey.origin.SurveyOrigin
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Request

import java.time.LocalDateTime

final case class SurveyJourney
  (
    _id:       JourneyId,
    sessionId: SessionId,
    origin:    SurveyOrigin,
    content:   ContentOptions,
    audit:     AuditOptions,
    //  meta: JourneyMeta,
    createdOn: LocalDateTime
)

object SurveyJourney {
  implicit val format: OFormat[SurveyJourney] = Json.format[SurveyJourney]

  def fromPayApi(
      createdOn: LocalDateTime,
      sessionId: SessionId
  )(
      journey: Journey[JourneySpecificData]
  )(implicit request: Request[_]): SurveyJourney = {
    val opts = JsdToContentOptions.toContentOptions(journey.journeySpecificData)

    val audit = AuditOptions.fromPayApi(journey)

    SurveyJourney(
      JourneyId(journey._id.value),
      sessionId,
      SurveyOrigin.PayApi(journey.origin),
      //      req,
      opts,
      audit,
      createdOn
    )
  }

}
