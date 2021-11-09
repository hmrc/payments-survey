package paysurvey.journey.ssj

import io.lemonlabs.uri.Url
import paysurvey.journey.{JourneyRepo, JourneyService}
import paysurvey.origin.SurveyOrigin.Itsa
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import stubs.PayApiStubFindJourneyBySessionId
import support.AppSpec
import testdata.paysurvey.TdAll._
import uk.gov.hmrc.http.SessionKeys

class SsjControllerSpec extends AppSpec {

  private val controller = app.injector.instanceOf[SsjController]

  private val journeyService = app.injector.instanceOf[JourneyService]

  "start survey journey should " in {
    //    val tdJourney = TdAll.lifecyclePfSa.afterSucceedWebPayment.credit
    //    val journey = tdJourney.journey
    //    val sessionId = tdJourney.journey.sessionId.value
    //    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)
    //
    val result = controller.start(Itsa)(surveyRequest)
    status(result) shouldBe Status.CREATED

    val responseBody = Json.parse(contentAsString(result)).as[SsjResponse]
    //    responseBody.journeyId shouldBe journeyId
    responseBody.nextUrl.value shouldBe "http://localhost:9966/payments-survey/survey"

    val j = {
      val maybeJ = journeyService.findLatestBySessionId(sessionId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j._id shouldBe responseBody.journeyId
    j.sessionId shouldBe sessionId

    //    contentAsString(result).contains("How was our payment service?") shouldBe true
    //    contentAsString(result).contains(journey.contentOptions.title.englishValue) shouldBe true

    //    PayApiStubFindJourneyBySessionId.findBySessionIdVerify()
  }

}
