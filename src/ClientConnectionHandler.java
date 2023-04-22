import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientConnectionHandler extends Thread{
    private InputStream clientFile;
    private final ParserGPX parser = new ParserGPX();
    private final int num_of_wpt = 16;
    private int fileId;
    private int userId;

    public ClientConnectionHandler(InputStream file, int fileId, int userId) {
        this.clientFile = file;
        this.fileId = fileId;
        this.userId = userId;
    }

    private void create_chunk(ArrayList<Waypoint> wpt_list) {
        // When we create a chunk, we must keep a connection between the
        // the sequential chunks. Keep the last waypoint of the previous chunk
        // as the first waypoint to the next.
        int num_chunk = 0;
        ArrayList<Chunk> chunks = new ArrayList<>();

        // Create key to pass it in the hashmap in the reducer & in the chunk in order to use it 
        // to access the hashmap
        Pair<Integer, Integer> key = new Pair<Integer, Integer>(this.userId, this.fileId);
        
        while (wpt_list.size() != 0)
        {
            int endIndex = Math.min(num_of_wpt - 1, wpt_list.size());
            List<Waypoint> list =  wpt_list.subList(0, endIndex);
            Chunk sublist = new Chunk(key, num_chunk, wpt_list.get(0).getUser());

            int k = 0;
            for (k = 0; k < endIndex; k++) 
            {
                sublist.add((Waypoint) list.remove(0));
            }
            if (wpt_list.size() != 0) 
            {
                sublist.add(wpt_list.get(0));
            }
            if (list.size() == 1 && k == num_of_wpt) {
                // if in the next chunk is there only one wpt, remove it and add it in the previous chunk
                wpt_list.remove(0);
            }
            chunks.add(sublist);
            num_chunk++;
        }

        Reducer.createEntry(key, num_chunk);
        for (Chunk c : chunks) { 
            //send chunk in RR sequence with random gpx order
            synchronized (Master.workerHandlers) {
                try {
                    ObjectOutputStream out = Master.workerHandlers.get();
                    out.writeObject(c);
                    out.flush();
                    // System.out.println(socket.getPort());
                    // System.out.println(sublist);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run()
    {
        ArrayList<Waypoint> wpt_list = parser.parse(this.clientFile);
        // Create chunks
        create_chunk(wpt_list);
    }
}
