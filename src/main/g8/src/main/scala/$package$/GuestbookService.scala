package $package$

import akka.actor.typed.{ ActorRef, ActorSystem }
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import com.typesafe.scalalogging.Logger
import $package$.model.GuestbookEntry

import scala.concurrent.duration._
import scala.concurrent.Future

trait GuestbookService {
  def createEntry(entry: GuestbookEntry): Future[GuestbookEntry]

  def getEntries(): Future[Seq[GuestbookEntry]]
}

class InMemoryGuestbookService(guestbookActor: ActorRef[GuestbookActor.Command])(implicit system: ActorSystem[Nothing]) extends GuestbookService {
  implicit val timeout = Timeout(1.second)
  import system.executionContext

  private val logger = Logger[InMemoryGuestbookService]

  def createEntry(entry: GuestbookEntry): Future[GuestbookEntry] = {
    logger.debug(s"createEntry: entry=\$entry")
    val result: Future[GuestbookActor.Event] = guestbookActor.ask(res => GuestbookActor.AddEntry(entry, res))
    result.map {
      case GuestbookActor.EntryAdded(res) =>
        logger.debug(s"createEntry: entry=\$entry added successfully")
        res
      case evt =>
        logger.warn(s"Unrecognized event receiver \$evt")
        throw new Exception(s"Unrecognized event received \$evt")
    }
  }

  def getEntries(): Future[Seq[GuestbookEntry]] = {
    logger.debug("getEntries")
    val result: Future[GuestbookActor.Event] = guestbookActor.ask(res => GuestbookActor.GetEntries(res))
    result.map {
      case GuestbookActor.EntriesList(res) =>
        logger.debug(s"getEntries: returning list of entries (debug limit 10) \${res.take(10)}")
        res
      case evt =>
        logger.warn(s"Unrecognized event receiver \$evt")
        throw new Exception(s"Unrecognized event received \$evt")
    }
  }
}
