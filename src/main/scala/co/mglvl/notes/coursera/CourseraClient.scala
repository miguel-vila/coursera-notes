package co.mglvl.notes.coursera

import org.http4s.client.Client
import org.http4s.Uri
import org.http4s.syntax.literals._
import org.http4s.{ Header, Headers, Method, Request }
import io.circe.Json
import org.http4s.circe.jsonDecoder
import io.circe.Decoder
import io.circe.generic.semiauto._
import cats.effect.ConcurrentEffect
import cats.syntax.flatMap._
import cats.MonadError
import scala.concurrent.ExecutionContext
import cats.effect.Resource

class CourseraClient[F[_]](client: Client[F]) {
  import CourseraClient._

  import cats.syntax.apply._
  def getNotes(courseId: String, cookie: String)(implicit CE: ConcurrentEffect[F]): F[RequestResponse] =
        CE.delay(println(s"Getting notes")) *>
    client.expect[Json](courseNotesRequest[F](courseId, cookie)).flatMap[RequestResponse] { json =>
      val id = json.asObject.flatMap(_.apply("id").flatMap(_.asString))
      json.as[RequestResponse].fold(
        failure =>
          MonadError[F, Throwable].raiseError(new Exception(s"Failure parsing = $failure")),
        response =>
          MonadError[F, Throwable].pure(response)
      )
    }

}

object CourseraClient {

  implicit val optionStringDecoder: Decoder[Option[String]] =
    Decoder.decodeString.map { str => if(str.nonEmpty) Some(str) else None }
 
  implicit val userNoteDetailsDefinitionDecoder: Decoder[UserNoteDetailsDefinition] = 
    deriveDecoder[UserNoteDetailsDefinition]
 
  implicit val userNoteDetailsDecoder: Decoder[UserNoteDetails] = 
    deriveDecoder[UserNoteDetails]
 
  implicit val userNoteDecoder: Decoder[UserNote] = 
    deriveDecoder[UserNote]
 
  implicit val requestResponseDecoder: Decoder[RequestResponse] =
    deriveDecoder[RequestResponse]

  def courserNotesUri(courseId: String): Uri =
    uri"https://www.coursera.org/api/userNotes.v1"
      .withQueryParam("courseId", courseId)
      .withQueryParam("start", 0)
      .withQueryParam("limit", 100)
      .withQueryParam("q", "courseWithContentSort")
      .withQueryParam("fields", "id,userText,createdAt,updatedAt,details")

      io.circe.Json

  def courseNotesRequest[F[_]](courseId: String, cookie: String): Request[F] =
    Request(
      method = Method.GET,
      uri = courserNotesUri(courseId), 
      headers = Headers.of(Header("Cookie", cookie))
    )

  def live[F[_]: ConcurrentEffect](executionContext: ExecutionContext): Resource[F, CourseraClient[F]] = {
    import org.http4s.client.blaze.BlazeClientBuilder
    BlazeClientBuilder.apply[F](executionContext).resource.map(new CourseraClient(_))
  }

}
