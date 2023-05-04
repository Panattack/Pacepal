import java.io.*;
import java.net.*;

public class RequestHandler extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;

    public RequestHandler(Socket connection) {
        try {
            this.in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Send it to the reducer
            Chunk request = (Chunk) this.in.readObject();
            int inputFileId = request.getKey();
            int size;
            synchronized (Master.intermediate_results)
            {
                // Add the intermediate result to the list
                // 1st getKey is for chunk and 2nd getKey is for Pair
                Master.intermediate_results.get(inputFileId).getKey().add(request);
                size = Master.intermediate_results.get(inputFileId).getValue();
                Master.intermediate_results.get(inputFileId).setValue(--size);
            }

            // If size == 0 then send signal to the Master and remove the element
            if (size == 0)
            {
                Master.clientLHandlers.get(inputFileId).setIntermResults(Master.intermediate_results.get(inputFileId).getKey());
                
                // Delete the file record from the database
                Master.intermediate_results.remove(inputFileId);
                Master.clientLHandlers.remove(inputFileId);
            }
            // End of request socket
            in.close();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
