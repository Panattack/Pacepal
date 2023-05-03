

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.Serializable;

public class Master
{
    private int num_of_workers; // DEFINE IN Config file
    private int num_of_wpt; // DEFINE IN Config file
    private int worker_port = 1234;
    private int user_port = 4321;
    private int reducer_port = 9876;
    static public RobinQueue<ObjectOutputStream> workerHandlers; //related to the socket that every worker has made
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
        this.num_of_workers = num_workers;
        Master.workerHandlers = new RobinQueue<>(this.num_of_workers);
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
                        // Accept the connection
                        // Define the socket that is used to handle the connection for a file from a client
                        // Can have multiple threads per client
                        Socket connectionSocket = clientSocket.accept();
                        // Object lock = new Object();
                        ClientAction clienThread = new ClientAction(connectionSocket, inputFile, num_of_wpt);
                        Master.clientLHandlers.put(Master.inputFile, clienThread);
                        Master.inputFile++;
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
        String fileName = "pacepal/src/config.conf";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
            //System.out.println(prop.getProperty("num_of_workers"));
        } catch (IOException ex) {
            System.out.println("File not found !!!"); // FileNotFoundException catch is optional and can be collapsed
        }
        System.out.println(prop.getProperty("num_of_workers"));
        System.out.println(Integer.parseInt(prop.getProperty("num_wpt")));
        new Master(Integer.parseInt(prop.getProperty("num_of_workers")), Integer.parseInt(prop.getProperty("num_wpt"))).openServer();
    }
}

// class Pair<K, V> implements Serializable{
//     private K key;
//     private V value;
    
//     public Pair(K key, V value) {
//         this.key = key;
//         this.value = value;
//     }
    
//     public K getKey() {
//         return key;
//     }
    
//     public V getValue() {
//         return value;
//     }
    
//     public void setKey(K key) {
//         this.key = key;
//     }
    
//     public void setValue(V value) {
//         this.value = value;
//     }

//     @Override
//     public boolean equals(Object obj)
//     {
//         if (this == obj) { // If the objects are the same reference, they are equal
//             return true;
//         }

//         if (!(obj instanceof Pair)) { // If the object is not an instance of Pair, they are not equal
//             return false;
//         }

//         Pair<?, ?> other = (Pair<?, ?>) obj; // Cast the object to Pair

//         // Compare the key and value of the Pair objects
//         if (key == null) {
//             if (other.key != null) {
//                 return false;
//             }
//         } else if (!key.equals(other.key)) {
//             return false;
//         }

//         if (value == null) {
//             if (other.value != null) {
//                 return false;
//             }
//         } else if (!value.equals(other.value)) {
//             return false;
//         }

//         return true; // If all comparisons are equal, the objects are equal
//     }
// }