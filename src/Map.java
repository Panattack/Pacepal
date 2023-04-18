import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Map extends Thread {
    // ObjectInputStream in;
    ObjectOutputStream out;
    Socket connection;
    // ArrayList<Chunk> bufferChunk;
    Chunk chunk;

    public Map(Socket connection, Chunk chunk) {
    
        try {
            this.out = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // in = new ObjectInputStream(connection.getInputStream());
        this.connection = connection;
        this.chunk = chunk;
    }

    @Override
    public void run() {       
        // System.out.println(this.chunk);

        // Make the intermediate results
        this.chunk.calcStatistics();
        Chunk c = this.chunk;
        // Send them to the reducer
        try {
            this.out.writeObject(c);
            this.out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
