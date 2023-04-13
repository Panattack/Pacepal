import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.management.remote.SubjectDelegationPermission;

public class ClientAction extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    private final ParserGPX parser = new ParserGPX();
    private final int num_of_wpt = 16;
    private RoundRobin rob;

    public ClientAction(Socket connection, RoundRobin rob) {
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            this.rob = rob;
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
            create_chunk(wpt_list);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void create_chunk(ArrayList<Waypoint> wpt_list) {

        // TODO: when we create a chunk, we must keep a connection between the 
        // the sequential chunks. Keep th last waypoint of the previous chunk
        // as the first waypoint to the next.
        int num_chunk = 0;
        while (wpt_list.size() != 0)
        {
            int endIndex = Math.min(num_of_wpt, wpt_list.size());
            List<Waypoint> list =  wpt_list.subList(0, endIndex);
            Chunk sublist = new Chunk(num_chunk, wpt_list.get(0).getUser());

            int k = 0;
            for (k = 0; k < endIndex; k++) 
            {
                sublist.add((Waypoint) list.remove(0));
            }
            if (list.size() == 1 && k == num_of_wpt)
            {
                sublist.add((Waypoint) list.remove(0));
            }
            rob.add_chunk(sublist, wpt_list);
            num_chunk++;
        }
    }
}
