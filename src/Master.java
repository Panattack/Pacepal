import java.io.*;
import java.net.*;
import java.util.*;

public class Master
{
    private int num_of_workers; // DEFINE IN Config file
    private int num_of_wpt; // DEFINE IN Config file
    private String gpx;
    private int worker_port = 1234;
    private int user_port = 4321;
    private int reducer_port = 9876;
    static public RobinQueue<ObjectOutputStream> workerHandlers; //related to the socket that every worker has made
    // It's a global id from clients --> workers and vice versa
    public static int inputFile = 0;
    // A hashmap that we keep the user records
    public static SynchronizedHashMap<Integer, User> userList = new SynchronizedHashMap<>(); //id, user
    // A hashmap that we keep the locks to add results to the clientThread
    public static SynchronizedHashMap<Integer, ClientAction> clientLHandlers= new SynchronizedHashMap<>();
    // A hashmap that we keep all the intermediate results per file in order to reduce them
    public static SynchronizedHashMap<Integer, Pair<ArrayList<Chunk>, Integer>> intermediate_results = new SynchronizedHashMap<>();

    /* Define the socket that sends requests to workers */
    ServerSocket workerSocket;

    /* Define the socket that receives results from reducer */
    ServerSocket clientSocket;

    /* Define the socket that receives intermediate results from workers */
    ServerSocket reducerSocket;

    public Master(int num_workers, int num_of_wpt) {
        this.num_of_wpt = num_of_wpt;
        Master.workerHandlers = new RobinQueue<>(num_workers);
        userList = new SynchronizedHashMap<>();
        // Master.clientHandlers = new HashMap<>();
    }

    void openServer() {
        try {
            // TODO 1. The backlog for workersocket and num_of_workers will be defined from a config file
            clientSocket = new ServerSocket(user_port, 4);
            workerSocket = new ServerSocket(worker_port, 4);
            reducerSocket = new ServerSocket(reducer_port, 4);

            Thread client = new Thread(() -> {
                while (true) {
                    try {
                        // Accept the connection
                        // Define the socket that is used to handle the connection for a file from a client
                        // Can have multiple threads per client
                        Socket connectionSocket = clientSocket.accept();
                        // Object lock = new Object();
                        ClientAction clienThread = new ClientAction(connectionSocket, inputFile, num_of_wpt);
                        Master.clientLHandlers.put(Master.inputFile, clienThread);
                        Master.inputFile++;
                        System.out.println("Master : " + Master.inputFile);
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
                        
                        // TODO New class to receive and reduce intermediate results
                        Thread reduceThread = new RequestHandler(requestSocket);
                        reduceThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            reducer.start();

            Thread worker = new Thread(() -> {
                while (true) {
                    try {
                        // Accept the connection
                        // Define the socket that is used to handle the connection
                        Socket communicationSocket = workerSocket.accept();

                        Master.workerHandlers.add(new ObjectOutputStream(communicationSocket.getOutputStream()));
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

        Properties prop = new Properties();
        String fileName = "src/config.conf";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
            //System.out.println(prop.getProperty("num_of_workers"));
        } catch (IOException ex) {
            System.out.println("File not found !!!");; // FileNotFoundException catch is optional and can be collapsed
        }
        System.out.println(prop.getProperty("num_of_workers"));
       System.out.println(Integer.parseInt(prop.getProperty("num_wpt")));
        // new Master(Integer.parseInt(prop.getProperty("num_of_workers")), Integer.parseInt(prop.getProperty("num_wpt"))).openServer();
        new Master(1, Integer.parseInt(prop.getProperty("num_wpt"))).openServer();
    }
}
