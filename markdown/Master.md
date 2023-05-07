# Master
> This class functions as a `server` and contains a database with collections & variables. It bridges the gap between the Worker and Client.

- It contains the following attributes :
    1. num_of_workers -> the least number of workers.
    2. num_of_wpt -> the upper limit of waypoints in a chunk.
    3. worker_port
    4. user_port
    5. reducer_port
    6. requestNo -> the number of request, global for all clients together.
    7. statistics -> a variable as database to keep all the averages value for all users.
    8. workerHandlers -> a RobinQueue that stores all the outputstreams of the available workers.
    9. userList -> a synchronizedHashMap as a database that keeps the personal records for each user.
    10. clientHandlers -> a synchronizedHashMap as a database that keeps the clientAction threads for each client request.
    11. intermediate_results -> a synchronizedHashMap as a database that keeps a Pair of a list with it's size of each requestNo.
    12. workerSocket
    13. clientSocket
    14. reducerSocket

- It also contains the following methods :
    - <a>openServer</a> -> creates 3 different threads that are responsible for different actions and listen in 3 different ports.
    - <a>initDefault</a> -> initialize the attributes from configuration file.
    - <a>Contructor</a>
    - <a>main</a>

- There are also the following inside classes per category :
    - [Collections](SynchronizedHashMap-RobinQueue.md)
    - [Connection Threads](RequestHandler-ClientAction.md)
    - [Messages](Results-Statistics.md)
    - [Other](Pair-User-ParserGPX.md)