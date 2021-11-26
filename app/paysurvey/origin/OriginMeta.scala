package paysurvey.origin

import config.AppConfig
import model.OriginExt
import paysurvey.origin.SurveyOrigin.{Itsa, PayApi}
import play.api.i18n.Messages

object OriginMeta {

  def auditName(origin: SurveyOrigin): String = origin match {
    case Itsa => "income-tax-repayments"
    case PayApi(origin) => origin.auditName
  }

  def presentationMode(origin: SurveyOrigin): String = origin match {
    case Itsa => "income-tax-repayments"
    case PayApi(origin) => origin.presentationMode
  }

  def returnMsg(origin: SurveyOrigin)(implicit messages: Messages): String = origin match {
    case Itsa =>
      messages("thanks.return-to-govuk")
    case PayApi(o) if o.entryName.contains("Bta") =>
      messages("thanks.return-to-bta")
    case PayApi(o) if o.entryName.contains("Pta") =>
      messages("thanks.return-to-pta")
    case _ =>
      messages("thanks.return-to-govuk")
  }

  def returnUrl(origin: SurveyOrigin)(implicit appConfig: AppConfig): Option[String] = origin match {
    case Itsa => Some(s"${appConfig.frontendBaseUrl}/report-quarterly/income-and-expenses/view")
    case PayApi(o) if o.entryName.contains("Bta") =>
      Some("/business-account")
    case PayApi(o) if o.entryName.contains("Pta") =>
      Some("/personal-account")
    case _ =>
      None
  }

}
