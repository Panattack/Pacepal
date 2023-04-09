import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Worker extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;
    ArrayList<Waypoint> chunk;
    
    public Worker(Socket connection) {
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Chunk c = (Chunk) in.readObject();
            // Worker job - Map function
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);// need it in readObject 
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
