package paysurvey.journey.ssj

import payapi.cardpaymentjourney.model.journey.Url
import paysurvey.journey.JourneyId
import play.api.libs.json.{Json, Writes}

final case class SsjResponse
  (
    journeyId: JourneyId,
    nextUrl:   Url
)

object SsjResponse {
  implicit val writes: Writes[SsjResponse] = Json.writes
}
