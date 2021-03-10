package co.mglvl.notes

import cats.effect.{ IO, IOApp}
import cats.effect.ExitCode
import co.mglvl.notes.coursera.CourseraClient
import org.http4s.client.blaze.BlazeClientBuilder
import co.mglvl.notes.readwise.ReadwiseClient
import co.mglvl.notes.readwise.Highlight
import com.monovore.decline._
import com.monovore.decline.effect._
import cats.syntax.applicative._

case class CliInput(
  courseId: String,
  courseUrl: String,
  courseTitle: String,
  courseAuthor: String,
  courseraCookie: String,
  readwiseToken: String,
)

object Main extends CommandIOApp(
  name = "coursera-notes",
  header = "Send your coursera lecture notes to readwise",
) {
    private val courseIdOpt       = Opts.option[String]("courseId", "Identifier for the course", "cid")
    private val courseUrlOpt      = Opts.option[String]("courseUrl", "Url for the course", "cu")
    private val courseTitleOpt    = Opts.option[String]("courseTitle", "Title of the course, used in the highlights titles", "ct")
    private val courseAuthorOpt   = Opts.option[String]("courseAuthor", "The professor, used as author of the highlights", "ca")
    private val courseraCookieOpt = Opts.option[String]("courseraCookie", "Your cookie, in order to retrieve the notes.", "cc")
    private val readwiseTokenOpt  = Opts.option[String]("readwiseToken", "Your readwise token", "rt")

    import cats.implicits._

    private val allOpts =
      (
        courseIdOpt,
        courseUrlOpt,
        courseTitleOpt,
        courseAuthorOpt,
        courseraCookieOpt,
        readwiseTokenOpt
      ).mapN(CliInput.apply)

    private val resources = 
        BlazeClientBuilder[IO](executionContext).resource.map { client =>
          val coursera = new CourseraClient(client)
          val readwise = new ReadwiseClient(client)
          (coursera, readwise)
        }

    override def main: Opts[IO[ExitCode]] =
      allOpts.map { input =>
        import input._
        resources.use { case (coursera, readwise) =>
          for {
            notesResponse <- coursera.getNotes(courseId, courseraCookie)
            highlights = notesResponse.elements.map(
              Highlight.fromCourseraNote(
                courseTitle = courseTitle,
                courseUrl = courseUrl,
                author = Some(courseAuthor)
              )
            )
            //.take(1)
            _ <- readwise.createHighlights(highlights, readwiseToken)
          } yield ()
          // .flatMap { highlights =>
          //   import cats.syntax.traverse.toTraverseOps 
          //   import cats.instances.list._
          //   highlights.traverse { highlight => 
          //     println(highlight.toString()) *> println("-"*10) 
          //   }
          // }
        }.as(ExitCode.Success)
      }
  }

