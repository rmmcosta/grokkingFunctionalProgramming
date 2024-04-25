package com.example.meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MeetingScheduler {
    private final static String MEETINGS_FILE = "./meetings.json";
    private final static boolean ENABLE_EXCEPTIONS = true;

    public static void createMeetingApiCall(List<String> names, MeetingTime meetingTime) throws IOException {
        if (ENABLE_EXCEPTIONS) {
            Random rand = new Random();
            if (rand.nextFloat() < 0.25)
                throw new RuntimeException("Connection error");
        }
        saveMeetingToFile(names, meetingTime);
    }

    public static List<MeetingTime> getEntriesApiCall(String name) throws IOException {
        if (ENABLE_EXCEPTIONS) {
            Random rand = new Random();
            if (rand.nextFloat() < 0.25)
                throw new RuntimeException("Unable to read meetings information");
        }
        return getEntriesFromFile(name);
    }

    public static void saveMeetingToFile(List<String> names, MeetingScheduler.MeetingTime meetingTime)
            throws IOException {
        // check if the required meeting time is available, i.e. no other meeting is
        // scheduled at that time for those names
        Boolean allPersonsAreAvailable = true;
        for (var name : names) {
            List<MeetingTime> meetings = getEntriesFromFile(name);
            for (var meeting : meetings) {
                if (meeting.startHour >= meetingTime.startHour && meeting.startHour <= meetingTime.endHour) {
                    allPersonsAreAvailable = false;
                    break;
                }
                if (meeting.endHour >= meetingTime.startHour && meeting.endHour <= meetingTime.endHour) {
                    allPersonsAreAvailable = false;
                    break;
                }
            }
        }
        if (!allPersonsAreAvailable) {
            throw new RuntimeException("Meeting time is not available for all persons");
        }
        // append to file this new meeting
        MeetingScheduler.Meeting newMeeting = new MeetingScheduler.Meeting(names, meetingTime);
        appendMeetingToFile(newMeeting);
    }

    public static List<MeetingTime> getEntriesFromFile(String name) throws IOException {
        String filePath = MEETINGS_FILE;
        if (!Files.exists(Paths.get(filePath))) {
            return new ArrayList<>();
        }
        // if the file is empty, return an empty list
        if (Files.size(Paths.get(filePath)) == 0) {
            return new ArrayList<>();
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Meeting> meetings = mapper.readValue(new File(filePath), new TypeReference<List<Meeting>>() {
            });
            return meetings.stream()
                    .filter(meeting -> meeting.names.contains(name))
                    .map(meeting -> meeting.meetingTime)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read meetings information", e);
        }
    }

    private static void appendMeetingToFile(Meeting newMeeting) {
        String filePath = MEETINGS_FILE;
        ObjectMapper mapper = new ObjectMapper();
        // create the file if it does not exist
        if (!Files.exists(Paths.get(filePath))) {
            try {
                Files.createFile(Paths.get(filePath));
            } catch (IOException e) {
                throw new RuntimeException("Unable to create meetings file", e);
            }
        }

        try {
            List<Meeting> meetings = new ArrayList<>();
            // Read the existing meetings from the file if the file is not empty
            if (Files.size(Paths.get(filePath)) > 0)
                meetings = mapper.readValue(new File(filePath), new TypeReference<List<Meeting>>() {
                });

            // Add the new meeting to the list
            meetings.add(newMeeting);

            // Write the updated list back to the file
            mapper.writeValue(new File(filePath), meetings);
        } catch (IOException e) {
            throw new RuntimeException("Unable to update meetings information", e);
        }
    }

    public static class MeetingTime {
        private int startHour;

        public int getStartHour() {
            return startHour;
        }

        private int endHour;

        public int getEndHour() {
            return endHour;
        }

        public MeetingTime(int startHour, int endHour) {
            this.startHour = startHour;
            this.endHour = endHour;
        }

        public MeetingTime() {
        }
    }

    public static class Meeting {
        private List<String> names;

        public List<String> getNames() {
            return names;
        }

        private MeetingTime meetingTime;

        public MeetingTime getMeetingTime() {
            return meetingTime;
        }

        public Meeting(List<String> names, MeetingTime meetingTime) {
            this.names = names;
            this.meetingTime = meetingTime;
        }

        public Meeting() {
        }
    }

    public static void clearFile() {
        // clear the file if it exists
        String filePath = MEETINGS_FILE;
        if (Files.exists(Paths.get(filePath))) {
            try {
                Files.delete(Paths.get(filePath));
            } catch (IOException e) {
                throw new RuntimeException("Unable to delete meetings file", e);
            }
        }
    }
}
