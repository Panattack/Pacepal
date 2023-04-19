import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Reducer extends Thread {
    public static HashMap<Integer, Pair<ArrayList<Chunk>, Integer>> intermediate_results =  new HashMap<>();
    
    // ClientAction
    public static void createEntry(int id, int size) {
        intermediate_results.put(id, new Pair<>(new ArrayList<Chunk>(), size));
    }

    // WorkerAction
    public static synchronized void addResults(Chunk intermResult) {
        Print();
        intermediate_results.get(intermResult.getId()).getKey().add(intermResult);
        int size = intermediate_results.get(intermResult.getId()).getValue();
        intermediate_results.get(intermResult.getId()).setValue(--size);

        if (size == 0) {
            // Final Results
            double distanceResult = 0.0;
            double timeResult = 0.0;
            double avgSpeedResult = 0.0;
            double elevationResult = 0.0;
            double num_chunks = 0.0;

            for (Chunk c : intermediate_results.get(intermResult.getId()).getKey()) {
                distanceResult = distanceResult + c.getTotalDistance();
                timeResult = timeResult + c.getTotalTime();
                elevationResult = elevationResult + c.getTotalElevation();
                avgSpeedResult = avgSpeedResult + c.getAvgSpeed();
                num_chunks++;
            }

            avgSpeedResult = avgSpeedResult / num_chunks;

            // calc the user statistics
            Master.userList.get(intermResult.getUser()).updateStatistics(distanceResult, timeResult, elevationResult);

            // send the results to the clients
            ArrayList list = new ArrayList<>();
            list.add(distanceResult);
            list.add(timeResult);
            list.add(elevationResult);
            list.add(avgSpeedResult);
            
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

    @Override
    public void run() {
        while (true) {
            
        }
    }
}
