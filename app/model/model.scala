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

import model.content.{ContentOptions, JsdToContentOptions}
import payapi.cardpaymentjourney.model.journey._
import payapi.corcommon.model.Origins._
import payapi.corcommon.model._

package object model {

  implicit class JourneyExt[Jsd <: JourneySpecificData](val j: Journey[Jsd]) {
    def contentOptions: ContentOptions = JsdToContentOptions.toContentOptions(j.journeySpecificData)
  }

  implicit class OriginExt(val o: Origin) extends AnyVal {

    def auditName: String = o match {
      case PfSa | BtaSa | PtaSa | ItSa => "self-assessment"
      case PfVat | BtaVat | DdVat | VcVatReturn | VcVatOther => "vat"
      case PfCt | BtaCt => "corporation-tax"
      case PfEpayeNi | BtaEpayeGeneral | BtaEpayeBill => "epaye"
      case PfEpayeLpp => "epaye-late-payment-penalty"
      case PfEpayeSeta => "epaye-settlement-agreement"
      case PfEpayeLateCis => "cis-late-filing-penalty"
      case BtaEpayePenalty => "epaye-penalty"
      case BtaEpayeInterest => "epaye-interest"
      case PfEpayeP11d | BtaClass1aNi => "class-1a-national-insurance"
      case PfP800 | PtaSimpleAssessment => "p800-or-pa302"
      case PfSdlt => "stamp-duty"
      case PfSdil | BtaSdil | DdSdil => "soft-drinks-industry-levy"
      case PfCds => "cds"
      case PfTpes => "taxes-penalties-and-enquiry-settlements"
      case PfClass2Ni => "class-2-national-insurance"
      case PfClass3Ni => "class-3-national-insurance"
      case PfInsurancePremium => "insurance-premium-tax"
      case PfMgd => "machine-games-duty"
      case PfSimpleAssessment => "simple-assessment"
      case PfPsAdmin => "pension-scheme-administrators"
      case PfBioFuels => "bio-fuels-or-road-gas"
      case PfAirPass => "air-passenger-duty"
      case PfBeerDuty => "beer-duty"
      case PfGamingOrBingoDuty => "gaming-or-bingo-duty"
      case PfGbPbRgDuty => "general-betting-pool-betting-remote-gaming-duty"
      case PfLandfillTax => "landfill-tax"
      case PfAggregatesLevy => "aggregates-levy"
      case PfClimateChangeLevy => "climate-change-levy"
      case PfOther => "other-taxes"
      case Amls => "anti-money-laundering"
      case BcPngr => "passengers"
      case CapitalGainsTax => "capital-gains-tax"
      case Mib => "merchandise-in-baggage"
      case Ni => "northern-ireland"
      case Parcels => "parcels"
      case PfJobRetentionScheme => "job-retention-scheme"
      case JrsJobRetentionScheme => "job-retention-scheme"
    }

    //TODO: is it used anywhere?
    def presentationMode: String = o match {
      case BtaSa | BtaVat | BtaCt | BtaEpayeGeneral | BtaEpayeBill | BtaEpayePenalty | BtaEpayeInterest | BtaClass1aNi
        | BtaSdil => "BTA"
      case PtaSa | PtaSimpleAssessment => "PTA"
      case PfSa | PfVat | PfCt | PfEpayeNi | PfEpayeP11d | PfEpayeLpp | PfEpayeSeta | PfEpayeLateCis | PfSdil | PfSdlt
        | PfP800 | PfCds | PfTpes | PfClass2Ni | PfClass3Ni | PfInsurancePremium | PfMgd | PfSimpleAssessment
        | PfPsAdmin | PfBioFuels | PfAirPass | PfBeerDuty | PfGamingOrBingoDuty | PfGbPbRgDuty | PfLandfillTax
        | PfAggregatesLevy | PfClimateChangeLevy | PfOther => "PF-GOVUK"
      case VcVatReturn | VcVatOther | DdVat | DdSdil => "MTD-BTA"
      case ItSa                                      => "MTD-PTA"
      case Amls | BcPngr | CapitalGainsTax | Mib | Ni | Parcels
        | PfJobRetentionScheme | JrsJobRetentionScheme => "Other"
    }
  }
}
