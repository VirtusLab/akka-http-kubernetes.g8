package $package$

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import $package$.model.GuestbookEntry
import org.scalatest.{ BeforeAndAfterAll, GivenWhenThen }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GuestbookActorSpec extends AnyFlatSpec with BeforeAndAfterAll with Matchers {

  val testKit = ActorTestKit()

  it should "return empty list when there are no entries" in new Fixture {
    guestbook.tell(GuestbookActor.GetEntries(probe.ref))

    probe.expectMessage(GuestbookActor.EntriesList(Seq.empty[GuestbookEntry]))
  }

  it should "return empty a list of entries" in new Fixture {
    val entry = GuestbookEntry("foo", "bar")

    guestbook.tell(GuestbookActor.AddEntry(entry, probe.ref))

    probe.expectMessage(GuestbookActor.EntryAdded(entry))

    guestbook.tell(GuestbookActor.GetEntries(probe.ref))

    probe.expectMessage(GuestbookActor.EntriesList(Seq(entry)))
  }

  trait Fixture {
    val guestbook = testKit.spawn(GuestbookActor())
    val probe = testKit.createTestProbe[GuestbookActor.Event]()
  }

  override protected def afterAll(): Unit = testKit.shutdownTestKit()
}
