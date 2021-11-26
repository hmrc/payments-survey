package paysurvey.origin

import config.AppConfig
import model.OriginExt
import paysurvey.origin.SurveyOrigin.{Itsa, PayApi}

object OriginMeta {

  def auditName(origin: SurveyOrigin): String = origin match {
    case Itsa           => "income-tax-repayments"
    case PayApi(origin) => origin.auditName
  }

  def presentationMode(origin: SurveyOrigin): String = origin match {
    case Itsa           => "income-tax-repayments"
    case PayApi(origin) => origin.presentationMode
  }

  def backUrl(origin: SurveyOrigin)(implicit appConfig: AppConfig): Option[String] = origin match {
    case Itsa      => Some(s"${appConfig.frontendBaseUrl}/report-quarterly/income-and-expenses/view")
    case PayApi(_) => None
  }

}
