package resources.rest

import akka.http.scaladsl.server.Route
import models.{StubUser, User}
import util.UUIDGenerator.generate

import scala.concurrent.{ExecutionContext, Future}

trait UserResource extends BaseResource {

  implicit val ec: ExecutionContext = ExecutionContext.global

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
            resourceId = addUser(user),
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
