/*
 * Copyright 2026 HM Revenue & Customs
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

import org.mongodb.scala.model.IndexModel
import support.AppSpec

import java.util.concurrent.TimeUnit

class RepoSpec extends AppSpec {

  "JourneyRepo should have an index which sets a time to live of 14 days" in {
    val indexes: Seq[IndexModel] = app.injector.instanceOf[JourneyRepo].indexes
    val createdOnIndexOption = indexes.filter(_.getKeys.toBsonDocument.containsKey("createdOn"))
    val ttl = createdOnIndexOption.map(_.getOptions.getExpireAfter(TimeUnit.DAYS))
    ttl shouldBe List(14)
  }

}
