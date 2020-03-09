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

import java.time.format.DateTimeFormatter

import payapi.cardpaymentjourney.model.journey._
import payapi.cardpaymentjourney.model.taxes.epaye.{ AccountsOfficeReference, SubYearlyEpayeTaxPeriod, YearlyEpayeTaxPeriod }
import payapi.cardpaymentjourney.model.taxes.sdlt.Utrn
import payapi.cardpaymentjourney.model.taxes.vat.VatPeriod
import payapi.corcommon.model.Reference
import payapi.corcommon.model.p800.P800ChargeRef

object JsdToContentOptions {

  def toContentOptions(jsd: JourneySpecificData): ContentOptions = jsd match {
    case j: JsdItSa => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your Self Assessment", welshValue = Option("Talu eich Hunanasesiad")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdDdVat => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Business tax account", welshValue = Option("Cyfrif treth busnes")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdVcVatReturn => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Business tax account", welshValue = Option("Cyfrif treth busnes")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdVcVatOther => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Business tax account", welshValue = Option("Cyfrif treth busnes")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfVat => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your VAT",
        welshValue = Some("Talu eich TAW")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaVat => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your VAT", welshValue = Option("Talu eich TAW")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaEpayeBill => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your employers’ PAYE and National Insurance",
        welshValue = Some("Talwch eich TWE a'ch Yswiriant Gwladol y cyflogwr")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaEpayePenalty => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your PAYE late payment or filing penalty",
        welshValue = Some("Talu’ch cosb am dalu neu gyflwyno TWE yn hwyr")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaEpayeInterest => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay employers’ PAYE interest",
        welshValue = Some("Talu llog TWE cyflogwr")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaEpayeGeneral => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your employers’ PAYE and National Insurance",
        welshValue = Some("Talwch eich TWE a'ch Yswiriant Gwladol y cyflogwr")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaClass1aNi => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your employers’ Class 1A National Insurance (P11D bill)",
        welshValue = Some("Talu’ch Yswiriant Gwladol Dosbarth 1A y cyflogwr (bil P11D)")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfEpayeNi => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your employers’ PAYE and National Insurance",
        welshValue = Some("Talwch eich TWE a'ch Yswiriant Gwladol y cyflogwr")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfEpayeLpp => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your PAYE late payment or filing penalty",
        welshValue = Some("Talwch eich cosb am dalu TWE y cyflogwr yn hwyr")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfEpayeSeta => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your PAYE Settlement Agreement",
        welshValue = Some("Talwch eich Cytundeb Setliad TWE y cyflogwr")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfEpayeLateCis => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your Construction Industry Scheme penalty",
        welshValue = Some("Talwch eich cosb - Cynllun y Diwydiant Adeiladu")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfEpayeP11d => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Pay your employers’ Class 1A National Insurance (P11D bill)",
        welshValue = Some("Talu’ch Yswiriant Gwladol Dosbarth 1A y cyflogwr (bil P11D)")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfPsAdmin => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your Pension scheme administrators", welshValue = Some("Talu eich gweinyddwyr cynllun pensiwn")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfOther => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your tax", welshValue = Some("Talwch-eich-treth")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfP800 => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Check how much Income Tax you paid", welshValue = Some("Gwirio faint o dreth incwm a daloch")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfClass2Ni => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Class 2 National Insurance", Some("Talu Yswiriant Gwladol Dosbarth 2")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfSdlt => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your Stamp Duty Land Tax", Some("Talwch eich Treth Dir y Tollau Stamp")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfCds => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title = BannerTitle("Customs Declaration Service"),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdParcels => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title = BannerTitle("Manage your import VAT on parcels you sell to UK buyers"),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBcPngr => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title = BannerTitle(englishValue = "Check tax on goods you bring into the UK", welshValue = None),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdMib => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title = BannerTitle(englishValue = "Merchandise in Baggage"),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfCt => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your Corporation Tax", welshValue = Some("Talu eich Treth Gorfforaeth")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaCt => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your Corporation Tax", welshValue = Some("Talu eich Treth Gorfforaeth")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdAmls => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title = BannerTitle(englishValue = "AMLS Payment"),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaSa => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your Self Assessment", welshValue = Option("Talu eich Hunanasesiad")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPtaSa => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your Self Assessment", welshValue = Some("Talu eich Hunanasesiad")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdNi => ContentOptions(
      isWelshSupported = IsWelshSupported.no,
      title = BannerTitle(englishValue = "Manage your Northern Ireland import VAT", welshValue = None),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfInsurancePremium => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Insurance Premium Tax", Some("Talu Treth Premiwm Yswiriant")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfClass3Ni => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay voluntary Class 3 National Insurance", Some("Talu Yswiriant Gwladol Dosbarth 3 gwirfoddol")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfBioFuels => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay duty on biofuels or gas for road use", Some("Talu toll ar fiodanwyddau neu ar nwy ar gyfer defnydd y ffordd")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfAirPass => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Air Passenger Duty", Some("Talu Toll Teithwyr Awyr")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfMgd => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Machine Games Duty", welshValue = Some("Talu’r Doll Peiriannau Hapchwarae")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfBeerDuty => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Beer Duty", welshValue = Some("Talu Toll Cwrw")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfGamingOrBingoDuty => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Gaming or Bingo Duty", welshValue = Some("Talu Toll Hapchwarae neu Doll Bingo")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfGbPbRgDuty =>
      val referenceToDisplay = j.prn.map(_.value).getOrElse("").toUpperCase()

      val line0 = InternationalisedLine(
        english = Line("Reference number", referenceToDisplay),
        welsh = Some(Line("Cyfeirnod", referenceToDisplay)))

      ContentOptions(
        isWelshSupported = IsWelshSupported.yes,
        title = BannerTitle(englishValue = "Pay General Betting, Pool Betting or Remote Gaming Duty", welshValue = Some("Talu Toll Betio Cyffredinol, Toll Cronfa Fetio neu Doll Hapchwarae o Bell")),
        showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfLandfillTax => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Landfill Tax", welshValue = Some("Talu Treth Dirlenwi ")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfAggregatesLevy => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Aggregates Levy", welshValue = Some("Talu Ardoll Agregau")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfClimateChangeLevy => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay Climate Change Levy", welshValue = Some("Talu’r Ardoll Newid yn yr Hinsawdd")),
      showBetaBanner = ShowBetaBanner.no)

    case _: JsdPfSdil => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay the Soft Drinks Industry Levy", welshValue = Some("Talu Ardoll y Diwydiant Diodydd Ysgafn")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdBtaSdil => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay the Soft Drinks Industry Levy", welshValue = Some("Talu Ardoll y Diwydiant Diodydd Ysgafn")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfSimpleAssessment => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay your Simple Assessment", welshValue = Some("Talu eich Asesiad Syml")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdPfTpes => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(englishValue = "Pay taxes, penalties or enquiry settlements", welshValue = Some("Talu trethi, cosbau neu setliadau ymholiadau")),
      showBetaBanner = ShowBetaBanner.no)

    case j: JsdCapitalGainsTax => ContentOptions(
      isWelshSupported = IsWelshSupported.yes,
      title = BannerTitle(
        englishValue = "Report and pay Capital Gains Tax on UK property",
        welshValue = Some("Rhoi gwybod am a thalu Treth Enillion Cyfalaf ar eiddo yn y DU")),
      showBetaBanner = ShowBetaBanner.no)
  }
}
