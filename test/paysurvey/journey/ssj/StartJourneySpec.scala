package paysurvey.journey.ssj

import support.AppSpec
import testdata.paysurvey.TdAll.ssjJourneyRequest
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.http.HttpReads.Implicits._

class StartJourneySpec extends AppSpec {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  "start survey journey should " in {
    val ssjUrl = s"http://localhost:${port}/payments-survey/journey/start"

    val response: HttpResponse = testHttpClient.POST[SsjJourneyRequest, HttpResponse](ssjUrl, ssjJourneyRequest).futureValue
    val ssjResponse = response.json.as[SsjResponse]

    ssjResponse.nextUrl.value shouldBe s"http://localhost:9966/payments-survey/v2/survey/${ssjResponse.journeyId.value}"
  }

}
