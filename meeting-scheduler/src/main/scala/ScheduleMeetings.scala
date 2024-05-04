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

def getCalendarEntries(name: String): IO[List[MeetingTime]] =
  IO.delay(getEntriesApiCall(name).asScala.toList)
    .orElse(
      IO.delay(getEntriesApiCall(name).asScala.toList)
        .orElse(IO.pure(List.empty))
    )

def saveMeeting(
    names: List[String],
    meetingTime: MeetingTime,
    withRetry: Boolean = false
): IO[Unit] =
  IO.delay(createMeetingApiCall(names.asJava, meetingTime))
    .orElse(IO.delay(createMeetingApiCall(names.asJava, meetingTime)))
    .orElse(IO.unit)

def executeGetCalendarEntries(
    name: String,
    withRetry: Boolean = false
): List[MeetingTime] = {
  // if withRetry is true we will retry the getCalendarEntries function 3 times if an exception is raised
  if (withRetry) {
    var retries = 3
    while (retries > 0) {
      try {
        return getCalendarEntries(name).unsafeRunSync()
      } catch {
        case e: Exception => retries -= 1
      }
    }
    throw new RuntimeException("Exceeded maximum retries")
  } else getCalendarEntries(name).unsafeRunSync()
}

def executeSaveMeeting(
    names: List[String],
    meetingTime: MeetingTime,
    withRetry: Boolean = false
): Unit = {
  // if withRetry is true we will retry the saveMeeting function 3 times if an exception is raised
  if (withRetry) {
    var retries = 3
    while (retries > 0) {
      try {
        saveMeeting(names, meetingTime).unsafeRunSync()
        return
      } catch {
        case e: Exception => retries -= 1
      }
    }
    throw new RuntimeException("Exceeded maximum retries")
  } else saveMeeting(names, meetingTime).unsafeRunSync()
}

def scheduleMeeting(
    names: List[String],
    duration: Duration,
    withRetry: Boolean = false
): Unit = {
  // generate slots with duration from day start to day end not conflicting with existing meetings for each name
  val possibleSlots = (dayStart until dayEnd).flatMap(hour => {
    val start = hour
    val end = hour + duration.toHours.toInt
    if (end <= dayEnd) Some(MeetingTime(start, end)) else None
  })
  // free slots can be calculated by removing conflicting slots from possible slots
  // conflicting slots are slots whose start hour or end hour is between existing calendar entries start and end hours
  val freeSlots = names.foldLeft(possibleSlots)((slots, name) => {
    val calendarEntries = executeGetCalendarEntries(name, withRetry)
    slots.filter(!isAConflictingMeeting(calendarEntries, _))
  })
  // save the first available slot for all names
  if (!freeSlots.isEmpty)
    executeSaveMeeting(names, freeSlots.head, withRetry)
}

def scheduledMeetings(person1: String, person2: String): IO[List[MeetingTime]] =
  for {
    person1Entries <- getCalendarEntries(person1)
    person2Entries <- getCalendarEntries(person2)
  } yield person1Entries.appendedAll(person2Entries)

def possibleMeetings(
    existingMeetings: List[MeetingTime],
    startHour: Int,
    endHour: Int,
    duration: Duration
): List[MeetingTime] = {
  val slots =
    Range(startHour, endHour, duration.toHours.toInt).toList.map(hour =>
      MeetingTime(hour, hour + duration.toHours.toInt)
    )
  slots.filter(!isAConflictingMeeting(existingMeetings, _))
}

def isAConflictingMeeting(
    existingMeetings: List[MeetingTime],
    slot: MeetingTime
): Boolean = {
  existingMeetings.forall(entry =>
    (slot.getStartHour() >= entry.getStartHour() && slot.getStartHour() < entry
      .getEndHour()) || (slot.getEndHour() > entry.getStartHour() && slot
      .getEndHour() <= entry.getEndHour()) || (slot.getStartHour() <= entry
      .getStartHour() && slot.getEndHour() >= entry.getEndHour())
  )
}
