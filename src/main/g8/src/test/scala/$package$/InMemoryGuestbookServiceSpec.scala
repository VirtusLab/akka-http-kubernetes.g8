package $package$

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import $package$.model.GuestbookEntry
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Await
import scala.concurrent.duration._

class InMemoryGuestbookServiceSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  val testKit = ActorTestKit()

  behavior of "InMemoryGuestbookService#createEntry"

  it should "fail when received unexpected event" in new Fixture {
    val entry = GuestbookEntry("foo", "bar")

    val action = service.createEntry(entry)

    val msg = guestbookActor.expectMessageType[GuestbookActor.AddEntry]
    msg.replyTo ! GuestbookActor.EntriesList(Seq.empty)

    assertThrows[Exception](Await.result(action, 1.second))
  }

  it should "add new entry" in new Fixture {
    val entry = GuestbookEntry("foo", "bar")

    val action = service.createEntry(entry)

    val msg = guestbookActor.expectMessageType[GuestbookActor.AddEntry]
    msg.replyTo ! GuestbookActor.EntryAdded(entry)

    Await.result(action, 1.second) shouldBe entry
  }

  behavior of "InMemoryGuestbookService#getEntries"

  it should "fail when received unexpected event" in new Fixture {
    val entry = GuestbookEntry("foo", "bar")

    val action = service.getEntries()

    val msg = guestbookActor.expectMessageType[GuestbookActor.GetEntries]
    msg.replyTo ! GuestbookActor.EntryAdded(entry)

    assertThrows[Exception](Await.result(action, 1.second))
  }

  it should "get list of all entries" in new Fixture {
    val entry = GuestbookEntry("foo", "bar")

    val action = service.getEntries()

    val msg = guestbookActor.expectMessageType[GuestbookActor.GetEntries]
    msg.replyTo ! GuestbookActor.EntriesList(Seq(entry))

    Await.result(action, 1.second) should contain theSameElementsAs Seq(entry)
  }

  trait Fixture {
    val guestbookActor = testKit.createTestProbe[GuestbookActor.Command]()
    val service = new InMemoryGuestbookService(guestbookActor.ref)(testKit.system)
  }

  override protected def afterAll(): Unit = testKit.shutdownTestKit()
}
