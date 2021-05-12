/*
 * Copyright 2021 HM Revenue & Customs
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

import payapi.cardpaymentjourney.model.journey._

object JsdToContentOptions {

  def toContentOptions(jsd: JourneySpecificData): ContentOptions = jsd match {
    case _: JsdItSa => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Self Assessment", welshValue = Option("Talu eich Hunanasesiad"))
    )

    case _: JsdDdVat => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Business tax account", welshValue = Option("Cyfrif treth busnes"))
    )

    case _: JsdVcVatReturn => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Business tax account", welshValue = Option("Cyfrif treth busnes"))
    )

    case _: JsdVcVatOther => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Business tax account", welshValue = Option("Cyfrif treth busnes"))
    )

    case _: JsdPfVat => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your VAT",
        welshValue   = Some("Talu eich TAW")
      )
    )

    case _: JsdBtaVat => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your VAT", welshValue = Option("Talu eich TAW"))
    )

    case _: JsdBtaEpayeBill => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your employers’ PAYE and National Insurance",
        welshValue   = Some("Talwch eich TWE a'ch Yswiriant Gwladol y cyflogwr")
      )
    )

    case _: JsdBtaEpayePenalty => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your PAYE late payment or filing penalty",
        welshValue   = Some("Talu’ch cosb am dalu neu gyflwyno TWE yn hwyr")
      )
    )

    case _: JsdBtaEpayeInterest => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay employers’ PAYE interest",
        welshValue   = Some("Talu llog TWE cyflogwr")
      )
    )

    case _: JsdBtaEpayeGeneral => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your employers’ PAYE and National Insurance",
        welshValue   = Some("Talwch eich TWE a'ch Yswiriant Gwladol y cyflogwr")
      )
    )

    case _: JsdBtaClass1aNi => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your employers’ Class 1A National Insurance (P11D bill)",
        welshValue   = Some("Talu’ch Yswiriant Gwladol Dosbarth 1A y cyflogwr (bil P11D)")
      )
    )

    case _: JsdPfEpayeNi => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your employers’ PAYE and National Insurance",
        welshValue   = Some("Talwch eich TWE a'ch Yswiriant Gwladol y cyflogwr")
      )
    )

    case _: JsdPfEpayeLpp => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your PAYE late payment or filing penalty",
        welshValue   = Some("Talwch eich cosb am dalu TWE y cyflogwr yn hwyr")
      )
    )

    case _: JsdPfEpayeSeta => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your PAYE Settlement Agreement",
        welshValue   = Some("Talwch eich Cytundeb Setliad TWE y cyflogwr")
      )
    )

    case _: JsdPfEpayeLateCis => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your Construction Industry Scheme penalty",
        welshValue   = Some("Talwch eich cosb - Cynllun y Diwydiant Adeiladu")
      )
    )

    case _: JsdPfEpayeP11d => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay your employers’ Class 1A National Insurance (P11D bill)",
        welshValue   = Some("Talu’ch Yswiriant Gwladol Dosbarth 1A y cyflogwr (bil P11D)")
      )
    )

    case _: JsdPfPsAdmin => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Pension scheme administrators", welshValue = Some("Talu eich gweinyddwyr cynllun pensiwn"))
    )

    case _: JsdPfOther => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your tax", welshValue = Some("Talwch-eich-treth"))
    )

    case _: JsdPfP800 => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Check how much Income Tax you paid", welshValue = Some("Gwirio faint o dreth incwm a daloch"))
    )

    case _: JsdPfClass2Ni => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Class 2 National Insurance", Some("Talu Yswiriant Gwladol Dosbarth 2"))
    )

    case _: JsdPfSdlt => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Stamp Duty Land Tax", Some("Talwch eich Treth Dir y Tollau Stamp"))
    )

    case _: JsdPfCds => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title            = BannerTitle("Customs Declaration Service")
    )

    case _: JsdParcels => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title            = BannerTitle("Manage your import VAT on parcels you sell to UK buyers")
    )

    case _: JsdBcPngr => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title            = BannerTitle(englishValue = "Check tax on goods you bring into the UK", welshValue = None)
    )

    case _: JsdMib => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title            = BannerTitle(englishValue = "Merchandise in Baggage")
    )

    case _: JsdPfCt => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Corporation Tax", welshValue = Some("Talu eich Treth Gorfforaeth"))
    )

    case _: JsdBtaCt => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Corporation Tax", welshValue = Some("Talu eich Treth Gorfforaeth"))
    )

    case _: JsdAmls => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title            = BannerTitle(englishValue = "AMLS Payment")
    )

    case _: JsdBtaSa => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Self Assessment", welshValue = Option("Talu eich Hunanasesiad"))
    )

    case _: JsdPtaSa => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Self Assessment", welshValue = Some("Talu eich Hunanasesiad"))
    )

    case _: JsdPfSa => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Self Assessment", welshValue = Some("Talu eich Hunanasesiad"))
    )

    case _: JsdNi => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title            = BannerTitle(englishValue = "Manage your Northern Ireland import VAT", welshValue = None)
    )

    case _: JsdPfInsurancePremium => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Insurance Premium Tax", Some("Talu Treth Premiwm Yswiriant"))
    )

    case _: JsdPfClass3Ni => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay voluntary Class 3 National Insurance", Some("Talu Yswiriant Gwladol Dosbarth 3 gwirfoddol"))
    )

    case _: JsdPfBioFuels => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay duty on biofuels or gas for road use", Some("Talu toll ar fiodanwyddau neu ar nwy ar gyfer defnydd y ffordd"))
    )

    case _: JsdPfAirPass => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Air Passenger Duty", Some("Talu Toll Teithwyr Awyr"))
    )

    case _: JsdPfMgd => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Machine Games Duty", welshValue = Some("Talu’r Doll Peiriannau Hapchwarae"))
    )

    case _: JsdPfBeerDuty => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Beer Duty", welshValue = Some("Talu Toll Cwrw"))
    )

    case _: JsdPfGamingOrBingoDuty => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Gaming or Bingo Duty", welshValue = Some("Talu Toll Hapchwarae neu Doll Bingo"))
    )

    case j: JsdPfGbPbRgDuty =>
      val referenceToDisplay = j.prn.map(_.value).getOrElse("").toUpperCase()

      val line0 = InternationalisedLine(
        english = Line("Reference number", referenceToDisplay),
        welsh   = Some(Line("Cyfeirnod", referenceToDisplay))
      )

      ContentOptions(
        isWelshSupported = IsWelshSupported.yes,
        title            = BannerTitle(englishValue = "Pay General Betting, Pool Betting or Remote Gaming Duty", welshValue = Some("Talu Toll Betio Cyffredinol, Toll Cronfa Fetio neu Doll Hapchwarae o Bell"))
      )

    case _: JsdPfLandfillTax => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Landfill Tax", welshValue = Some("Talu Treth Dirlenwi "))
    )

    case _: JsdPfAggregatesLevy => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Aggregates Levy", welshValue = Some("Talu Ardoll Agregau"))
    )

    case _: JsdPfClimateChangeLevy => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay Climate Change Levy", welshValue = Some("Talu’r Ardoll Newid yn yr Hinsawdd"))
    )

    case _: JsdPfSdil => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay the Soft Drinks Industry Levy", welshValue = Some("Talu Ardoll y Diwydiant Diodydd Ysgafn"))
    )

    case _: JsdBtaSdil => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay the Soft Drinks Industry Levy", welshValue = Some("Talu Ardoll y Diwydiant Diodydd Ysgafn"))
    )

    case _: JsdDdSdil => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay the Soft Drinks Industry Levy", welshValue = Some("Talu Ardoll y Diwydiant Diodydd Ysgafn"))
    )

    case _: JsdPfSimpleAssessment => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Simple Assessment", welshValue = Some("Talu eich Asesiad Syml"))
    )

    case _: JsdPfTpes => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay taxes, penalties or enquiry settlements", welshValue = Some("Talu trethi, cosbau neu setliadau ymholiadau"))
    )

    case _: JsdCapitalGainsTax => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Report and pay Capital Gains Tax on UK property",
        welshValue   = Some("Rhoi gwybod am a thalu Treth Enillion Cyfalaf ar eiddo yn y DU")
      )
    )

    case _: JsdPtaSimpleAssessment => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(englishValue = "Pay your Simple Assessment", welshValue = Some("Talu eich Asesiad Syml"))
    )

    case _: JsdPfJobRetentionScheme => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay Coronavirus Job Retention Scheme grants back",
        welshValue   = Some("Talu grantiau’r Cynllun Cadw Swyddi yn sgil Coronafeirws yn ôl")
      )
    )

    case _: JsdJrsJobRetentionScheme => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay Coronavirus Job Retention Scheme grants back",
        welshValue   = Some("Talu grantiau’r Cynllun Cadw Swyddi yn sgil Coronafeirws yn ôl")
      )
    )
    case _: JsdPfChildBenefitRepayments => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title            = BannerTitle(
        englishValue = "Pay Coronavirus Job Retention Scheme grants back",
        welshValue   = Some("Talu grantiau’r Cynllun Cadw Swyddi yn sgil Coronafeirws yn ôl")
      )
    )
  }
}
