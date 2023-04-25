import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Reducer {

    // Key : [User, File_Id]
    // Value : [Chunk_List --> [...], size]
    public static HashMap<Integer, Pair<ArrayList<Chunk>, Integer>> intermediate_results =  new HashMap<>();
    
    // TODO if there is a generic HashMap class with sync methods, this method will be removed
    public static void createEntry(int key, int size) {
        synchronized (intermediate_results)
        {
            intermediate_results.put(key, new Pair<ArrayList<Chunk>, Integer>(new ArrayList<Chunk>(), size));
        }
    }

    public static void addResults(Chunk intermResult) {
        int size;
        int key = intermResult.getKey();
        // Synchronize for the threads that send intermediate results in the same gpx file from the same user
        synchronized (intermediate_results.get(key))
        {
            intermediate_results.get(key).getKey().add(intermResult);
            size = intermediate_results.get(key).getValue();
            intermediate_results.get(key).setValue(--size);
            if (size == 0) {
                // TODO: Another thread must send it 
                calcResults(intermediate_results.get(key).getKey(), key, intermResult.getUser());
            }
        }
    }

    // private static Pair<Integer, Integer> compareHashKeys(Pair<Integer, Integer> o)
    // {
    //     for (Map.Entry<Pair<Integer, Integer>, Pair<ArrayList<Chunk>, Integer>> entry : intermediate_results.entrySet()) {
    //         //System.out.println(entry.getKey().hashCode());
    //         //System.out.println(" User : " + entry.getKey().getKey() + " gpx : " + entry.getKey().getValue());
    //         if (entry.getKey().equals(o)) {
    //             return entry.getKey();
    //         }
    //     }
    //     return o;
    // }

    // public static void Print(Chunk c) {
    //     for (Map.Entry<Pair<Integer, Integer>, Pair<ArrayList<Chunk>, Integer>> entry : intermediate_results.entrySet()) {
    //         //System.out.println(entry.getKey().hashCode());
    //         //System.out.println(" User : " + entry.getKey().getKey() + " gpx : " + entry.getKey().getValue());
    //         if (entry.getKey().equals(c.getHashKey())) {
    //             System.out.println(true);
    //         }
    //     }
    // }

    private static void calcResults(ArrayList<Chunk> chunks, int id, String user)
    {
        // Final Results
        double distanceResult = 0.0;
        double timeResult = 0.0;
        double avgSpeedResult = 0.0;
        double elevationResult = 0.0;
        double num_chunks = 0.0;
        double timeInSeconds = 0.0;

        for (Chunk c : intermediate_results.get(id).getKey()) {
            // System.out.println(c.getTotalDistance());
            distanceResult += c.getTotalDistance();
            timeResult += c.getTotalTime();
            elevationResult += c.getTotalElevation();
            avgSpeedResult += c.getAvgSpeed();
            timeInSeconds += c.getTotalTimeInSeconds();
            num_chunks++;
        }

        avgSpeedResult = avgSpeedResult / num_chunks;

        Results results = new Results(distanceResult, avgSpeedResult, elevationResult, timeInSeconds, id.getValue(), user);

        ObjectOutputStream out = Master.clientHandlers.get(id.getKey());
    
        try {
            // TODO Synchronize the out for the same user
            out.writeObject(results);
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
