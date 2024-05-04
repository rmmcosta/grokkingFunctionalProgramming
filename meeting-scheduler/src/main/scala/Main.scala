import cats.effect.unsafe.implicits.global
import java.time.Duration

@main def hello(): Unit =
  println(s"casting the die ${castTheDieImpureClient}")
  println(s"casting the die pure output type ${castTheDiePureClient}")
  println(s"casting the die pure ${castTheDiePureClient.unsafeRunSync()}")
  println(
    s"casting the die pure sum ${castTheDiePureClientSum.unsafeRunSync()}"
  )
  println(s"scheduled meetings ${scheduledMeetings("Alice", "Bob").unsafeRunSync()}")
  scheduleMeeting(List("Alice", "Bob", "Charlie"), Duration.ofHours(2), true)
  println(s"scheduled meetings ${scheduledMeetings("Alice", "Bob").unsafeRunSync()}")
