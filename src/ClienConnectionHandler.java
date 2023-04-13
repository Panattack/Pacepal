import java.io.*;
import java.net.*;

public class ClienConnectionHandler extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;
    ServerSocket clientSocket;

    public ClienConnectionHandler(ServerSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try {

            while (true) {
                Socket providerSocket = clientSocket.accept();
                // System.out.println("client");
                Thread clienThread = new ClientAction(providerSocket, Master.rob);
                clienThread.start();
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
        
    }
}
