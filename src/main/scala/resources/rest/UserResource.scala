package resources.rest

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.server.Route
import models.UserRepository.AddUserCommand
import models.{StubUser, User, UserPersistenceActor, UserRepository}
import util.UUIDGenerator.generate

import scala.concurrent.{ExecutionContext, Future}

trait UserResource extends BaseResource {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val userPersistanceActor: ActorRef = system.actorOf(Props[UserPersistenceActor])

  def addUser(user: User): Future[Option[String]] =
    Future {
      val id: String = generate()
      StubUser(id)
      Some(id)
    }(executionContext)

  def fetchItem(id: Long): Future[Option[User]] =
    Future {
      Option(
        StubUser()
      )
    }

  def fetchUsers(): Future[List[User]] =
    Future {
      List(StubUser())
    }

  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._

  val userRoutes: Route = pathPrefix("users") {
    pathEnd {
      post {
        entity(as[AddUserCommand]) { user =>
          completeWithLocationHeader(
            resourceId = Future {
              Option {
                userPersistanceActor ! AddUserCommand(user.name, user.surname)
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

}
