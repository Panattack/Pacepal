# Pair, User and ParserGPX
*These classes are all being used only from the Master*

## Pair
>We created are own `generic` pair class, which we mainly use for our HashMap lists.

It has two generic variables:
1. key
2. value 

It has the following methods:
- Constructor
- Getters and Setters for the previous variables
- Equals -> we can compare two objects 

---
## User
>Each different client has a user object in Master . We use it to save for each client there statistics and infomation

It has the following variables:
1. A `Synchronized` Hash Map which we store all the results of the user
2. A id
3. The total distance
4. The total Elevation
5. The total Time
6. The average distance
7. The average elevation
8. The avrege time 

It has the following methods:
- Constructor
- Getters and Setters for the previous variables
- Update Statistics

---
## ParserGPX
>We use this class to read the gpx file and to make the list of waypoints, for each file.

It only has one method, the parse, that takes as parameter the file, extracts the information for each waypoint, and in the end it creates, for each file(thread) the list with the waypoints.




