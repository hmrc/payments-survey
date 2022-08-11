package requests

import model.content.ContentOptions
import paysurvey.audit.AuditOptions
import play.api.mvc.{Request, WrappedRequest}

final case class SurveyRequest[A]
  (
    content:    ContentOptions,
    audit:      AuditOptions,
    origin:     String,
    returnMsg:  String,
    returnHref: String,
    request:    Request[A]
) extends WrappedRequest[A](request)

object SurveyRequest {

  def default[A](implicit request: Request[A]): SurveyRequest[A] = {
    SurveyRequest[A](ContentOptions.default, AuditOptions.default, "Pay-frontend",
      "Skip survey", "https://www.gov.uk/government/organisations/hm-revenue-customs", request)
  }

}
