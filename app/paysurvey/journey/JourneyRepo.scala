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

import config.AppConfig
import model.content.{BannerTitle, ContentOptions, IsWelshSupported}
import org.bson.BsonType
import org.mongodb.scala.model.Indexes.{ascending, descending}
import org.mongodb.scala.model.{Filters, IndexModel, IndexOptions, Indexes, Updates}
import org.mongodb.scala.result.UpdateResult
import paysurvey.audit.AuditOptions
import play.api.Logging
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
@SuppressWarnings(Array("org.wartremover.warts.Any"))
final class JourneyRepo @Inject() (appConfig: AppConfig, mongo: MongoComponent)(implicit ec: ExecutionContext)
    extends PlayMongoRepository[SurveyJourney](
      mongoComponent = mongo,
      collectionName = "journey",
      indexes = JourneyRepo.getIndexes(appConfig),
      domainFormat = SurveyJourney.format,
      replaceIndexes = true
    )
    with Logging {

  /** Find the latest journey for given sessionId.
    */
  def findLatestJourneyByJourneyId(journeyId: SurveyJourneyId): Future[Option[SurveyJourney]] = {
    collection
      .withReadPreference(com.mongodb.ReadPreference.primary())
      .find(Filters.equal("journeyId", journeyId.value))
      .sort(descending("createdOn"))
      .toFuture()
      .map(_.headOption)
  }

  def insert(surveyJourney: SurveyJourney): Future[Unit] = {
    collection
      .insertOne(surveyJourney)
      .toFuture()
      .map(result => if (result.wasAcknowledged()) () else throw new RuntimeException(result.toString))
  }

  def updateCreatedOnTime(): Future[Unit] = {
    logger.info(s"Inside updateCreatedOnTime: ${LocalDateTime.now().toString}")
    collection
      .updateMany(
        Filters.`type`("createdOn", BsonType.STRING),
        Updates.set("createdOn", LocalDateTime.now().minusDays(15))
      )
      .toFuture()
      .map { (result: UpdateResult) =>
        if (result.wasAcknowledged()) {
          logger.info(s"Inside wasAcknowledged, finished updating: ${LocalDateTime.now().toString}")()
        } else {
          throw new RuntimeException(result.toString)
        }
      }
  }

  // to be used for building test records (2.5M?)
  def insertMany(): Future[Unit] = {
    val testRecords: Seq[SurveyJourney] = (1 to 500000).map { _ =>
      SurveyJourney(
        journeyId = SurveyJourneyId(UUID.randomUUID().toString),
        content = ContentOptions(IsWelshSupported.yes, BannerTitle("blah", None)),
        audit = AuditOptions("wuh", None, None, None),
        origin = "Origin",
        returnMsg = "returnMsg",
        returnHref = "returnHref",
        createdOn = LocalDateTime.now()
      )
    }

    collection.insertMany(testRecords).toFuture().map(_ => ())
  }
}

object JourneyRepo {

  private def getIndexes(appConfig: AppConfig): Seq[IndexModel] = Seq[IndexModel](
    IndexModel(
      keys = ascending("journeyId"),
      indexOptions = IndexOptions().name("journeyId")
    ),
    IndexModel(
      keys = Indexes.ascending("createdOn"),
      indexOptions = IndexOptions().expireAfter(appConfig.mongoExpiry.toSeconds, TimeUnit.SECONDS).name("createdOnIdx")
    )
  )
}
