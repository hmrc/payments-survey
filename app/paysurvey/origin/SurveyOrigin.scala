package paysurvey.origin

import enumeratum._
import payapi.corcommon.model.Origin
import play.api.libs.json.Format
import play.api.mvc.PathBindable
import util.enumformat.EnumFormat

import scala.collection.immutable

sealed trait SurveyOrigin extends EnumEntry

object SurveyOrigin extends Enum[SurveyOrigin] {

  case object Itsa extends SurveyOrigin
  final case class PayApi(origin: Origin) extends SurveyOrigin

  override def values: immutable.IndexedSeq[SurveyOrigin] = findValues

  implicit val format: Format[SurveyOrigin] = EnumFormat(SurveyOrigin)

  implicit val originBinder: PathBindable[SurveyOrigin] = {
      def fromString(string: String) = SurveyOrigin.withNameInsensitiveOption(string)
        .getOrElse(throw new Exception(s"originBinder: $string is not a valid origin name"))

    PathBindable.bindableString.transform[SurveyOrigin](fromString, _.entryName.toLowerCase)
  }
}
