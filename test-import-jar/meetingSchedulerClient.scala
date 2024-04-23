import com.example.meeting.MeetingScheduler

object MeetingSchedulerClientMain extends App {
  // the api get entries return a json string, so we need to parse it
  // the api create meeting receives a json string, so we need to serialize it
  // but first we need to make sure the file is empty
  MeetingScheduler.clearFile()

  // check that no meetings exist for Alice and Bob
  var aliceMeetings = MeetingScheduler.getEntriesApiCall("Alice")
  println("should print empty list: " + aliceMeetings)
  var bobMeetings = MeetingScheduler.getEntriesApiCall("Bob")
  println("should print empty list: " + bobMeetings)

  // create a meeting for Alice and Bob
  val meeting = "{\"names\":[\"Alice\",\"Bob\"],\"meetingTime\":{\"startHour\":8,\"endHour\":9}}"
  MeetingScheduler.createMeetingApiCall(meeting)

  // check that the meeting was created
  aliceMeetings = MeetingScheduler.getEntriesApiCall("Alice")
  println("should print 1 meeting: " + aliceMeetings)
  bobMeetings = MeetingScheduler.getEntriesApiCall("Bob")
  println("should print 1 meeting: " + bobMeetings)
}