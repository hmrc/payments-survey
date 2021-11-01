package model.content

import paysurvey.origin.SurveyOrigin
import paysurvey.origin.SurveyOrigin.Itsa

object OriginToContentOptions {

  def toContentOptions(origin: SurveyOrigin): Option[ContentOptions] = origin match {
    case Itsa => Option(ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "ITSA TEXT HERE", welshValue = Option("ITSA TEXT"))
    ))
    case _ => None
  }

}
