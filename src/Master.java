import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Master
{
    private int num_of_workers;// DEFINE IN Config file
    private int num_of_wpt;// DEFINE IN Config file
    private String gpx;
    private ArrayList<Waypoint> wpt_list = new ArrayList<>();
    private int worker_port = 1234;
    private int user_port = 4321;
    ObjectInputStream in_client;
    ObjectOutputStream out_client;
    ObjectInputStream in_worker;
    ObjectOutputStream out_worker;
    // Object mapper;
    /* Define the socket that receives requests from workers*/
    ServerSocket workersocket;

    /* Define the socket that receives requests from user */
    ServerSocket clientsocket;

    /* Define the socket that is used to handle the connection between master and clients*/
    Socket providerSocket;

    /* Define the socket that is used to handle the connection between master and workers*/
    Socket communicationSocket;

    public Master() {
    }

    void openServer() {
        try {

            clientsocket = new ServerSocket(user_port, 4);
            workersocket = new ServerSocket(worker_port, 4);

            while (true) {
                providerSocket = clientsocket.accept();
                Thread clienThread = new ClientAction(providerSocket);
                clienThread.start();

                //communicationSocket = workersocket.accept();
                in_client = new ObjectInputStream(providerSocket.getInputStream());
            }
            
        } catch (IOException ioException) {
            ioException.printStackTrace();
        // } catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        } 
        finally {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Master().openServer();
        // Properties prop = new Properties();
        // String fileName = "pacepal/src/config.conf";
        // try (FileInputStream fis = new FileInputStream(fileName)) {
        //     prop.load(fis);
        //     //System.out.println(prop.getProperty("num_of_workers"));
        // } catch (FileNotFoundException ex) {
        //     System.out.println("File not found !!!");; // FileNotFoundException catch is optional and can be collapsed
        // }
        // new MasterWorker("pacepal/gpxs/gpxs/route1.gpx", Integer.parseInt(prop.getProperty("num_of_workers")), Integer.parseInt(prop.getProperty("num_wpt"))).openServer();
    }
}
