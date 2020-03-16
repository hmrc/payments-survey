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
      case PfP800 => "p800"
      case PfSdlt => "stamp-duty"
      case PfSdil | BtaSdil => "soft-drinks-industry-levy"
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
    }
  }

  implicit class TaxTypeExt(val t: TaxType) extends AnyVal {
    def auditName: String = t match {
      case TaxTypes.selfAssessment => "self-assessment"
      case TaxTypes.vat => "vat"
      case TaxTypes.epaye => "epaye"
      case TaxTypes.corporationTax => "corporation-tax"
      case TaxTypes.other => "other-taxes"
      case TaxTypes.class2NationalInsurance => "class-2-national-insurance"
      case TaxTypes.ni => "northern-ireland"
      case TaxTypes.parcels => "parcels"
      case TaxTypes.mib => "unimplemented"
      case TaxTypes.stampDuty => "unimplemented"
      case TaxTypes.cds => "unimplemented"
      case TaxTypes.pngr => "unimplemented"
      case TaxTypes.p800 => "unimplemented"
      case TaxTypes.insurancePremium => "unimplemented"
      case TaxTypes.class3NationalInsurance => "unimplemented"
      case TaxTypes.bioFuelsAndRoadGas => "unimplemented"
      case TaxTypes.airPassengerDuty => "unimplemented"
      case TaxTypes.beerDuty => "unimplemented"
      case TaxTypes.landfillTax => "unimplemented"
      case TaxTypes.aggregatesLevy => "unimplemented"
      case TaxTypes.climateChangeLevy => "unimplemented"
      case TaxTypes.epayeTpes => "unimplemented"
      case TaxTypes.capitalGainsTax => "unimplemented"
    }
  }

  implicit class JsdExt(val jsd: JourneySpecificData) extends AnyVal {
    def contentOptions: ContentOptions = JsdToContentOptions.toContentOptions(jsd)
  }
}
