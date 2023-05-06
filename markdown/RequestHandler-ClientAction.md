# RequestHandler and ClientAction

## *RequestHandler*
> A `thread` used by Master, created per worker request/chunk. It's main role is to receive the intermediate results from
the workers and store them in a buffer called `intermediate_results`, for the reducing function.

- It has the following attributes :
    1. ObjectInputStream in -> to receive the intermediate result from the worker

- It also contains the following methods :
    - <a>Contructor</a> -> initializes the inputstream
    - <a>run</a> -> **overriding** the run method, it is starting as soon as is there a connection with the worker. It waits until it receives a chunk and add it to the Master.intermediate_results. When all the intermediate results have been received, 
    it activates the suitable ClientAction thread by finding it from the clientHandlers synchronizedHashMap with the help of the chunk key. In the end, it removes the record from the clientHandlers and the record in the intermediate_results.
---
## *ClientAction*
> A `thread` used by Master, created per client request. It's main role is to implement the interface as a backend class in between Client and Master connection. 

- It has the following attributes :
    1. ObjectOutputStream out -> used to send results & statistics to client.
    2. ObjectInputStream in -> used to receive files and request ids.
    3. InputStream is -> the file in inputstream state.
    4. socket -> the socket defined by master connection with client.
    5. userId -> the user id from client.
    6. fileId -> the fileId from client.
    7. num_of_wpt -> the number of waypoints per chunk. Defined by Master.
    8. requestNo -> the number of client request (used in the database collections as key).
    9. interResults -> store all the the intermediate results of a file.
    10. lock -> works as a monitor to stop the thread from sending the chunks to the workers until **all** the intermediate results are ready and reducing is able to be executed.
    11. choice -> the option of the client UI request.
    12. parser -> a parser that cuts a gpx file to numerous waypoints.
    13. num_of_workers -> the minimum limit of workers that must connect to execute the MapReduce operation.

- It also contains the following methods :
    - <a>Contructor</a> -> initializes the following variables {socket, requestNo, num_of_wpt, lock, in, out}.
    - <a>getFileId</a>
    - <a>setFileId</a>
    - <a>receiveFile</a> -> receiving the bytestreams from Client and convert it in a inputstream
    - <a>concatenateByteArrays</a>
    - <a>create_chunk</a>-> it creates chunk from a list of waypoints. To create a chunk and keep the sequence order between them, it stores last waypoint in the previous chunk as the first waypoint to the next chunk. After the chunks are ready, update the database of the synchronizedHashMap intermediate_results. Then, for each chunk in the list, get the outputstream of the next available worker in a roundrobin sequence to send the chunk. **Please note that in case two or more clientAction use the same output stream to send chunks to the same worker, there is a synchronized block to counter this conflict**.
    - <a>checkBuffer</a> -> checks if there are enough workers to execute the request comparing the size of the buffer of Master.workerHandlers and Master.num_of_Workers and sends suitable messages to clients.
    - <a>setIds</a> -> set fileId & userId.
    - <a>create_user</a> -> checks if user has already registered in the app & updates the database Master.userList.
    - <a>setIntermResults</a> -> initializes the interResults list and wakes the thread to continue its execution in the function SendResults.
    - <a>reduceResults</a> -> executes the calculations in the interResult list and returns a result.
    - <a>sendResults</a> -> executes after Master has send all the chunks of the same file to the workers. It waits until Master has collected all the intermediate results and updates the database of the statistics and the personal record in the Master.userlist. In the end, it send the results to the client.
    - <a>uploadStatistics</a> -> calculating the statistics of the user and the percentage of the substraction between the personal record and the average global statistics.Then sends them to client.
    - <a>run</a> ->  **overriding** the run method, it is starting as soon as is there a connection with the client. It acts as an backend interface where it can execution multiple actions, given a suitable integer.
