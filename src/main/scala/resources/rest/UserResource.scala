package resources.rest

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.server.Route
import models.UserRepository.AddUserCommand
import models.{StubUser, User, UserPersistenceActor}
import util.UUIDGenerator.generate

import scala.concurrent.{ExecutionContext, Future}

trait UserResource extends BaseResource {

  import configurations.WebServer._

  implicit val ec: ExecutionContext = ExecutionContext.global

  val userPersistanceActor: ActorRef = system.actorOf(Props[UserPersistenceActor])

  def addUser(user: User): Future[Option[String]] =
    Future {
      val id : String = generate()
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

  val userRoutes: Route = pathPrefix("users") {
    pathEnd {
      post {
        entity(as[User]) { user =>
          completeWithLocationHeader(
            resourceId = {
              ask(userPersistanceActor, AddUserCommand(user.name, user.surname))
                  .
//              userPersistanceActor ? AddUserCommand(user.name, user.surname)

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
