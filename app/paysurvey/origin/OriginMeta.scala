package paysurvey.origin

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

}
