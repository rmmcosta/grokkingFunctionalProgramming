package com.example.meeting;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MeetingSchedulerTester {
    public static void main(String[] args) throws IOException {
        // clear the file if it exists
        MeetingScheduler.clearFile();
        // create an object mapper to deserialize and serialize json
        ObjectMapper objectMapper = new ObjectMapper();
        // check that no meetings exist for Alice and Bob, returns a json string that
        // can be parsed to MeetingTime
        String aliceMeetingsJson = getEntriesApiCall("Alice");
        System.out.println("sould print empty json: " + aliceMeetingsJson);
        List<MeetingScheduler.MeetingTime> aliceMeetings = Arrays
                .asList(objectMapper.readValue(aliceMeetingsJson, MeetingScheduler.MeetingTime[].class));
        System.out.println("sould print empty list: " + aliceMeetings);
        String bobMeetingsJson = getEntriesApiCall("Bob");
        System.out.println("sould print empty json: " + bobMeetingsJson);
        {
        }
        List<MeetingScheduler.MeetingTime> bobMeetings = Arrays
                .asList(objectMapper.readValue(bobMeetingsJson, MeetingScheduler.MeetingTime[].class));
        System.out.println("sould print empty list: " + bobMeetings);

        // create a meeting for Alice and Bob
        String meeting = "{\"names\":[\"Alice\",\"Bob\"],\"meetingTime\":{\"startHour\":8,\"endHour\":9}}";
        createMeetingApiCall(meeting);
        // check that the meeting was created
        aliceMeetingsJson = getEntriesApiCall("Alice");
        aliceMeetings = Arrays.asList(objectMapper.readValue(aliceMeetingsJson, MeetingScheduler.MeetingTime[].class));
        System.out.println("sould print 1 meeting: " + aliceMeetings);
        bobMeetingsJson = getEntriesApiCall("Bob");
        bobMeetings = Arrays.asList(objectMapper.readValue(bobMeetingsJson, MeetingScheduler.MeetingTime[].class));
        System.out.println("sould print 1 meeting: " + bobMeetings);

        // now create a meeting for Alice and Bob that overlaps with the previous one
        try {
            meeting = "{\"names\":[\"Alice\",\"Bob\"],\"meetingTime\":{\"startHour\":8,\"endHour\":9}}";
            createMeetingApiCall(meeting);
        } catch (RuntimeException e) {
            System.out.println("sould print Meeting time is not available for all persons: " + e.getMessage());
        }

        // create a meeting for Alice and Bob that does not overlap with the previous
        // one
        meeting = "{\"names\":[\"Alice\",\"Bob\"],\"meetingTime\":{\"startHour\":10,\"endHour\":11}}";
        createMeetingApiCall(meeting);
        // check that the meeting was created
        aliceMeetingsJson = getEntriesApiCall("Alice");
        aliceMeetings = Arrays.asList(objectMapper.readValue(aliceMeetingsJson, MeetingScheduler.MeetingTime[].class));
        System.out.println("sould print 2 meetings: " + aliceMeetings);
        bobMeetingsJson = getEntriesApiCall("Bob");
        bobMeetings = Arrays.asList(objectMapper.readValue(bobMeetingsJson, MeetingScheduler.MeetingTime[].class));
        System.out.println("sould print 2 meetings: " + bobMeetings);

        // create a meeting for Alice and other person
        meeting = "{\"names\":[\"Alice\",\"Charlie\"],\"meetingTime\":{\"startHour\":12,\"endHour\":13}}";
        createMeetingApiCall(meeting);
        // check that the meeting was created
        aliceMeetingsJson = getEntriesApiCall("Alice");
        aliceMeetings = Arrays.asList(objectMapper.readValue(aliceMeetingsJson, MeetingScheduler.MeetingTime[].class));
        System.out.println("sould print 3 meetings: " + aliceMeetings);
        String charlieMeetingsJson = getEntriesApiCall("Charlie");
        List<MeetingScheduler.MeetingTime> charlieMeetings = Arrays
                .asList(objectMapper.readValue(charlieMeetingsJson, MeetingScheduler.MeetingTime[].class));
        System.out.println("sould print 1 meeting: " + charlieMeetings);
    }

    private static String getEntriesApiCall(String string) throws IOException {
        // we will use this function as a wrapper for the
        // MeetingScheduler.getEntriesApiCall
        // here we will try a few times to get the entries, if we get an exception we
        // will retry
        // if we get an exception after the retries, we will throw the exception
        final int MAX_RETRIES = 4; // since we know that we have a probability of 25% of getting an exception
        try {
            for (int i = 0; i < MAX_RETRIES; i++) {
                try {
                    return MeetingScheduler.getEntriesApiCall(string);
                } catch (RuntimeException e) {
                    System.out.println("Retrying to get entries for " + string);
                }
            }
            return MeetingScheduler.getEntriesApiCall(string);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private static void createMeetingApiCall(String meeting) throws IOException {
        // we will use this function as a wrapper for the
        // MeetingScheduler.createMeetingApiCall
        // here we will try a few times to create the meeting, if we get an exception we
        // will retry
        // if we get an exception after the retries, we will throw the exception
        final int MAX_RETRIES = 4; // since we know that we have a probability of 25% of getting an exception
        try {
            for (int i = 0; i < MAX_RETRIES; i++) {
                try {
                    MeetingScheduler.createMeetingApiCall(meeting);
                    return;
                } catch (RuntimeException e) {
                    System.out.println("Retrying to create meeting");
                }
            }
            MeetingScheduler.createMeetingApiCall(meeting);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
