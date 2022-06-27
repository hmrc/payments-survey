package testdata.paysurvey

import model.content.ContentOptions
import paysurvey.audit.AuditOptions
import paysurvey.journey.ssj.{SsjJourneyRequest, SsjRequest}
import paysurvey.origin.SurveyOrigin.Itsa
import play.api.mvc.Request
import play.api.test.FakeRequest
import requests.SurveyRequest
import uk.gov.hmrc.http.SessionKeys

trait TdSsjRequest extends TdBase {

  implicit val r = FakeRequest("GET", "/")
    .withSession(SessionKeys.sessionId -> sessionId.value)

  val auditOptions = AuditOptions(
    userType = "IsLoggedIn"
  )

  val ssjRequest: SsjRequest = SsjRequest(
    auditOptions
  )
  val ssjJourneyRequest: SsjJourneyRequest = SsjJourneyRequest(
    "lala",
    "returnMsg",
    "returnHref",
    "auditname",
    auditOptions,
    ContentOptions.default
  )

  val surveyRequest: SurveyRequest[SsjRequest] = SurveyRequest(
    ContentOptions.default,
    AuditOptions.default,
    Option(Itsa),
    r
      .withBody(ssjRequest)
  )

}
