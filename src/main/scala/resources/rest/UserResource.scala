package resources.rest

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.server.Route
import models.UserPersistenceActor
import models.UserRepository.{AddUser, UserAdded}

import scala.concurrent.{ExecutionContext, Future}

trait UserResource extends BaseResource {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val userPersistanceActor: ActorRef = system.actorOf(Props[UserPersistenceActor], "user-persistence")
  val userRoutes: Route = pathPrefix("users") {
    pathEnd {
      post {
        entity(as[AddUser]) { user =>
          completeWithLocationHeader(
            resourceId = Future {
              Option {
                userPersistanceActor ! AddUser(user.name, user.surname)
              }
            },
            ifDefinedStatus = 201,
            ifEmptyStatus = 404)

        }
      } ~
        get {
          complete(200, fetchUsers())
        }
    }
  }

  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._

  def fetchUsers(): Future[List[UserAdded]] =
    Future {
      List()
    }

}
