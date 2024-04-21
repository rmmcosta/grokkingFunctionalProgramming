import com.example.meeting.MeetingScheduler;
import java.util.Arrays;
import java.util.List;

public class MeetingSchedulerClient {
    public static void main(String args[]) throws Exception {
        // the api get entries return a json string, so we need to parse it
        // the api create meeting receives a json string, so we need to serialize it
        // but first we need to make sure the file is empty
        MeetingScheduler.clearFile();

        // check that no meetings exist for Alice and Bob
        String aliceMeetings = MeetingScheduler.getEntriesApiCall("Alice");
        System.out.println("sould print empty list: " + aliceMeetings);
        String bobMeetings = MeetingScheduler.getEntriesApiCall("Bob");
        System.out.println("sould print empty list: " + bobMeetings);

        // create a meeting for Alice and Bob
        String meeting = "{\"names\":[\"Alice\",\"Bob\"],\"meetingTime\":{\"startHour\":8,\"endHour\":9}}";
        MeetingScheduler.createMeetingApiCall(meeting);

        // check that the meeting was created
        aliceMeetings = MeetingScheduler.getEntriesApiCall("Alice");
        System.out.println("sould print 1 meeting: " + aliceMeetings);
        bobMeetings = MeetingScheduler.getEntriesApiCall("Bob");
        System.out.println("sould print 1 meeting: " + bobMeetings);
    }
}