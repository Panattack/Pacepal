import java.io.*;
import java.net.*;

public class ClientConnectionHandler extends Thread{
    ObjectInputStream in;
    ObjectOutputStream out;
    ServerSocket clientSocket;

    public ClientConnectionHandler(ServerSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try {

            while (true) {
                Socket providerSocket = clientSocket.accept();
                // System.out.println("client");
                Master.clientHandlers.put(Master.client_id, new ObjectOutputStream(providerSocket.getOutputStream()));
                
                Thread clienThread = new ClientAction(providerSocket, Master.client_id++);
                clienThread.start();
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
        
    }
}
