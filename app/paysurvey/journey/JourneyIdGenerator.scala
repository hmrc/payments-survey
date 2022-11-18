/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package paysurvey.journey

import org.bson.BsonObjectId
import javax.inject.Singleton

@Singleton
class JourneyIdGenerator {
  def nextJourneyId(): SurveyJourneyId = SurveyJourneyId(new BsonObjectId().getValue.toString)
}
