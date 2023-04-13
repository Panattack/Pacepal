import java.io.*;
import java.net.*;

public class WorkerConnectionHandler extends Thread{
    
    ObjectInputStream in;
    ObjectOutputStream out;
    ServerSocket workerSocket;

    public WorkerConnectionHandler (ServerSocket workerSocket) {
        this.workerSocket = workerSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket communicationSocket = workerSocket.accept();
                WorkerAction workerThread = new WorkerAction(communicationSocket, Master.rob);
                workerThread.start();
                Master.workerHandlers.add(workerThread);
                // System.out.println("worker");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
