import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;

public class Worker extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket requestSocket;
    int id;
    
    public Worker(int work_id){
        this.id = work_id;
    }
    
    public void run() {
        try {
            String host = "localhost";
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, 1234);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Wait for a chunk from thread --> workerAction
            Chunk c = (Chunk) in.readObject();

            System.out.println(c);
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

    public static void main(String[] args) {
        new Worker(1).start();
        new Worker(2).start();
    }
}
