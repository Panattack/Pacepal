# Pacepal


## Introduction
This project is part of the subject *"Distributed Systems"* in Athens University of Economics & Business. It is organized into two sub projects, the [backend](backend\README.md) and [frontend] code.

## Description
<p> Recent years have seen a big increase in exercise recording apps
(activity), activity tracker. These systems usually consist of a
Mobile frontend application for recording activity and a backend system
which undertakes the analysis of the data. At the same time, many of these
systems enable social networking between their users with
features such as displaying leaderboards with the best runners per route,
comparison with the average and others. Known services available online
is e.g. Strava and MapMyRun. As part of the course work you are invited to
Create such a simple data analysis system from activity tracking.

<p> These systems have the ability to serve a large number of users. This
means that each user maintains a personal "profile", that is, a space where
can add their activities, which they can then analyze through the
application and be compared to other users in similar activities. Also
can see their overall stats, such as number of activities, total
distance, total exercise time, etc. Each user can either record and
upload his/her activity at that time through the mobile application,
or upload an activity previously recorded. A
activity is a sequence of GPS waypoints. A waypoint is characterized by
its coordinates (latitude, longitude), altitude (elevation) and time of recording
his. The sequence of these waypoints is stored in a special XML file format that
is called GPX.

<p> Users through the mobile app upload this GPX file to the system and the
system performs some processing on this file. A GPX file contains only
an activity/route.

<p> Usually waypoints are generated per x meters and this depends on its accuracy
GPS of the device. therefore the file size grows in proportion to the total
distance covered during exercise. Editing the file can
be done in parallel by many machines in the form of MapReduce to speed up the
processing large files;

<p> The MapReduce framework is a programming model that enables
parallel processing of large amounts of data.

MapReduce is based in two functions:
- map(key,value) -> [(key2, value2)]
    - "Map" function: processes a key/value pair and produces an intermediate
    key/value pair. The input to the map function can be lines of a
    file, etc., and have the format (key, value). The map function converts every
    such pair in another pair (key2, value2). The map function can be
    runs in parallel, on different inputs and on different
    Nodes. The degree of parallelism depends on the application and can
    set by the user.

- reduce(key2,[value2]) -> [final_value]
    - "Reduce" function: merges all intermediate values related to the same
    key and produces the final results. For each separate key is created
    A list of the values that correspond to this key. This function
    Calculates a final value for the key by processing the list of values that
    correspond to this key. The reduce function is processed after
    All map functions have finished processing.

<p> The mobile application initially sends GPX to a Master Node. Then the Master
Node creates chunks from n waypoints and sends them to Worker Nodes in order
Round Robin. Each Worker calculates for the chunk he receives the total
distance, average speed, total climb and total time. In
He then turns these intermediate results back to the Master so that they become reduced to
end result. In this specific version, Master is also the Reducer.

<p> When the Master receives all the intermediate results and completes the reduce, he takes out the
Final results for the activity. Finally, asynchronously promotes results back to
mobile application for the user to see.

<p> At the same time, the Master must keep statistics from all users. Specifically
maintains the Average Exercise Time, Average Distance and Average Ascent for both
each user individually, as well as for all users.

## Backend implementation requirements:
- The Master must be implemented in Java and implement TCP Server. Not
allow the use of out-of-the-box libraries beyond the default Java ServerSocket, or
HTTP protocol using ready-made server such as Java or Apache.
- The Master must be multithreaded and be able to serve many
users at the same time and communicate simultaneously with employees.
- Workers must be implemented in Java and multithreaded to
perform many requests from the Master in parallel.
- Workers should be defined dynamically during the initialization of the Master (by
arguments or config file) and their number could be arbitrary.
- Master/Worker communication should also be implemented exclusively via TCP
sockets. Specifically, Workers must open a Socket with the Master.
Therefore, TCP Server will implement only the Master. You can implement two
different TCP Servers in Master, listening on different ports. One for
communicate with the different users who use the Application
and one for internal communication with Workers.
- There must be synchronization at the points you deem necessary. The
synchronization must be done exclusively using techniques
Synchronized, wait - notify and not by using ready-made library tools
java.util.concurrent or other out-of-the-box tools.
- It is not required to use files to store statistics and profiles of
Users. All data structures can be stored in memory
of the Master. The use of a database is prohibited

## Frontend implementation requirements:
You will develop an application that will run on Android devices and will
is an interface for the system. Through it, the user:
- He will be able to select a GPX file, saved on his device and make it
send to the backend for processing asynchronously.
- You should be able to receive notification from the Master that the processing
finished and can show the results of editing the GPX
(total distance, average speed, total climb and total
time).
- They will also be able to see their personal statistics (Total Exercise Time,
Total Distance and Total Ascent) that he has done and project with
graphic way their difference from the general average (e.g. has run overall
24% more than average).
- The communication of the Application with the Master should be done exclusively with the
use TCP Sockets. The Application must connect to TCP Socket in the
Master. It is through this socket that GPX is sent. The Master sends back
the results of processing through the same Socket which remains
open until they are taken. This process should be implemented by
the use of Threads, so that the application remains interactive until
obtained the results.

### Bonus (+20%)
In many apps, such as Strava, users can set a sequence of
Waypoints as a Segment, e.g. a small piece of the Classic path
Athens Marathon. Then each time the system detects a sub-sequence
from Waypoints identified with a Segment can and keeps the previous ones
User statistics for this piece. At the same time, it maintains a leaderboard with
performance of all users in descending order. You are invited to include this additional
operation on the system by modifying MapReduce appropriately.
Finally, the android application should also come out graphically, eg. table, the
leaderboard for the selected segment.

**Note**: Two devices in the same place may read slightly different
Coordinates. e.g. show a difference of 5m. This is called GPS Drift.

Useful Links:
1. https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html
2. Android. URL : http://code.google.com/android/
3. Android SDK: http://developer.android.com/sdk/index.html
4. Android Studio http://developer.android.com/sdk/index.html
5. GPX Generator https://www.gpxgenerator.com/

## Contributors
- Lydia - Christina Wallace 
- Georgia Petsa
- Panagiotis Triantafillidis