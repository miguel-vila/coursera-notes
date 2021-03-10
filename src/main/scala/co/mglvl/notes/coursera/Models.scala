package co.mglvl.notes.coursera

final case class RequestResponse(
  elements: List[UserNote]
)

final case class UserNote(
  createdAt: Long,
  userText: Option[String], //todo make it be none if empty str
  details: UserNoteDetails
)

final case class UserNoteDetails(
  definition: UserNoteDetailsDefinition
)

final case class UserNoteDetailsDefinition(
  itemId: String,
  itemName: String,
  noteStartTs: Long,
  transcriptText: String
)
