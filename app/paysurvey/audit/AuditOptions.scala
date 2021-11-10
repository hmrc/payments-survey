package paysurvey.audit

import cats.implicits.catsSyntaxOptionId
import model.OriginExt
import payapi.cardpaymentjourney.model.journey.{Journey, JourneySpecificData}
import play.api.libs.json.Json
import play.api.mvc.Request
import requests.RequestSupport

final case class AuditOptions
  (
    userType:  String,
    journey:   Option[String] = None,
    orderId:   Option[String] = None,
    liability: Option[String] = None
) {
  lazy val toMap = {
    Map(
      "userType" -> userType,
      "journey" -> journey.getOrElse("Unknown"),
      "orderId" -> orderId.getOrElse("Unknown"),
      "liability" -> liability.getOrElse("Unknown"),
    )
  }

}

object AuditOptions {
  implicit val format = Json.format[AuditOptions]

  def default(implicit r: Request[_]) = AuditOptions(
    userType = if (RequestSupport.isLoggedIn) "LoggedIn" else "LoggedOut"
  )

  def fromPayApi(journey: Journey[JourneySpecificData])(implicit r: Request[_]): AuditOptions = {
    AuditOptions(
      userType  = if (RequestSupport.isLoggedIn) "LoggedIn" else "LoggedOut",
      journey   = Option(journey.status.entryName),
      orderId   = journey.reference.map(_.value),
      liability = journey.origin.auditName.some
    )
  }

}

