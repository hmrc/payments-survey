package requests

import model.content.ContentOptions
import paysurvey.audit.AuditOptions
import paysurvey.origin.{SurveyOrigin}
import play.api.mvc.{Request, WrappedRequest}

final case class SurveyRequest[A]
  (
    content: ContentOptions,
    audit:   AuditOptions,
    origin:  Option[SurveyOrigin],
    //todo remove Option when we migrate over
    returnMsg:  Option[String] = Some("Skip survey"),
    returnHref: Option[String] = Some("https://www.gov.uk/government/organisations/hm-revenue-customs"),
    request:    Request[A]
) extends WrappedRequest[A](request)

object SurveyRequest {

  def default[A](implicit request: Request[A]): SurveyRequest[A] = {
    SurveyRequest[A](ContentOptions.default, AuditOptions.default, None,
                     Some("Skip survey"), Some("https://www.gov.uk/government/organisations/hm-revenue-customs"), request)
  }

}
