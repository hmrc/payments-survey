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

import model.content.{ ContentOptions, JsdToContentOptions }
import payapi.cardpaymentjourney.model.journey._
import payapi.corcommon.model.Origins._
import payapi.corcommon.model._

package object model {

  implicit class JourneyExt[Jsd <: JourneySpecificData](val j: Journey[Jsd]) {
    def contentOptions: ContentOptions = j.journeySpecificData.contentOptions
  }

  implicit class OriginExt(val o: Origin) extends AnyVal {
    def authExpected: Boolean = o match {
      case PfSa => false
      case PfVat => false
      case PfCt => false
      case PfEpayeNi => false
      case PfEpayeLpp => false
      case PfEpayeSeta => false
      case PfEpayeLateCis => false
      case PfEpayeP11d => false
      case PfTpes => false
      case PfClass2Ni => false
      case PfClass3Ni => false
      case PfInsurancePremium => false
      case PfSdlt => false
      case PfCds => false
      case PfMgd => false
      case PfSimpleAssessment => false
      case PfPsAdmin => false
      case PfSdil => false
      case PfOther => false
      case BcPngr => false
      case Mib => false
      case PfBioFuels => false
      case PfAirPass => false
      case PfBeerDuty => false
      case PfGamingOrBingoDuty => false
      case PfGbPbRgDuty => false
      case PfLandfillTax => false
      case PfAggregatesLevy => false
      case PfClimateChangeLevy => false
      case CapitalGainsTax => false

      case PfP800 => true
      case BtaSa => true
      case BtaVat => true
      case BtaEpayeBill => true
      case BtaEpayePenalty => true
      case BtaEpayeInterest => true
      case BtaEpayeGeneral => true
      case BtaClass1aNi => true
      case BtaCt => true
      case BtaSdil => true
      case DdVat => true
      case VcVatReturn => true
      case VcVatOther => true
      case ItSa => true
      case Amls => true
      case Parcels => true
      case Ni => true
      case PtaSa => true
    }
  }

  implicit class JsdExt(val jsd: JourneySpecificData) extends AnyVal {
    def contentOptions: ContentOptions = JsdToContentOptions.toContentOptions(jsd)
  }
}

