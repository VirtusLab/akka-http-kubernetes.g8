package $package$

import akka.actor.typed.scaladsl.adapter._
import akka.actor.{ ActorSystem => UntypedActorSystem }
import akka.http.scaladsl.Http
import $package$.config.ServiceConfig

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {

  implicit val system = UntypedActorSystem("micro-template")
  import system.dispatcher

  private val serviceConfig = ServiceConfig.load()

  private val guestbook = system.spawn(GuestbookActor(), "guestbook")
  private val guestbookService = new InMemoryGuestbookService(guestbook)(system.toTyped)
  private val guestbookRoute = new GuestbookRoute(guestbookService)

  private val healthRoute = new HealthRoute

  val routes = {
    import akka.http.scaladsl.server.Directives._
    guestbookRoute.route ~ healthRoute.route
  }

  val bindingFuture = Http().bindAndHandle(
    handler = routes,
    interface = serviceConfig.host.name,
    port = serviceConfig.port.number
  )

  sys.addShutdownHook {
    Await.ready(bindingFuture.flatMap(_.unbind()), 10.seconds)
    Await.ready(system.terminate(), 10.seconds)
  }
}
