import java.io.*;
import java.net.*;

public class WorkerConnectionHandler extends Thread{
    
    ServerSocket workerSocket;

    public WorkerConnectionHandler (ServerSocket workerSocket) {
        this.workerSocket = workerSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket communicationSocket = workerSocket.accept();
                // System.out.println(communicationSocket.getPort());
                // WorkerAction workerThread = new WorkerAction(communicationSocket);
                // workerThread.start();
                // Client Action Thread : out.writeObject
                Master.workerHandlers.add(new ObjectOutputStream(communicationSocket.getOutputStream()));

                // WorkerAction / Reduce
                Thread workerThread = new WorkerAction(communicationSocket);
                workerThread.start();

                // Master.mapperHandlers.add(new ObjectInputStream(communicationSocket.getInputStream()));
                // System.out.println("worker");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
