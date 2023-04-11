import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientAction extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    private final ParserGPX parser = new ParserGPX();
    private final int num_of_wpt = 16;
    
    public ClientAction(Socket connection) {
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override 
    public void run()
    {
        try {
            File file = (File) in.readObject();

            ArrayList<Waypoint> wpt_list = parser.parse(file);
            
            // Create chunks 
            //ArrayList<Chunk> chunks = create_chunk(wpt_list);
            ArrayList<Chunk> chunks = create_chunk(wpt_list);
            out.writeObject(chunks);
            out.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Chunk> create_chunk(ArrayList<Waypoint> wpt_list) {
        //ArrayList<Chunk> chunks = new ArrayList<>();
        // List that contains lists of waypoints / (chunks)
        // List<Waypoint> -> Chunk
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        int size = wpt_list.size();
        // Split the wpt_list in wpt_list.size() // num_of_wpt chunks and store them in
        // chunks (return variable) 
        for (int i = 0; i < size; i += num_of_wpt) {
            int endIndex = Math.min(i + num_of_wpt, size);
            List<Waypoint> list =  wpt_list.subList(i, endIndex);
            Chunk sublist = new Chunk();
            for (int k = 0; k< list.size(); k++) 
            {
                sublist.add((Waypoint) list.get(k));
            }
            if (sublist.size() == 1)
            {
                chunks.get(chunks.size() - 1).add(sublist.get(0));
            }
            chunks.add(sublist);
        }
    
        return chunks;

    }

}
