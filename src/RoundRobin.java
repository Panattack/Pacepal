import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.spi.SyncResolver;

public class RoundRobin extends Thread {
    
    // TODO: must keep the id of the client & the number of chunks in a
    // database --> hashmap

    static ArrayList<Chunk> robin_chunks;
    private RobinQueue<WorkerAction> workerHandlers;

    public RoundRobin(RobinQueue<WorkerAction> workerHandlers) {
        this.workerHandlers = workerHandlers;
        this.robin_chunks = new ArrayList<>();
    }

    public synchronized void add_chunk(Chunk chunk, List list) {
        // add chunks to the queue
        robin_chunks.add(chunk);
        notify();
    }

    public synchronized void transmit() {
        try {

            if (RoundRobin.robin_chunks.size() == 0) {
                wait();
            }

            while (RoundRobin.robin_chunks.size() != 0) {

                WorkerAction workerConnect = workerHandlers.remove();
                workerConnect.setChunk(robin_chunks.remove(0));
            }
            
            // System.out.println(RoundRobin.robin_chunks.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        // try {
        while (true) {
            // if (workerHandlers.is_Empty()) {
            //     wait();
            // }
            transmit();
        }
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
    }

}
