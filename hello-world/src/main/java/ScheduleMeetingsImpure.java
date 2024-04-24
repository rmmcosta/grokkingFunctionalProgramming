import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import MeetingScheduler.MeetingTime;

class ShedulingMeetingsImpure {
    private static final int START_HOUR = 8;
    private static final int END_HOUR = 17;

    static MeetingTime schedule(List<String> persons, Duration meetingDuration) {
        // we need to check which meetings the passed persons already have
        // and then schedule a meeting for them in a time slot where they are all
        // available
        // with the given duration
        var scheduledMeetings = new ArrayList<MeetingTime>();
        for (var person : persons) {
            var meetings = getEntriesFromFile(person);
            scheduledMeetings.addAll(meetings);
        }

        // find a time slot where all persons are available
        // first we build all the possible time slots from START_HOUR to END_HOUR
        var timeSlots = new ArrayList<MeetingTime>();
        for (int i = START_HOUR; i <= (END_HOUR - meetingDuration.toHours()); i++) {
            timeSlots.add(new MeetingTime(i, i + meetingDuration.toHours()));
        }

        // then we remove the time slots where any person is not available
        for (var meeting : scheduledMeetings) {
            timeSlots.removeIf(
                    timeSlot -> timeSlot.startHour >= meeting.startHour && timeSlot.startHour < meeting.endHour);
            timeSlots.removeIf(timeSlot -> timeSlot.endHour > meeting.startHour && timeSlot.endHour <= meeting.endHour);
        }

        // if there are no available time slots, we return null
        if (timeSlots.isEmpty()) {
            return null;
        }

        // we return the first available time slot
        MeetingScheduler.createMeetingApiCall(persons, timeSlots.get(0));
        return timeSlots.get(0);
    }
}