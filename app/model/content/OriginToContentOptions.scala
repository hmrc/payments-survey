package model.content

import paysurvey.origin.SurveyOrigin
import paysurvey.origin.SurveyOrigin.Itsa

object OriginToContentOptions {

  def toContentOptions(origin: SurveyOrigin): Option[ContentOptions] = origin match {
    case Itsa => Option(ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Request a Self Assessment repayment", welshValue = Option("Gwneud cais am ad-daliad Hunanasesiad"))
    ))
    case _ => None
  }

}
