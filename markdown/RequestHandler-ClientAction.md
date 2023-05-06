# RequestHandler and ClientAction

## *RequestHandler*
> A `thread` used by Master, created per worker request/chunk. It's main role is to receive the intermediate results from
the workers and store them in a buffer called `intermediate_results`, for the reducing function.

- The variables are :
    - ObjectInputStream in -> to receive the intermediate result from the worker

- It also contains the following methods :
    - Contructor -> initializes the inputstream
    - run -> **overriding** the run method, it is starting as soon as is there a connection with the worker. It waits until it receives a chunk and add it to the Master.intermediate_results. When all the intermediate results have been received, 
    it activates the suitable ClientAction thread by finding it from the clientHandlers synchronizedHashMap with the help of the chunk key. In the end, it removes the record from the clientHandlers and the record in the intermediate_results.
---
## *ClientAction*
> A `thread` used by Master, created per client request. It's main role is to implement the interface as a backend class in between Client and Master connection. 

- The variables are :
    - ObjectOutputStream out -> used to send results & statistics to client.
    - ObjectInputStream in -> used to receive files and request ids.
    - InputStream is -> the file in inputstream state.
    - socket -> the socket defined by master connection with client.
    - userId -> the user id from client.
    - fileId -> the fileId from client.
    - num_of_wpt -> the number of waypoints per chunk. Defined by Master.
    - inputFile -> the number of client request (used in the database collections as key).
    - interResults -> store all the the intermediate results of a file.
    - lock -> works as a monitor to stop the thread from sending the chunks to the workers until *all* the intermediate results are ready and reducing is able to be executed.
    - choice -> the option of the client UI request.
    - parser -> a parser that cuts a gpx file to numerous waypoints.
    - num_of_workers -> the minimum limit of workers that must connect to execute the MapReduce operation.

- It also contains the following methods :
    - Contructor -> initializes the following variables {socket, inputFile, num_of_wpt, lock, in, out}.
    - getFileId
    - setFileId
    - receiveFile -> receiving the bytestreams from Client and convert it in a inputstream
    - concatenateByteArrays
    - create_chunk -> it creates chunk from a list of waypoints. To create a chunk and keep the sequence order between them, it stores last waypoint in the previous chunk as the first waypoint to the next chunk. After the chunks are ready, update the database of the synchronizedHashMap intermediate_results. Then, for each chunk in the list, get the outputstream of the next available worker in a roundrobin sequence to send the chunk. **Please note that in case two or more clientAction use the same output stream to send chunks to the same worker, there is a synchronized block to counter this conflict**.
    - checkBuffer -> checks if there are enough workers to execute the request comparing the size of the buffer of Master.workerHandlers and Master.num_of_Workers and sends suitable messages to clients.
    - setIds -> set fileId & userId.
    - create_user -> checks if user has already registered in the app & updates the database Master.userList.
    - setIntermResults -> initializes the interResults list and wakes the thread to continue its execution in the function SendResults.
    - reduceResults -> executes the calculations in the interResult list and returns a result.
    - sendResults -> executes after Master has send all the chunks of the same file to the workers. It waits until Master has collected all the intermediate results and updates the database of the statistics and the personal record in the Master.userlist. In the end, it send the results to the client.
    - uploadStatistics -> calculating the statistics of the user and the percentage of the substraction between the personal record and the average global statistics.Then sends them to client.
    - run ->  **overriding** the run method, it is starting as soon as is there a connection with the client. It acts as an backend interface where it can execution multiple actions, given a suitable integer.
