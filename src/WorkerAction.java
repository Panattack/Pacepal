import java.io.*;
import java.net.*;

public class WorkerAction extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;

    public WorkerAction(Socket connection) {
        try {
            this.in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Send it to the reducer
                Reducer.addResults((Chunk) this.in.readObject());
            }
            
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
