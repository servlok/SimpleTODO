package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.persistence.PersistentActor
import models.UserRepository.{AddUserCommand, UserAdded}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
import spray.json.DefaultJsonProtocol
import util.UUIDGenerator.generate

sealed trait Domain
final case class User(_id: String, name: String, surname: String) extends Domain

object User {
  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val userFormat = jsonFormat3((a : String, b : String, c :String) => User(a ,b ,c))
  }

  implicit object UserWriter extends BSONDocumentWriter[User] {
    def write(user: User): BSONDocument = BSONDocument(
        "id" -> user._id,
        "name" -> user.name,
        "surname" -> user.surname)
  }

  implicit object UserReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User =
      User(
        doc.getAs[String]("id").get,
        doc.getAs[String]("name").get,
        doc.getAs[String]("surname").get)
  }
}

object UserRepository {
  sealed trait Command
  case class AddUserCommand(name: String, surname: String) extends Command

  sealed trait Event
  case class UserAdded(id: String, name: String, surname: String) extends Event

}

object StubUser {
  def apply() : User = User(generate(), "some-name", "some-surname")
  def apply(id :String) : User = User(id, "some-name", "some-surname")
}

trait UserPersistenceActor extends PersistentActor {
  override def persistenceId: String = "user-persistence-1"

  override def receiveRecover: Receive = {
    case UserAdded(id, name, surname) => updateState(User(id, name, surname))
  }

  var state : Seq[User] = Seq.empty[User]

  def updateState(user: User) = state :+ user

  override def receiveCommand: Receive = {
    case AddUserCommand(name, surname) => persistAsync(s"$name-$surname") {
      event => updateState(User(generate(), name, surname))
    }
  }
}
