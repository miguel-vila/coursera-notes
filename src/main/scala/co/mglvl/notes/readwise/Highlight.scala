package co.mglvl.notes.readwise

import enumeratum._
import java.time.Instant
import io.circe.Encoder
import io.circe.generic.extras.semiauto._
import co.mglvl.notes.coursera.UserNote
import io.circe.generic.extras.Configuration
import enumeratum.EnumEntry.Camelcase
import enumeratum.EnumEntry.LowerCamelcase
import enumeratum.EnumEntry.Snakecase
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

final case class Highlight(
    text: String,
    title: Option[String],
    author: Option[String],
    imageUrl: Option[String],
    sourceUrl: Option[String],
    sourceType: Option[SourceType],
    note: Option[String],
    location: Option[Int],
    locationType: Option[LocationType],
    highlightedAt: Option[Date],
    highlightUrl: Option[String]
)

object Highlight {

    private implicit val config = Configuration.default.withSnakeCaseMemberNames

    implicit val instantEncoder: Encoder[Date] = {
        // val df = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.from(ZoneOffset.UTC))

        val tz = TimeZone.getTimeZone("UTC")
        val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00") // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz)
        Encoder.encodeString.contramap(df.format(_).replace('T','+'))
    }
    implicit val highlightEncoder: Encoder[Highlight] = deriveConfiguredEncoder[Highlight]

    def fromCourseraNote(courseTitle: String, courseUrl: String, author: Option[String])(courseraNote: UserNote): Highlight = {
        import courseraNote.details.definition._
        val noteStart = (noteStartTs / 1000).toInt
        val url = lectureUrl(courseUrl, itemId)
        Highlight(
            text = transcriptText,
            title = Some(s"$courseTitle - $itemName"),
            author = author,
            imageUrl = None,
            sourceUrl = Some(url),
            sourceType = None,
            note = courseraNote.userText,
            location = Some(noteStart),
            locationType = Some(LocationType.TimeOffset),
            highlightedAt = Some(Date.from(Instant.ofEpochMilli(courseraNote.createdAt))),
            highlightUrl = Some(s"$url?t=${noteStart.toString()}")
        )
    }

    private def lectureUrl(courseUrl: String, itemId: String): String = {
        val prefix = if(courseUrl.endsWith("/")){ courseUrl } else{ s"$courseUrl/" }
        s"${prefix}lecture/$itemId"
    }

    private def highlightUrl(courseUrl: String, itemId: String, noteStart: Int): String = {
        val prefix = if(courseUrl.endsWith("/")){ courseUrl } else{ s"$courseUrl/" }
        s"${prefix}lecture/${itemId}?t=${noteStart.toString()}"
    }
}

sealed trait SourceType extends EnumEntry with Snakecase

object SourceType extends Enum[SourceType] with CirceEnum[SourceType] {

    val values = findValues

    case object Book extends SourceType
    case object Article extends SourceType
    case object Podcast extends SourceType

}

sealed trait LocationType extends EnumEntry with Snakecase

object LocationType extends Enum[LocationType] with CirceEnum[LocationType] {

    val values = findValues

    case object Page extends LocationType
    case object Order extends LocationType
    case object TimeOffset extends LocationType
}