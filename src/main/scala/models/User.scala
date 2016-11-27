package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}
import spray.json.DefaultJsonProtocol
import util.UUIDGenerator.generate

final case class User(_id: String, name: String, surname: String)

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

object StubUser {
  def apply() : User = User(generate(), "some-name", "some-surname")
  def apply(id :String) : User = User(id, "some-name", "some-surname")
}

