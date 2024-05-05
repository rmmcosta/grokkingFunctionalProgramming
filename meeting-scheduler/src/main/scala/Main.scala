import cats.effect.unsafe.implicits.global
import java.time.Duration
import com.example.meeting.MeetingScheduler.MeetingTime
import com.example.meeting.MeetingScheduler.createMeetingApiCall
import scala.jdk.CollectionConverters._

@main def hello(): Unit =
  println(s"casting the die ${castTheDieImpureClient}")
  println(s"casting the die pure output type ${castTheDiePureClient}")
  println(s"casting the die pure ${castTheDiePureClient.unsafeRunSync()}")
  println(
    s"casting the die pure sum ${castTheDiePureClientSum.unsafeRunSync()}"
  )
  val newMeeting = scheduleMeeting(List("Alice", "Bob", "Charlie"), Duration.ofHours(2)).unsafeRunSync()
  val printMsg = newMeeting match {
    case Some(meeting) => s"meeting scheduled: ${meeting.getStartHour()} - ${meeting.getEndHour()}"
    case None => "No meeting scheduled"
  }
  println(printMsg)
