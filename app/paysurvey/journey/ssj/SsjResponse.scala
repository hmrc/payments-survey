package paysurvey.journey.ssj

import payapi.cardpaymentjourney.model.journey.Url
import paysurvey.journey.JourneyId
import play.api.libs.json.{Format, Json}

final case class SsjResponse
  (
    journeyId: JourneyId,
    nextUrl:   Url
)

object SsjResponse {
  implicit val format: Format[SsjResponse] = Json.format
}
