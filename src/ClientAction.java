import java.io.*;
import java.net.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.List;

public class ClientAction extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;

    private final ParserGPX parser = new ParserGPX();
    private final int num_of_wpt = 16;
    
    // private RoundRobin rob;
    private int clientId;

    public ClientAction(Socket connection, int id) {
        try {
            in = new ObjectInputStream(connection.getInputStream());
            this.clientId = id;
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
            create_user(wpt_list.get(0).getUser());

            // Create chunks
            create_chunk(wpt_list);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void create_user(String user) {
        if (Master.userList.get(user) != null) {
            Master.userList.put(user, new User(user));
        }
    }

    private void create_chunk(ArrayList<Waypoint> wpt_list) {

        // TODO: when we create a chunk, we must keep a connection between the 
        // the sequential chunks. Keep the last waypoint of the previous chunk
        // as the first waypoint to the next.
        int num_chunk = 0;
        ArrayList<Chunk> chunks = new ArrayList<>();
        while (wpt_list.size() != 0)
        {
            int endIndex = Math.min(num_of_wpt - 1, wpt_list.size());
            List<Waypoint> list =  wpt_list.subList(0, endIndex);
            Chunk sublist = new Chunk(this.clientId, num_chunk, wpt_list.get(0).getUser());

            int k = 0;
            for (k = 0; k < endIndex; k++) 
            {
                sublist.add((Waypoint) list.remove(0));
            }
            if (wpt_list.size() != 0) {
                sublist.add(wpt_list.get(0));
            }
            if (list.size() == 1 && k == num_of_wpt) {
                // if in the next chunk is there only one wpt, remove it and add it in the previous chunk
                wpt_list.remove(0);
            }
            chunks.add(sublist);
            num_chunk++;
        }
        Reducer.createEntry(clientId, num_chunk);
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
}
