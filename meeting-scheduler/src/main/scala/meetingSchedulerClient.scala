import MeetingScheduler._
import MeetingScheduler.MeetingTime
import java.util.ArrayList
import java.util.Arrays

object Main extends App {
//  createMeetingApiCall
//  getEntriesApiCall
//  clearFile
  clearFile()
  println(s"should print empty list: ${getEntriesApiCall("Ricardo")}")
  println(s"should print empty list: ${getEntriesApiCall("Ana")}")
  val scalaList = List("Ricardo", "Ana")
  val javaList = new ArrayList(Arrays.asList(scalaList: _*))
  createMeetingApiCall(javaList, new MeetingTime(10, 12))
  println(
    s"should print List(Meeting(List(Ricardo, Ana),MeetingTime(10,12))) : ${getEntriesApiCall("Ricardo")}"
  )
  println(
    s"should print List(Meeting(List(Ricardo, Ana),MeetingTime(10,12))) : ${getEntriesApiCall("Ana")}"
  )
}
