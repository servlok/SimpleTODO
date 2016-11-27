package configurations

import akka.http.scaladsl.server.Route
import resources.rest.UserResource

import scala.concurrent.ExecutionContext

trait RestSupport extends UserResource {
  implicit def executionContext: ExecutionContext

  val routes: Route = userRoutes
}
