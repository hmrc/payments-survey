package support

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{AppendedClues, BeforeAndAfterAll, BeforeAndAfterEach, Suite}
import play.api.Logging
import uk.gov.hmrc.mongo.test.MongoSupport

trait MongoTestSupport extends MongoSupport with BeforeAndAfterAll with BeforeAndAfterEach with Logging {
  self: Suite with ScalaFutures with AppendedClues =>

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropDatabase()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    dropDatabase()
  }
}
