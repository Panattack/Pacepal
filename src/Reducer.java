import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Reducer extends Thread {
    public static HashMap<Integer, Pair<ArrayList<Chunk>, Integer>> intermediate_results =  new HashMap<>();
    
    // ClientAction
    public static synchronized void createEntry(int id, int size) {
        intermediate_results.put(id, new Pair<>(new ArrayList<Chunk>(), size));
    }

    // WorkerAction
    public static void addResults(Chunk intermResult) {
        //Print();
        int size;
        synchronized (intermediate_results.get(intermResult.getId()))
        {
            intermediate_results.get(intermResult.getId()).getKey().add(intermResult);
            size = intermediate_results.get(intermResult.getId()).getValue();
            intermediate_results.get(intermResult.getId()).setValue(--size);
            if (size == 0) {
                calcResults(intermediate_results.get(intermResult.getId()).getKey(), intermResult.getId(), intermResult.getUser());
            }    
        }

        
        Print();
        // Pair p = intermediate_results.get(intermResult.getId());
        // ArrayList<Chunk> k = (ArrayList<Chunk>) p.getKey();
        // k.add(intermResult);
    }

    public static void Print() {
        for (Map.Entry<Integer, Pair<ArrayList<Chunk>, Integer>> entry : intermediate_results.entrySet()) {

            System.out.println(" gpx file : " + entry.getKey() + " size array : " + entry.getValue().getValue());
        }
    }

    private static void calcResults(ArrayList<Chunk> chunks, int id, String user)
    {
        // Final Results
        double distanceResult = 0.0;
        double timeResult = 0.0;
        double avgSpeedResult = 0.0;
        double elevationResult = 0.0;
        double num_chunks = 0.0;

        for (Chunk c : intermediate_results.get(id).getKey()) {
            distanceResult = distanceResult + c.getTotalDistance();
            timeResult = timeResult + c.getTotalTime();
            elevationResult = elevationResult + c.getTotalElevation();
            avgSpeedResult = avgSpeedResult + c.getAvgSpeed();
            num_chunks++;
        }

        avgSpeedResult = avgSpeedResult / num_chunks;

        // calc the user statistics
       // Master.userList.get(intermResult.getUser()).updateStatistics(distanceResult, timeResult, elevationResult);

        // send the results to the clients

        Results results = new Results(distanceResult, avgSpeedResult, elevationResult, timeResult, id, user);
        
        synchronized (Master.clientHandlers.get(id))
        {
            ObjectOutputStream out = Master.clientHandlers.get(id);
            try {
                out.writeObject(results);
                out.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            
        }
    }
}
