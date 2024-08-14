/*
 * Copyright 2024 HM Revenue & Customs
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

package paysurvey.journey

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala.model.{Filters, IndexModel, IndexOptions}
import org.mongodb.scala.model.Indexes.{ascending, descending}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

@Singleton
@SuppressWarnings(Array("org.wartremover.warts.Any"))
final class JourneyRepo @Inject() (mongo: MongoComponent)(implicit ec: ExecutionContext)
  extends PlayMongoRepository[SurveyJourney](
    mongoComponent = mongo,
    collectionName = "journey",
    indexes        = Seq(IndexModel(ascending("journeyId"), IndexOptions().name("journeyId"))),
    domainFormat   = SurveyJourney.format,
    replaceIndexes = true
  ) {

  /**
   * Find the latest journey for given sessionId.
   */
  def findLatestJourneyByJourneyId(journeyId: SurveyJourneyId): Future[Option[SurveyJourney]] = {
    collection
      .withReadPreference(com.mongodb.ReadPreference.primary())
      .find(Filters.equal("journeyId", journeyId.value))
      .sort(descending("createdOn"))
      .toFuture().map(_.headOption)
  }

  def insert(surveyJourney: SurveyJourney): Future[Unit] = //Throw a new RuntimeException(writeResult.toString) if things go wrong
    collection
      .insertOne(surveyJourney)
      .toFuture()
      .map(result => if (result.wasAcknowledged()) () else throw new RuntimeException(result.toString))
}
