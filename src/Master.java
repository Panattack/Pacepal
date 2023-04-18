import java.io.*;
import java.net.*;
import java.util.*;

public class Master
{
    private int num_of_workers;// DEFINE IN Config file
    private int num_of_wpt;// DEFINE IN Config file
    private String gpx;
    public static int client_id = 0;
    private int worker_port = 1234;
    private int user_port = 4321;
    static public RobinQueue<ObjectOutputStream> workerHandlers;
    static public ArrayList<ObjectInputStream> mapperHandlers;

    /* Define the socket that receives requests from workers */
    ServerSocket workerSocket;

    /* Define the socket that receives requests from user */
    ServerSocket clientSocket;

    WorkerConnectionHandler worker;

    ClientConnectionHandler client;

    public Master(int num_workers) {
        Master.workerHandlers = new RobinQueue<>(num_workers);
        // this.rob = new RoundRobin(workerHandlers);
    }

    void openServer() {
        try {
            clientSocket = new ServerSocket(user_port, 4);
            this.client = new ClientConnectionHandler(clientSocket);
            workerSocket = new ServerSocket(worker_port, 4);
            this.worker = new WorkerConnectionHandler(workerSocket);

            // rob.start();
            worker.start();
            client.start();

            // Thread client = new Thread(() -> {
            //     while (true) {
            //         try {
            //             // Accept the connection
            //             // Define the socket that is used to handle the connection
            //             Socket connection = clientSocket.accept();
            //             Thread clientHandler = new ClientAction(connection, rob);
            //             clientHandler.start();
            //         } catch (IOException e) {
            //             e.printStackTrace();
            //         }
            //     }
            // });
            // client.start();
            
            // Thread worker = new Thread(() -> {
            //     while (true) {
            //         try {
            //             // Accept the connection
            //             // Define the socket that is used to handle the connection
            //             Socket connection = clientSocket.accept();
            //             workerHandlers.add(new WorkerAction(connection, rob));
            //         } catch (IOException e) {
            //             e.printStackTrace();
            //         }
            //     }
            // });
            // worker.start();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Master master = new Master(2);
        master.openServer();

    }

}
