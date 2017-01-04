package resources.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.{ToResponseMarshallable, ToResponseMarshaller}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.{Directives, Route}
import org.json4s.{DefaultFormats, native}

import scala.concurrent.{ExecutionContext, Future}

trait BaseResource extends Directives {

  implicit val serialization = native.Serialization
  implicit val formats = DefaultFormats

  implicit def executionContext: ExecutionContext

  implicit val system = ActorSystem("microservices-cqrs")

  def completeWithLocationHeader[T](resourceId: Future[Option[T]], ifDefinedStatus: Int, ifEmptyStatus: Int): Route =
    onSuccess(resourceId) {
      case Some(t) => completeWithLocationHeader(ifDefinedStatus, t)
      case None => complete(ifEmptyStatus, None)
    }

  def completeWithLocationHeader[T](status: Int, resourceId: T): Route =
    extractRequestContext { requestContext =>
      val request = requestContext.request
      val location = request.uri.copy(path = request.uri.path / resourceId.toString)
      respondWithHeader(Location(location)) {
        complete(status, None)
      }
    }

  def complete[T: ToResponseMarshaller](resource: Future[Option[T]]): Route =
    onSuccess(resource) {
      case Some(t) => complete(ToResponseMarshallable(t))
      case None => complete(404, None)
    }

  def complete(resource: Future[Unit]): Route = onSuccess(resource) { complete(204, None) }

}
