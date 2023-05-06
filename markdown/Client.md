# Client
> With this class we represent each android user. It also extends the class `Thread` .

~It has the following attributes for **every** Client :
1. fileName -> the path for the file that we write the results
2. serverPort
3. path -> the path for the gpx files 
4. host -> the IP address of the master server
5. userId -> we only have one client, so we have the same userId
6. indexFile-> the number of files we have sent
7. writter -> an BufferedWriter, we only need one 


~It has the following attributes for **each** Client:
1. gpx -> the name of the gpx file
2. out-> an ObjectOutputStream 
3. in -> ObjectInputStream 
4. lock_msg -> a lock we use to see if we send a file ??
5. fileId -> the number of the file the client send(5 equals to the 5th)

It has the following methods:
- <a> Constructor </a>
- <a> initDefault </a> -> it initializes the global variables from the configuration file 
- <a> uiGpx </a> -> we ask from the client to type the file it wants to send , and it <u> creates</u> and <u> starts</u> the client thread for the specific file. It then asks if a the cleint wants to send another file.The client answers with a **y** or **n**
- <a> uiResults </a> -> it reads the file that contains the results and prints them 
- <a> uiStatistics </a> -> sends to the Master a request(**id 2**) , awaits the answer (which is the statistics of the user) and prints them. 
- <a> sendFile </a> -> sends the file to the server as bytes
- <a>  main </a> -> has the main UI for the client . It's basically a menu with 4 options:
    1. Send Files
    2. Vier Results
    3. Check Statistics
    4. End the application
- <a> run </a> -> It sends the file to the server and receives the results. First sends the request id(**1**), then awaits the answer of the master , to find out if there are enough workers to handle the load . If the message is not **0** then the thread send the user id , the file id and lastly, the file. Else it terminates its proccess. In the end, it receives the results and writes it in the file.  



