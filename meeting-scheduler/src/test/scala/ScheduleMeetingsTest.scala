import munit.FunSuite
import java.time.Duration
import com.example.meeting.MeetingScheduler.MeetingTime

class ScheduleMeetingsTest extends FunSuite {

  test(
    "scheduleMeetingWithRetry should save the first available slot for all names"
  ) {
    val names = List("Alice", "Bob", "Charlie")
    val duration = Duration.ofHours(2)

    def getCalendarEntries(name: String): List[MeetingTime] = {
      name match {
        case "Alice"   => List(MeetingTime(9, 11), MeetingTime(14, 16))
        case "Bob"     => List(MeetingTime(10, 12), MeetingTime(13, 15))
        case "Charlie" => List(MeetingTime(11, 13), MeetingTime(16, 18))
        case _         => List.empty
      }
    }

    def saveMeeting(names: List[String], slot: List[MeetingTime]): Unit = {
      assertEquals(slot, List(MeetingTime(12, 14)))
    }

    scheduleMeeting(names, duration, true)
  }

  test(
    "scheduleMeetingWithRetry should not save a slot when there are no available slots"
  ) {
    val names = List("Alice", "Bob", "Charlie")
    val duration = Duration.ofHours(2)

    def getCalendarEntries(name: String): List[MeetingTime] = {
      List(
        MeetingTime(9, 11),
        MeetingTime(11, 13),
        MeetingTime(13, 15),
        MeetingTime(15, 17)
      )
    }

    def saveMeeting(names: List[String], slot: List[MeetingTime]): Unit = {
      fail("saveMeeting should not be called")
    }

    scheduleMeeting(names, duration, true)
  }

  test(
    "scheduleMeetingWithRetry should not save a slot when the names list is empty"
  ) {
    val names = List.empty[String]
    val duration = Duration.ofHours(2)

    def saveMeeting(names: List[String], slot: List[MeetingTime]): Unit = {
      fail("saveMeeting should not be called")
    }

    scheduleMeeting(names, duration, true)
  }

  test(
    "scheduleMeetingWithRetry should not save a slot when the duration is zero"
  ) {
    val names = List("Alice", "Bob", "Charlie")
    val duration = Duration.ZERO

    def saveMeeting(names: List[String], slot: List[MeetingTime]): Unit = {
      fail("saveMeeting should not be called")
    }

    scheduleMeeting(names, duration, true)
  }

  test(
    "scheduleMeetingWithRetry should not save a slot when the duration is longer than the working day"
  ) {
    val names = List("Alice", "Bob", "Charlie")
    val duration = Duration.ofHours(9)

    def saveMeeting(names: List[String], slot: List[MeetingTime]): Unit = {
      fail("saveMeeting should not be called")
    }

    scheduleMeeting(names, duration, true)
  }
}
