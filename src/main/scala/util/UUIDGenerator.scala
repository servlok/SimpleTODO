package util

import java.util.UUID

object UUIDGenerator {

  def generate() : String = UUID.randomUUID().toString

  def generate(seed : String): String = UUID.nameUUIDFromBytes(seed.getBytes).toString

}
