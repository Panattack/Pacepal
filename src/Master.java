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
    static public RobinQueue<ObjectOutputStream> workerHandlers; //related to the socket that every worker has made
    static public Reducer reducer;
    public static int inputFile = 0;
    public static HashMap<Integer, User> userList; //id, user

    public static HashMap<Integer, ObjectOutputStream> clientHandlers; // 

    /* Define the socket that receives requests from workers */
    ServerSocket workerSocket;

    /* Define the socket that receives requests from user */
    ServerSocket clientSocket;

    // WorkerConnectionHandler worker;

    // ClientConnectionHandler client;

    public Master(int num_workers) {
        Master.workerHandlers = new RobinQueue<>(num_workers);
        userList = new HashMap<>();
        Master.clientHandlers = new HashMap<>();
    }

    void openServer() {
        try {
            clientSocket = new ServerSocket(user_port, 4);
            // this.client = new ClientConnectionHandler(clientSocket);
            workerSocket = new ServerSocket(worker_port, 4);
            // this.worker = new WorkerConnectionHandler(workerSocket);
            reducer = new Reducer();
            // worker.start();
            // client.start();
            
            Thread client = new Thread(() -> {
                while (true) {
                    try {
                        // Accept the connection
                        // Define the socket that is used to handle the connection
                        Socket connection = clientSocket.accept();
                        Master.clientHandlers.put(Master.inputFile, new ObjectOutputStream(connection.getOutputStream()));

                        Thread clienThread = new ClientAction(connection);
                        clienThread.start();
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

        Master master = new Master(2);
        master.openServer();

    }

}
