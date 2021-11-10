package paysurvey.journey

import cats.implicits.catsSyntaxOptionId
import paysurvey.journey.ssj.SsjService
import paysurvey.origin.SurveyOrigin.Itsa
import support.AppSpec
import testdata.paysurvey.TdAll._
import uk.gov.hmrc.http.HeaderCarrier

class JourneyServiceSpec extends AppSpec {

  private val service = app.injector.instanceOf[JourneyService]

  private val ssjService = app.injector.instanceOf[SsjService]

  def startJourney(sessionId: SessionId) = {
    implicit val hc: HeaderCarrier = HeaderCarrier(sessionId = sessionId.some)

    ssjService.start(Itsa, ssjRequest)
  }

  "return None when no journey for sessionId" in {
    service.findLatestBySessionId(sessionId).futureValue shouldBe None
  }

  "return journey for sessionId if there is a journey" in {
    val ssjResponse = startJourney(sessionId).futureValue

    val j = {
      val maybeJ = service.findLatestBySessionId(sessionId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j._id shouldBe ssjResponse.journeyId
    j.sessionId shouldBe sessionId
  }

  "return latest journey for sessionId if there is multiple journeys" in {
    val ssjResponse = startJourney(sessionId).futureValue
    val ssjResponse2 = startJourney(sessionId).futureValue

    val j = {
      val maybeJ = service.findLatestBySessionId(sessionId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j._id shouldBe ssjResponse2.journeyId
    j.sessionId shouldBe sessionId
  }

}
