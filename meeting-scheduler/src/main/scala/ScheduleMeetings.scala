import com.example.meeting.MeetingScheduler.MeetingTime
import com.example.meeting.MeetingScheduler.getEntriesApiCall
import cats.effect.IO
import com.example.meeting.MeetingScheduler.createMeetingApiCall
import java.time.Duration
import scala.jdk.CollectionConverters._
import cats.effect.unsafe.implicits.global
import cats.implicits._

//create two constants for the day start working hour and end hour
val dayStart = 9
val dayEnd = 17

private def getCalendarEntries(name: String): IO[List[MeetingTime]] =
  IO.delay(getEntriesApiCall(name).asScala.toList)
    .orElse(
      IO.delay(getEntriesApiCall(name).asScala.toList)
    )
    .orElse(IO.pure(List.empty))

def createMeeting(
    persons: List[String],
    meetingTime: MeetingTime
): IO[Unit] = {
  println(s"Creating meeting for ${persons.mkString(", ")} at ${meetingTime.getStartHour()} - ${meetingTime.getEndHour()}")
  IO.delay(createMeetingApiCall(persons.asJava, meetingTime))
    .orElse(IO.delay(createMeetingApiCall(persons.asJava, meetingTime)))
    .orElse(IO.unit)
}

def scheduleMeeting(
    persons: List[String],
    duration: Duration
): IO[Option[MeetingTime]] = for {
  existingMeetings <- scheduledMeetings(persons)
  possibleSlots = possibleMeetings(
    existingMeetings,
    dayStart,
    dayEnd,
    duration
  )
  slot <- IO.delay(possibleSlots.headOption)
  _ <- slot.fold(IO.unit)(createMeeting(persons, _))
} yield slot

private def scheduledMeetings(persons: List[String]): IO[List[MeetingTime]] =
  persons.traverse(getCalendarEntries).map(_.flatten)

private def possibleMeetings(
    existingMeetings: List[MeetingTime],
    startHour: Int,
    endHour: Int,
    duration: Duration
): List[MeetingTime] = {
  val slots =
    Range(startHour, endHour, duration.toHours.toInt).toList.map(hour =>
      MeetingTime(hour, hour + duration.toHours.toInt)
    )
  slots.filter(isNotConflictingMeeting(existingMeetings, _))
}

private def isNotConflictingMeeting(
    existingMeetings: List[MeetingTime],
    slot: MeetingTime
): Boolean = existingMeetings.forall(entry =>
  slot.getStartHour() >= entry.getEndHour() || slot.getEndHour() <= entry
    .getStartHour()
)
