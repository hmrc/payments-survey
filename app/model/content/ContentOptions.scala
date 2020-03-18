/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package model.content

import model.langswitch.{ Language, Languages }
import play.api.i18n.Messages
import play.api.libs.functional.syntax._
import play.api.libs.json.{ Format, Json, OFormat }

final case class ContentOptions(
  isWelshSupported: IsWelshSupported,
  title: BannerTitle)

object ContentOptions {
  implicit val format: OFormat[ContentOptions] = Json.format[ContentOptions]
}

final case class IsWelshSupported(value: Boolean)

object IsWelshSupported {
  val yes: IsWelshSupported = IsWelshSupported(true)
  val no: IsWelshSupported = IsWelshSupported(false)
  val default: IsWelshSupported = IsWelshSupported.yes

  implicit val format: Format[IsWelshSupported] = implicitly[Format[Boolean]].inmap(IsWelshSupported(_), _.value)
}

final case class BannerTitle(englishValue: String, welshValue: Option[String] = None) {
  def forCurrentLanguage(implicit messages: Messages): Option[String] = Language() match {
    case Languages.English => Some(englishValue)
    case Languages.Welsh => welshValue
    case _ => None
  }
}

object BannerTitle {
  implicit val format: OFormat[BannerTitle] = Json.format[BannerTitle]
  val default: BannerTitle = BannerTitle(englishValue = "Pay tax", welshValue = Option("Talu treth"))
}
