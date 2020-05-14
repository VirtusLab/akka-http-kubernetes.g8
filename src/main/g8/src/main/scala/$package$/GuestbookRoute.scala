package $package$

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import $package$.model.GuestbookEntry
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.syntax._

class GuestbookRoute(guestbookService: GuestbookService) {

  def route: Route = path("guestbook") {
    pathEndOrSingleSlash {
      (post & entity(as[GuestbookEntry])) { entry =>
        onSuccess(guestbookService.createEntry(entry)) { _ =>
          complete(StatusCodes.Created -> entry.asJson)
        }
      } ~
        get {
          onSuccess(guestbookService.getEntries()) { entries =>
            complete(StatusCodes.OK -> entries.asJson)
          }
        }
    }
  }
}
