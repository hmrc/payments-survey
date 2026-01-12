/*
 * Copyright 2025 HM Revenue & Customs
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

package langswitch

import langswitch.Languages.{English, Welsh}
import play.api.i18n.Lang
import support.AppSpec

class LanguageSpec extends AppSpec {

  "Should give correct language for" - {
    "english" in {
      Language.apply(Lang("en")) shouldBe English
    }

    "default -> english" in {
      Language.apply(Lang("blah")) shouldBe English
    }

    "welsh" in {
      Language.apply(Lang("cy")) shouldBe Welsh
    }
  }

  "Should have correct play language for" - {
    "english" in {
      English.toPlayLang.code shouldBe "en"
    }

    "welsh" in {
      Welsh.toPlayLang.code shouldBe "cy"
    }
  }

  "Should have correct labels for" - {
    "english" in {
      English.label shouldBe "English"
    }

    "welsh" in {
      Welsh.label shouldBe "Cymraeg"
    }
  }
}
