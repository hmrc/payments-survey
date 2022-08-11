package paysurvey.journey.ssj

import paysurvey.journey.{JourneyService, SurveyJourney}
import play.api.libs.json.Json
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import support.AppSpec
import testdata.paysurvey.TdAll._

class SsjControllerSpec extends AppSpec {

  private val controller = app.injector.instanceOf[SsjController]

  private val journeyService = app.injector.instanceOf[JourneyService]

  "start survey journey v2 should " in {
    val result = controller.startJourney()(r.withBody[SsjJourneyRequest](ssjJourneyRequest))

    val responseBody = Json.parse(contentAsString(result)).as[SsjResponse]
    responseBody.nextUrl.value shouldBe s"http://localhost:9966/payments-survey/v2/survey/${responseBody.journeyId.value}"

    val j: SurveyJourney = {
      val maybeJ = journeyService.findLatestByJourneyId(responseBody.journeyId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j.journeyId shouldBe responseBody.journeyId
  }

}
