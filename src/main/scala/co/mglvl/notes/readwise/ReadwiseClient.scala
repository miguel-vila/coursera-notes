package co.mglvl.notes.readwise

import org.http4s.client.Client
import io.circe.Json
import org.http4s.syntax.literals._
import io.circe.syntax._
import io.circe.Encoder.encodeList
import cats.syntax.functor._
import org.http4s.circe.jsonDecoder
import org.http4s.circe.CirceEntityEncoder._
// import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.impl.Methods
import cats.effect.Sync
import org.http4s.Header
import io.circe.Printer

class ReadwiseClient[F[_]](client: Client[F]) extends Http4sClientDsl[F] with Methods {

    private implicit val printer = Printer.noSpaces.copy(dropNullValues = true)

    def createHighlights(highlights: List[Highlight], token: String)(implicit S: Sync[F]): F[Unit] = {
        import cats.syntax.apply._
        import cats.syntax.flatMap._
        val requestBody = Json.obj("highlights" -> highlights.asJson)
        
        S.delay(println(new String(printer.print(requestBody)))) *>
        client.fetchAs[String](
            POST(
                printer.print(requestBody), 
                uri"https://readwise.io/api/v2/highlights/", 
                Header("Authorization", s"Token $token")
            )
        ).flatMap { response =>
            S.delay(println(response)).void
        }
    }
        
}
