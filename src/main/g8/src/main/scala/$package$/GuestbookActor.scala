package $package$

import akka.actor.typed.{ ActorRef, Behavior }
import akka.actor.typed.scaladsl.Behaviors
import $package$.model.GuestbookEntry

object GuestbookActor {
  sealed trait Command {
    def replyTo: ActorRef[Event]
  }
  final case class AddEntry(entry: GuestbookEntry, replyTo: ActorRef[Event]) extends Command
  final case class GetEntries(replyTo: ActorRef[Event]) extends Command

  sealed trait Event
  final case class EntryAdded(entry: GuestbookEntry) extends Event
  final case class EntriesList(entries: Seq[GuestbookEntry]) extends Event

  def apply(entries: Seq[GuestbookEntry] = Seq.empty): Behavior[Command] =
    Behaviors.logMessages(
      Behaviors.receiveMessage[Command] {
        case AddEntry(entry, replyTo) =>
          replyTo ! EntryAdded(entry)
          apply(entries :+ entry)
        case GetEntries(replyTo) =>
          replyTo ! EntriesList(entries)
          Behaviors.same
      }
    )
}
