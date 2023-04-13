import java.io.*;
import java.net.*;

public class WorkerAction extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket connection;
    RoundRobin rob;
    Chunk chunk;

    public WorkerAction(Socket connection, RoundRobin rob) {
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            this.rob = rob;
            this.connection = connection;
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    public void setChunk(Chunk chunk) {
        // System.out.println(chunk);
        this.chunk = chunk;
    }

    @Override
    public void run()
    {
        try {
            out.writeObject(this.chunk);
            out.flush();

            // Take the results from reduce

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
