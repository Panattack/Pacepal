# Chunk and Waypoint
**They are both `Serializable` and are being used by the Master and the Workers**

## *Waypoint*
> With the object of this class we describe the waypoints that the gpx file contains.

- It has the following attributes :
    1. The user's name 
    2. The longitude 
    3. The latitude
    4. The elevation
    5. The time 

- It also has the following methods:
    - <a>Constructor</a>
    - <a>Getters</a> and <a>Setters</a> for the previous variables 
    - <a>toString</a> 
---
## *Chunk* 
> Α gpx file is separated to an x amount of chunks. We use this class to show that. 

- This class has the following attributes:

    1. A list of waypoints 
    2. A number 
    3. The user name
    4. The fileId -> it shows, from the number of files the user has sent , which one is these . (for instance 6 means its from the 6th file ).
    5. The userId
    6. A key -> it shows the global request number.
    7. The total Distance of the chunk 
    8. The total Time (in Hours) of the chunk 
    9. The total Elevation of the chunk
    10. The average speed of the chunk  
    11. The total time (in seconds) of the chunk

- It has has the following methods:
    - <a>Constructor</a>
    - <a>Duplicator</a>
    - <a>Getters</a> and <a>Setters</a> for the above variables 
    - <a>Adder</a> of new waypoint to the array 
    - <a>Size</a> of the Array
    - <a>Getter</a> from the Array
    - <a>Distance calculator</a>
    - <a>calsStatistics</a> , for the remaining variables
    - <a>toString</a>
