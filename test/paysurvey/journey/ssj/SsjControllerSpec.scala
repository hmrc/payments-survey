package paysurvey.journey.ssj

import paysurvey.journey.JourneyService
import paysurvey.origin.SurveyOrigin.Itsa
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import support.AppSpec
import testdata.paysurvey.TdAll._

class SsjControllerSpec extends AppSpec {

  private val controller = app.injector.instanceOf[SsjController]

  private val journeyService = app.injector.instanceOf[JourneyService]

  "start survey journey should " in {
    val result = controller.start(Itsa)(surveyRequest)
    status(result) shouldBe Status.CREATED

    val responseBody = Json.parse(contentAsString(result)).as[SsjResponse]
    responseBody.nextUrl.value shouldBe "http://localhost:9966/payments-survey/survey"

    val j = {
      val maybeJ = journeyService.findLatestBySessionId(sessionId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j._id shouldBe responseBody.journeyId
    j.sessionId shouldBe sessionId
  }

}