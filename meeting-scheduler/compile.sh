#!/bin/bash

javac -cp 'lib/*' -d . src/main/java/com/example/meeting/MeetingScheduler.java
jar cf meeting-scheduler.jar -C . com