import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Map extends Thread {
    // ObjectInputStream in;
    ObjectOutputStream out;
    Chunk chunk;

    public Map(Chunk chunk, ObjectOutputStream out) {
        // in = new ObjectInputStream(connection.getInputStream());
        this.out = out;
        this.chunk = chunk;
    }

    @Override
    public void run() {       
        // System.out.println(this.chunk);

        // Make the intermediate results
        this.chunk.calcStatistics();
        
        try {
            // Send them to the reducer
            synchronized (out) {
                //one worker has many mappers and one output stream
                out.writeObject(chunk);
                out.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
