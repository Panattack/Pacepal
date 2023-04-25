import java.io.*;
import java.net.*;
import java.util.*;

public class Master
{
    private int num_of_workers;// DEFINE IN Config file
    private int num_of_wpt;// DEFINE IN Config file
    private String gpx;
    private int worker_port = 1234;
    private int user_port = 4321;
    private int reducer_port = 9876;
    static public RobinQueue<ObjectOutputStream> workerHandlers; //related to the socket that every worker has made
    static public Reducer reducer;
    // It's a global id from clients --> workers and vice versa
    public static int inputFile = 0;
    public static HashMap<Integer, User> userList; //id, user

    public static HashMap<Integer, ObjectOutputStream> clientHandlers; // 

    /* Define the socket that sends requests to workers */
    ServerSocket workerSocket;

    /* Define the socket that receives results from reducer */
    ServerSocket clientSocket;

    /* Define the socket that receives intermediate results from workers */
    ServerSocket reducerSocket;

    public Master(int num_workers, int num_of_wpt) {
        this.num_of_wpt = num_of_wpt;
        Master.workerHandlers = new RobinQueue<>(num_workers);
        userList = new HashMap<>();
        Master.clientHandlers = new HashMap<>();
    }

    void openServer() {
        try {
            // TODO 1. The backlog for workersocket and num_of_workers will be defined from a config file
            clientSocket = new ServerSocket(user_port, 4);
            workerSocket = new ServerSocket(worker_port, 4);
            reducerSocket = new ServerSocket(user_port, 4);
            
            Thread client = new Thread(() -> {
                while (true) {
                    try {
                        // Accept the connection
                        // Define the socket that is used to handle the connection for a file from a client
                        // Can have multiple threads per client
                        Socket connectionSocket = clientSocket.accept();
                        // Master.clientHandlers.put(Master.inputFile, new ObjectOutputStream(connection.getOutputStream()));

                        Thread clienThread = new ClientAction(connectionSocket, inputFile++, num_of_wpt);
                        clienThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            client.start();

            Thread reducer = new Thread(() -> {
                while (true) {
                    try {
                        // Accept the connection
                        // Define the socket that is used to handle the connection for an intermediate result from a worker
                        // Can have multiple threads per client
                        Socket requestSocket = reducerSocket.accept();
                        // New class to receive and reduce intermediate results
                        Thread reduceThread = new Thread();
                        reduceThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            client.start();
            
            Thread worker = new Thread(() -> {
                while (true) {
                    try {
                        // Accept the connection
                        // Define the socket that is used to handle the connection
                        Socket communicationSocket = workerSocket.accept();

                        Master.workerHandlers.add(new ObjectOutputStream(communicationSocket.getOutputStream()));

                        // WorkerAction / Reduce
                        Thread workerThread = new WorkerAction(communicationSocket);
                        workerThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                }
            });
            worker.start();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Master master = new Master(2, 16);
        master.openServer();

    }

}
