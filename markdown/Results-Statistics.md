# Results and Statistics
**Both classes are Serializable and are messages of Master , by request of the Client .**
## *Results*
This class is mainly being used by the Master, to send the results to the Client. Obviously its Serializable , and it contains 6 variables.

- The results that the user wants to see :
    1. Total Distance
    2. Average Speed
    3. Total Elevation 
    4. Total Time  

- And two more that we need for the identification of the object:

    5. gpx_id -> so we can know the results from which gpx file it was originated
    6. user_id -> so we know for whom the results are for 


 It also has the following methods:
- Constructor 
- Getters and Setters for the above variables
- to String 
---
## *Statistics* 
>This class is has two different uses. First , as an object that the client gets its results from the master . Secondly , it hold the global values of the users .

It contains 8 variables:

- The average variables of all the users
    1. Global Average Time 
    2. Global Average Distance 
    3. Global Average Elevation
    4. Global Size 
    5. pre Size 

- The statistics of a user
    1. Distance
    2. Time 
    3. Elevation 

It also contains the following methods:
- 2 different <a>Constructors</a>, depending on the usage of the object
- Synchronized <a>Getters</a> and <a>Setters</a> for the previous variables
- <a>defUs</a> -> updates the users variables 
- <a>updateValues</a> -> updates the global variables
- <a>toString</a>
