package $package$

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class HealthRoute {

  def route: Route =
    (get & path("health")) {
      pathEndOrSingleSlash {
        complete(StatusCodes.OK)
      }
    }
}
