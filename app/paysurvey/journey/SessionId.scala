package paysurvey.journey

import play.api.libs.functional.syntax.toInvariantFunctorOps
import play.api.libs.json.Format
import uk.gov.hmrc.http

final case class SessionId(value: String) extends AnyVal

object SessionId {
  //bridge between two domain worlds
  implicit def fromLoggingSessionId(sid: http.SessionId): SessionId = SessionId(sid.value)
  implicit def fromLoggingSessionId(sid: Option[http.SessionId]): Option[SessionId] = sid.map(x => x: SessionId)
  implicit def toLoggingSessionId(sid: SessionId): http.SessionId = http.SessionId(sid.value)
  implicit def toLoggingSessionId(sid: Option[SessionId]): Option[http.SessionId] = sid.map(x => http.SessionId(x.value))
  implicit val format: Format[SessionId] = implicitly[Format[String]].inmap(SessionId(_), _.value)
}
