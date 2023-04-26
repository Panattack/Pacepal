import java.io.*;
import java.net.*;
import java.util.*;

public class ClientAction extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    private InputStream is;
    private Socket socket;
    // Given from the client : user_id
    private int userId;
    // Given from the client : file_id
    private int fileId;
    // Given from the Master : num of waypoints per chunk
    private int num_of_wpt;
    // Given from the Master : Global input file
    private int inputFile;
    // An arraylist that contains all the intermediate results of the specific file from Master
    ArrayList<Chunk> interResults;
    // Monitor
    Object lock;
    private final ParserGPX parser = new ParserGPX();

    public ClientAction(Socket connection, int inputFile, int num_of_wpt) {
        try {
            // Socket connection to listen from the client
            this.socket = connection;
            // File id is the global variable and we use it as a key in  the chunk
            this.inputFile = inputFile;
            this.num_of_wpt = num_of_wpt;
            // Monitor
            this.lock = new Object();

            this.in = new ObjectInputStream(connection.getInputStream());
            this.out = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    private void receiveFile()
    {
        try {
            // Get the file name from the client
            // String filename = (String) in.readObject();
        
            int fileSize = this.in.readInt();
            byte[] buffer = new byte[fileSize];
            byte[] fileBytes = new byte[0];
            int bytesRead;

            while (fileBytes.length < fileSize) {
                bytesRead = in.read(buffer);
                // fos.write(buffer, 0, bytesRead);
                fileBytes = concatenateByteArrays(fileBytes, buffer, bytesRead);
            }
            // fos.close();
            this.is = new ByteArrayInputStream(fileBytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private byte[] concatenateByteArrays(byte[] array1, byte[] array2, int length) {
        byte[] result = new byte[array1.length + length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, length);
        return result;
    }

    private void create_chunk(ArrayList<Waypoint> wpt_list) {
        // When we create a chunk, we must keep a connection between the
        // the sequential chunks. Keep the last waypoint of the previous chunk
        // as the first waypoint to the next.
        int num_chunk = 0;
        ArrayList<Chunk> chunks = new ArrayList<>();
        
        while (wpt_list.size() != 0)
        {
            int endIndex = Math.min(this.num_of_wpt - 1, wpt_list.size());
            List<Waypoint> list =  wpt_list.subList(0, endIndex);
            // TODO check the Input file as an attribute to the Chunk class
            // Create key to pass it in the hashmap in the reducer & in the chunk in order to use it 
            // to access the hashmap
            Chunk sublist = new Chunk(this.inputFile, num_chunk, wpt_list.get(0).getUser(), this.userId, this.fileId);

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

        Master.intermediate_results.put(this.inputFile, new Pair<ArrayList<Chunk>, Integer>(new ArrayList<Chunk>(), num_chunk));
        
        for (Chunk c : chunks) { 
            //send chunk in RR sequence with random gpx order
            ObjectOutputStream outstream;
            
            outstream = Master.workerHandlers.get();
            
            try {
                // Sync in order to send a chunk in the worker 
                // but if two or more client threads have the same outstream, lock it
                synchronized (outstream)
                {
                    outstream.writeObject(c);
                    outstream.flush();
                }
                // TODO Maybe we will make a HashMap generic class to sync methods
                // Master.clientHandlers.put(this.inputFile, this.out);
                
                // System.out.println(socket.getPort());
                // System.out.println(sublist);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void setIds() 
    {
        // Listen from the client it's id and the file id 
        try {
            this.userId = this.in.readInt();
            this.fileId = this.in.readInt();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void create_user(int user) {
        synchronized (Master.userList)
        {
            if (Master.userList.get(user) == null) 
            {
                // TODO Maybe we will create a general class with sync methods -- 
                Master.userList.put(user, new User(user));
            }
        }
    }
    
    public void setIntermResults(ArrayList<Chunk> list)
    {
        this.interResults = list;
        synchronized (this.lock)
        {
            this.lock.notify();
        }
    }

    private Results reduceResults()
    {
        // Final Results
        double distanceResult = 0.0;
        double timeResult = 0.0;
        double avgSpeedResult = 0.0;
        double elevationResult = 0.0;
        double num_chunks = 0.0;
        double timeInSeconds = 0.0;
 
        for (Chunk c : this.interResults) {
            // System.out.println(c.getTotalDistance());
            distanceResult += c.getTotalDistance();
            timeResult += c.getTotalTime();
            elevationResult += c.getTotalElevation();
            avgSpeedResult += c.getAvgSpeed();
            timeInSeconds += c.getTotalTimeInSeconds();
            num_chunks++;
        }
 
        avgSpeedResult = avgSpeedResult / num_chunks;

        Results results = new Results(distanceResult, avgSpeedResult, elevationResult, timeInSeconds, this.fileId, this.userId);

        return results;
    }

    private void sendResults()
    {
        if (this.interResults == null)
        {
            synchronized (this.lock)
            {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        try {
            Results results = reduceResults();
            // TODO Check for the computation of the personal record
            synchronized (Master.userList.get(this.userId))
            {
                // register the new route for the server
                Master.userList.get(this.userId).getResultList().put(this.fileId, results);
                // update the personal record
                Master.userList.get(this.userId).updateStatistics(results.getTotalDistance(), results.getTotalTime(), results.getTotalElevation());
            }
            this.out.writeObject(results);
            this.out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override 
    public void run()
    {
        // Set the ids
        setIds();
        // Create the user record
        create_user(this.userId);
        
        receiveFile();

        ArrayList<Waypoint> wpt_list = parser.parse(this.is);
        create_chunk(wpt_list);

        // while (true)
        // {}
        sendResults();
    }
}
