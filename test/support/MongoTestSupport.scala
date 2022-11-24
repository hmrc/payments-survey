package support

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{AppendedClues, BeforeAndAfterAll, BeforeAndAfterEach, Suite}
import play.api.Logging
import uk.gov.hmrc.mongo.test.MongoSupport

trait MongoTestSupport extends MongoSupport with BeforeAndAfterAll with BeforeAndAfterEach with Logging {
  self: Suite with ScalaFutures with AppendedClues =>

  //longer timeout for dropping database or cleaning collections
  //  private val longPatienceConfig = PatienceConfig(
  //    timeout  = scaled(Span(6, Seconds)),
  //    interval = scaled(Span(50, Millis)) //tests aren't run in parallel so why bother with waiting longer
  //  )

  override def beforeEach(): Unit = {
    super.beforeEach()
    dropDatabase()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    dropDatabase()
  }
}
