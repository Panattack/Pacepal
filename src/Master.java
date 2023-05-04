import java.io.*;
import java.net.*;
import java.util.*;

public class Master
{
    public static int num_of_workers; // DEFINE IN Config file
    private int num_of_wpt; // DEFINE IN Config file
    private int worker_port = 1234;
    private int user_port = 4321;
    private int reducer_port = 9876;
    static public RobinQueue<ObjectOutputStream> workerHandlers; // related to the socket that every worker has made
    // It's a global id from clients --> workers and vice versa
    public static int inputFile = 0;
    // Global statistics
    public static Statistics statistics;
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
        Master.statistics = new Statistics();
        this.num_of_wpt = num_of_wpt;
        Master.num_of_workers = num_workers;
        Master.workerHandlers = new RobinQueue<>();
        userList = new SynchronizedHashMap<>();
    }

    void openServer() {
        try {
            clientSocket = new ServerSocket(user_port, 4);
            workerSocket = new ServerSocket(worker_port, 4);
            reducerSocket = new ServerSocket(reducer_port, 4);

            Thread client = new Thread(() -> {
                while (true) {
                    try {
                        Socket connectionSocket = clientSocket.accept();
                        ClientAction clienThread = new ClientAction(connectionSocket, inputFile, num_of_wpt);
                        Master.clientLHandlers.put(Master.inputFile, clienThread);
                        Master.inputFile++;
                        clienThread.start();
                    } catch (IOException e) {
                        System.out.println("Connection Error with Client <--> Master");
                    }
                }
            });
            client.start();

            Thread reducer = new Thread(() -> {
                while (true) {
                    try {
                        Socket requestSocket = reducerSocket.accept();
                        Thread reduceThread = new RequestHandler(requestSocket);
                        reduceThread.start();
                    } catch (IOException e) {
                        System.out.println("Connection Error with Reducer <--> Master");
                    }
                }
            });
            reducer.start();

            Thread worker = new Thread(() -> {
                while (true) {
                    try {
                        Socket communicationSocket = workerSocket.accept();
                        Master.workerHandlers.add(new ObjectOutputStream(communicationSocket.getOutputStream()));
                    } catch (IOException e) {
                        System.out.println("Connection Error with Worker <--> Master");
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
        String fileName = "pacepal/src/config.conf";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException ex) {
            System.out.println("File not found !!!");
        }
        System.out.println(prop.getProperty("num_of_workers"));
        System.out.println(Integer.parseInt(prop.getProperty("num_wpt")));
        new Master(Integer.parseInt(prop.getProperty("num_of_workers")), Integer.parseInt(prop.getProperty("num_wpt"))).openServer();
    }
}
