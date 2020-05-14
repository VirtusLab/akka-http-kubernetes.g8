package $package$.model

import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

case class GuestbookEntry(author: String, content: String)

object GuestbookEntry {
  implicit val decoder: Decoder[GuestbookEntry] = deriveDecoder
  implicit val encoder: Encoder[GuestbookEntry] = deriveEncoder
}
