import java.io.*;
import java.net.*;
import java.util.Properties;

public class Worker extends Thread {
    // Global socket for the worker to listen chunks-requests from the master
    private static ObjectInputStream in;
    // Local socket per request to send the intermediate result to the reducer
    private ObjectOutputStream out;
    // Host port to listen the request
    private static int roundrobinPort ;
    // Host port to send intermediate result per chunk
    private static int requestreducePort; 
    private static String host ;
    private Chunk chunk;
    Socket requestSocket;
    
    public Worker(Socket connection, Chunk c) {
        this.requestSocket = connection;
        this.chunk =  c;
        try {
            this.out = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error in creating the \"out\" object");
        }
    }
    
    public void run() {
        // Map Make the intermediate results
        this.chunk.calcStatistics();
        
        try {
            // Send them to the reducer
            this.out.writeObject(chunk);
            this.out.flush();
            this.requestSocket.close();
        } catch (IOException e) {
            System.err.println("Mapper found problem in sending the intermediate result");
        }
    }

    public static void initiDefault() {

        Properties prop = new Properties();
        String fileName = "pacepal/config/worker.cfg"; 
        
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException ex) {
            System.out.println("File not found !!!");
        }
     
        Worker.host = prop.getProperty("host");
        Worker.roundrobinPort = Integer.parseInt(prop.getProperty("roundrobinPort"));
        Worker.requestreducePort = Integer.parseInt(prop.getProperty("requestreducePort"));
        
    }

    public static void main(String[] args) {

        Worker.initiDefault();
        Socket connectionSocket ;
        try {
            connectionSocket = new Socket(host, roundrobinPort);
            Worker.in = new ObjectInputStream(connectionSocket.getInputStream());
        }
        catch (IOException e) {
            System.err.println("Error I/O error occurs when creating the socket when stabilizing with the Master");
        }
        
        while (!Thread.interrupted()) {
            try {
                // new Socket();
                Chunk chunk = (Chunk) Worker.in.readObject();
                Socket chunkSocket = new Socket(Worker.host, Worker.requestreducePort);
                // System.out.println(chunkSocket.getLocalPort());
                // new Socket per request
                new Worker(chunkSocket, chunk).start();
            } catch (ClassNotFoundException e) {
                System.err.println("Error: Didn't read Chunk object from Master");
            } catch (IOException e) {
                System.err.println("Error: I/O error occurs when creating the socket per request");
            }
        }
    }
    
}
