import java.io.*;
import java.net.*;

public class Worker extends Thread{
    // Global socket for the worker to listen chunks-requests from the master
    private static ObjectInputStream in;
    // Local socket per request to send the intermediate result to the reducer
    private ObjectOutputStream out;
    // Host port to listen the request
    private static int roundrobinPort = 1234;
    // Host port to send intermediate result per chunk
    private static int requestreducePort = 9876;
    private Chunk chunk;
    Socket requestSocket;
    // int id = 0;
    
    public Worker(Socket connection, Chunk c){
        this.requestSocket = connection;
        this.chunk =  c;
        try {
            this.out = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void run() {
        // Map Make the intermediate results
        this.chunk.calcStatistics();
        
        try {
            // Send them to the reducer
            this.out.writeObject(chunk);
            this.out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // TODO The worker will be one main and it's code will be from that in the run function
        
        String host = "localhost";
        Socket connectionSocket;
        try {
            connectionSocket = new Socket(host, roundrobinPort);
            Worker.in = new ObjectInputStream(connectionSocket.getInputStream());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        while (true) {
            try {
                Chunk chunk = (Chunk) in.readObject();
                Socket chunkSocket = new Socket(host, requestreducePort);
                // System.out.println(chunkSocket.getLocalPort());
                // new Socket per request
                new Worker(chunkSocket, chunk).start();
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
