package requests

import config.AppConfig
import model.content.ContentOptions
import paysurvey.audit.AuditOptions
import paysurvey.origin.{OriginMeta, SurveyOrigin}
import play.api.i18n.Messages
import play.api.mvc.{Request, WrappedRequest}

final case class SurveyRequest[A]
  (
    content: ContentOptions,
    audit:   AuditOptions,
    origin:  Option[SurveyOrigin],
    request: Request[A]
) extends WrappedRequest[A](request) {

  def returnMsg(implicit msg: Messages): String = {
    OriginMeta.returnMsg(origin)
  }

  def returnHref(implicit appConfig: AppConfig): Option[String] = {
    origin.map(OriginMeta.returnHref)
  }

}

object SurveyRequest {

  def default[A](implicit request: Request[A]): SurveyRequest[A] = {
    SurveyRequest[A](ContentOptions.default, AuditOptions.default, None, request)
  }

}
