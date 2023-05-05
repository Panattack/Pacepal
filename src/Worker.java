import java.io.*;
import java.net.*;

public class Worker extends Thread {
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
    // public static int id = 0;
    
    public Worker(Socket connection, Chunk c){
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

    public static void main(String[] args) {
        
        String host = "localhost";
    
        try {
            Socket connectionSocket = new Socket(host, roundrobinPort);
            Worker.in = new ObjectInputStream(connectionSocket.getInputStream());
        }
        catch (IOException e) {
            System.err.println("Error I/O error occurs when creating the socket when stabilizing with the Master");
        }
        
        while (true) {
            try {
                // new Socket();
                Chunk chunk = (Chunk) Worker.in.readObject();
                Socket chunkSocket = new Socket(host, requestreducePort);
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
