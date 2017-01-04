package models

import akka.persistence.PersistentActor
import models.UserRepository.{AddUser, UserAdded}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
import util.UUIDGenerator.generate

import scala.collection.mutable

object UserRepository {

  sealed trait Command

  sealed trait Event

  case class AddUser(name: String, surname: String) extends Command

  case class UserAdded(id: String, name: String, surname: String) extends Event

  implicit object UserReader extends BSONDocumentReader[UserAdded] {
    def read(doc: BSONDocument): UserAdded =
      UserAdded(
        doc.getAs[String]("id").get,
        doc.getAs[String]("name").get,
        doc.getAs[String]("surname").get)
  }

  implicit object AddUserCommandWriter extends BSONDocumentWriter[UserAdded] {
    def write(user: UserAdded): BSONDocument = BSONDocument(
      "name" -> user.name,
      "surname" -> user.surname)
  }


}

class UserPersistenceActor extends PersistentActor {
  var state = mutable.ArrayBuffer.empty[UserAdded]

  override def persistenceId: String = "user-persistence-1"

  override def receiveRecover: Receive = {
    case UserAdded(id, name, surname) => updateState(UserAdded(id, name, surname))
  }

  override def receiveCommand: Receive = {
    case AddUser(name, surname) =>
      val id = generate()
      val user = UserAdded(id, name, surname)
      persistAsync(user)(updateState)
  }

  def updateState(user: UserAdded): Unit = {
    state += user
    Unit
  }
}
