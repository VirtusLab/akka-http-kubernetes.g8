package $package$

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import $package$.model.GuestbookEntry
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.syntax._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class GuestbookRouteSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {

  behavior of "GuestbookRoute#POST"

  it should "return a bad request when body is empty" in new Fixture {
    Post("/guestbook") ~> route ~> check {
      status shouldBe StatusCodes.BadRequest
    }
  }

  it should "create entry when body is correct" in new Fixture {
    private val entry = GuestbookEntry("abc", "def")

    Post("/guestbook", entry) ~> route ~> check {
      status shouldBe StatusCodes.Created
      responseAs[GuestbookEntry] shouldBe entry
    }
  }

  behavior of "GuestbookRoute#GET"

  it should "return a list of entries" in new Fixture {
    Get("/guestbook") ~> route ~> check {
      status shouldBe StatusCodes.OK
      responseAs[Seq[GuestbookEntry]]
    }
  }

  trait Fixture {
    var entries = Seq(GuestbookEntry("foo", "bar"))

    private val guestbookService = new GuestbookService {
      override def createEntry(entry: GuestbookEntry): Future[GuestbookEntry] = {
        entries = entries :+ entry
        Future.successful(entry)
      }

      override def getEntries(): Future[Seq[GuestbookEntry]] = Future.successful(entries)
    }

    val route = Route.seal(new GuestbookRoute(guestbookService).route)
  }
}
