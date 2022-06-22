package paysurvey.origin

import config.AppConfig
import model.OriginExt
import paysurvey.origin.SurveyOrigin.{Itsa, PayApi}
import play.api.i18n.Messages

object OriginMeta {
//Move this to  request
  def auditName(origin: SurveyOrigin): String = origin match {
    case Itsa           => "income-tax-repayments"
    case PayApi(origin) => origin.auditName
  }
 //todo see what this is used for
  def presentationMode(origin: SurveyOrigin): String = origin match {
    case Itsa           => "income-tax-repayments"
    case PayApi(origin) => origin.presentationMode
  }

  def returnMsg(origin: Option[SurveyOrigin])(implicit msg: Messages): String = origin match {
    case Some(Itsa) =>
      msg("thanks.return-to-govuk")
    case Some(PayApi(o)) if o.entryName.contains("Bta") =>
      msg("thanks.return-to-bta")
    case Some(PayApi(o)) if o.entryName.contains("Pta") =>
      msg("thanks.return-to-pta")
    case _ =>
      msg("thanks.return-to-govuk")
  }

  def returnHref(origin: SurveyOrigin)(implicit appConfig: AppConfig): String = origin match {
    case Itsa =>
      s"${appConfig.frontendBaseUrl}/report-quarterly/income-and-expenses/view"
    case PayApi(o) if o.entryName.contains("Bta") =>
      "/business-account"
    case PayApi(o) if o.entryName.contains("Pta") =>
      "/personal-account"
    case _ =>
      "https://www.gov.uk"

  }

}
