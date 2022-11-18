package paysurvey.journey

import paysurvey.journey.ssj.SsjService
import support.AppSpec
import testdata.paysurvey.TdAll._
import uk.gov.hmrc.http.HeaderCarrier

class JourneyServiceSpec extends AppSpec {

  private val service = app.injector.instanceOf[JourneyService]

  private val ssjService = app.injector.instanceOf[SsjService]

  def startJourney() = {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    ssjService.startJourney(ssjJourneyRequest)
  }

  "return None when no journey for JourneyId" in {
    service.findLatestByJourneyId(journeyId).futureValue shouldBe None
  }

  "return journey for JourneyId if there is a journey" in {
    val ssjResponse = startJourney().futureValue

    val j = {
      val maybeJ = service.findLatestByJourneyId(ssjResponse.journeyId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j.journeyId shouldBe ssjResponse.journeyId
  }

  "return latest journey for sessionId if there is multiple journeys" in {
    val ssjResponse = startJourney().futureValue
    val ssjResponse2 = startJourney().futureValue

    val j = {
      val maybeJ = service.findLatestByJourneyId(ssjResponse2.journeyId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j.journeyId shouldBe ssjResponse2.journeyId
  }

}
