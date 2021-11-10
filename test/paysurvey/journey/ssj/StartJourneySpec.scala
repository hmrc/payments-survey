package paysurvey.journey.ssj

import paysurvey.journey.SessionId.toLoggingSessionId
import support.AppSpec
import testdata.paysurvey.TdAll.{sessionId, ssjRequest}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.http.HttpReads.Implicits._

class StartJourneySpec extends AppSpec {

  implicit val hc: HeaderCarrier = HeaderCarrier(sessionId = Option(toLoggingSessionId(sessionId)))

  "start survey journey should " in {
    val ssjUrl = s"http://localhost:${port}/payments-survey/itsa/journey/start"

    val response: HttpResponse = testHttpClient.POST[SsjRequest, HttpResponse](ssjUrl, ssjRequest).futureValue
    val ssjResponse = response.json.as[SsjResponse]

    ssjResponse.nextUrl.value shouldBe "http://localhost:9966/payments-survey/survey"
  }

}
