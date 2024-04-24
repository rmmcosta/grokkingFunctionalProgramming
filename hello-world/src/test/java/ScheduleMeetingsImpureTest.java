import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleMeetingsImpureTest {

    @Test
    void testSchedule() {
        // Test case 1: No scheduled meetings, should return the first available time slot
        List<String> persons1 = Arrays.asList("Alice", "Bob", "Charlie");
        Duration meetingDuration1 = Duration.ofHours(1);
        MeetingTime expected1 = new MeetingTime(8, 9);
        MeetingTime actual1 = ScheduleMeetingsImpure.schedule(persons1, meetingDuration1);
        assertEquals(expected1, actual1);

        // Test case 2: Some scheduled meetings, should return the first available time slot
        List<String> persons2 = Arrays.asList("Alice", "Bob", "Charlie");
        Duration meetingDuration2 = Duration.ofHours(2);
        MeetingTime expected2 = new MeetingTime(10, 12);
        MeetingTime actual2 = ScheduleMeetingsImpure.schedule(persons2, meetingDuration2);
        assertEquals(expected2, actual2);

        // Test case 3: All time slots are occupied, should return null
        List<String> persons3 = Arrays.asList("Alice", "Bob", "Charlie");
        Duration meetingDuration3 = Duration.ofHours(1);
        MeetingTime actual3 = ScheduleMeetingsImpure.schedule(persons3, meetingDuration3);
        assertNull(actual3);

        // Test case 4: Empty list of persons, should return null
        List<String> persons4 = new ArrayList<>();
        Duration meetingDuration4 = Duration.ofHours(1);
        MeetingTime actual4 = ScheduleMeetingsImpure.schedule(persons4, meetingDuration4);
        assertNull(actual4);
    }
}