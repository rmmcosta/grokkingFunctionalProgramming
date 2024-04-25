import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

import com.example.meeting.MeetingScheduler.MeetingTime;
import com.example.meeting.MeetingScheduler;

import static com.example.meeting.MeetingScheduler.getEntriesFromFile;
import static com.example.meeting.MeetingScheduler.createMeetingApiCall;
import static com.example.meeting.MeetingScheduler.MeetingTime;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleMeetingsImpureTest {

    @Test
    public void testNoScheduledMeetings() throws IOException {
        List<String> persons = Arrays.asList("Alice", "Bob", "Charlie");
        Duration meetingDuration = Duration.ofHours(1);
        MeetingTime expected = new MeetingTime(8, 9);
        MeetingTime actual = ScheduleMeetingsImpure.schedule(persons, meetingDuration);
        assertEquals(expected, actual);
    }

    @Test
    public void testSomeScheduledMeetings() throws IOException {
        List<String> persons = Arrays.asList("Alice", "Bob", "Charlie");
        Duration meetingDuration = Duration.ofHours(2);
        MeetingTime expected = new MeetingTime(10, 12);
        MeetingTime actual = ScheduleMeetingsImpure.schedule(persons, meetingDuration);
        assertEquals(expected, actual);
    }

    @Test
    public void testAllTimeSlotsOccupied() throws IOException {
        List<String> persons = Arrays.asList("Alice", "Bob", "Charlie");
        Duration meetingDuration = Duration.ofHours(1);
        MeetingTime actual = ScheduleMeetingsImpure.schedule(persons, meetingDuration);
        assertNull(actual);
    }

    @Test
    public void testEmptyListOfPersons() throws IOException {
        List<String> persons = new ArrayList<>();
        Duration meetingDuration = Duration.ofHours(1);
        MeetingTime actual = ScheduleMeetingsImpure.schedule(persons, meetingDuration);
        assertNull(actual);
    }

    @Test
    public void testScheduleMultipleTimes() {
        List<String> persons = Arrays.asList("Alice", "Bob", "Charlie");
        Duration meetingDuration = Duration.ofHours(1);
        assertThrows(IOException.class, () -> {
            for (int i = 0; i < 10; i++) {
                ScheduleMeetingsImpure.schedule(persons, meetingDuration);
            }
        });
    }
}