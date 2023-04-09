import java.io.*;
import java.net.*;
 
public class Server {
 
    public static void main(String args[]) {
        new Server().openServer();
    }
    
    /* Define the socket that receives requests */
    ServerSocket s;

    /* Define the socket that is used to handle the connection */
    Socket providerSocket;
    void openServer() {
        try {
 
            /* Create Server Socket */
            s = new ServerSocket(4321, 10);//the same port as before, 10 connections
 
            while (true) {
                /* Accept the connection */
                providerSocket = s.accept();
            
                /* Handle the request */
                Thread t = new ActionsForClients(providerSocket);
                t.start();
            }
 
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
} 
