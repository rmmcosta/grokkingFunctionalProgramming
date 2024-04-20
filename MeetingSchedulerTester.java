import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MeetingSchedulerTester {
    public static void main(String[] args) throws IOException {
        //clear the file if it exists
        MeetingScheduler.clearFile();
        // check that no meetings exist for Alice and Bob
        List<MeetingScheduler.MeetingTime> aliceMeetings = MeetingScheduler.getEntriesApiCall("Alice");
        System.out.println("sould print empty list: " + aliceMeetings);
        List<MeetingScheduler.MeetingTime> bobMeetings = MeetingScheduler.getEntriesApiCall("Bob");
        System.out.println("sould print empty list: " + bobMeetings);

        // create a meeting for Alice and Bob
        MeetingScheduler.createMeetingApiCall(Arrays.asList("Alice", "Bob"),
                new MeetingScheduler.MeetingTime(8, 9));
        // check that the meeting was created
        aliceMeetings = MeetingScheduler.getEntriesApiCall("Alice");
        System.out.println("sould print 1 meeting: " + aliceMeetings);
        bobMeetings = MeetingScheduler.getEntriesApiCall("Bob");
        System.out.println("sould print 1 meeting: " + bobMeetings);

        // now create a meeting for Alice and Bob that overlaps with the previous one
        try {
            MeetingScheduler.createMeetingApiCall(Arrays.asList("Alice", "Bob"),
                    new MeetingScheduler.MeetingTime(8, 9));
        } catch (RuntimeException e) {
            System.out.println("sould print Meeting time is not available for all persons: " + e.getMessage());
        }

        // create a meeting for Alice and Bob that does not overlap with the previous
        // one
        MeetingScheduler.createMeetingApiCall(Arrays.asList("Alice", "Bob"),
                new MeetingScheduler.MeetingTime(10, 11));
        // check that the meeting was created
        aliceMeetings = MeetingScheduler.getEntriesApiCall("Alice");
        System.out.println("sould print 2 meetings: " + aliceMeetings);
        bobMeetings = MeetingScheduler.getEntriesApiCall("Bob");
        System.out.println("sould print 2 meetings: " + bobMeetings);

        // create a meeting for Alice and other person
        MeetingScheduler.createMeetingApiCall(Arrays.asList("Alice", "Charlie"),
                new MeetingScheduler.MeetingTime(12, 13));
        // check that the meeting was created
        aliceMeetings = MeetingScheduler.getEntriesApiCall("Alice");
        System.out.println("sould print 3 meetings: " + aliceMeetings);
        List<MeetingScheduler.MeetingTime> charlieMeetings = MeetingScheduler.getEntriesApiCall("Charlie");
        System.out.println("sould print 1 meeting: " + charlieMeetings);
    }
}
