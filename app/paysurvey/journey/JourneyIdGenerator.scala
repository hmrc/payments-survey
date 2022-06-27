/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package paysurvey.journey

import reactivemongo.bson.BSONObjectID

import javax.inject.Singleton

@Singleton
class JourneyIdGenerator {
  def nextJourneyId(): SurveyJourneyId = SurveyJourneyId(BSONObjectID.generate.stringify)
}
