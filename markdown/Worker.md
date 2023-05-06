# Worker
> With this class we represent each worker. It also extends the class `Thread`. 

~ It has the following variables, for **every** worker:
1. in-> an  ObjectInputStream, they listen from the same master;
2. roundrobinPort -> the port in which it receives the chunks, from the master
3. requestreducePort -> the port to send the results to the master 
4. host -> the IP address of the master

~ It has the following variables, for **each** worker :
1. out -> an  ObjectOutputStream, everyone has different port to sent their result to the reducer
2. chuck 
3. requestSocket

It has the following methods:
- <a> Constructor </a>
- <a> initDefault </a> -> it initializes the global variables from the configuration file 
- <a> main </a> -> it creates the connection with the master and waits for chuncks. For every new chuck it receives, constructs a new socket and <u>creates</u> and <u>starts</u> the worker thread.
- <a>run</a> -> each threat calculates the requested parameters, and sents it to the master for the reducing process. 

