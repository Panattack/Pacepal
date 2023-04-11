import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

public class Client extends Thread{

    private static String path = "pacepal/gpxs/gpxs/";
    private File gpx;

    public Client(String file)
    {
        this.gpx = new File(file);
    }
    public static void main(String[] args) {
        new Client(path + "/route1.gpx").start();
        new Client(path + "/route2.gpx").start();
        //code
        // Properties prop = new Properties();
        // String fileName = "pacepal/src/config.conf";
        // try (FileInputStream fis = new FileInputStream(fileName)) {
        //     prop.load(fis);
        //     System.out.println(prop.getProperty("num_of_workers"));
        // } catch (FileNotFoundException ex) {
        //     System.out.println("File not found !!!"); // FileNotFoundException catch is optional and can be collapsed
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    @Override
    public void run()
    {
        ObjectOutputStream out = null ;
        ObjectInputStream in = null ;

        Socket requestSocket = null;

        try {
            String host = "localhost";

            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, 4321);
 
            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            out.writeObject(gpx);
            out.flush();

            //ArrayList<Chunk> file = (ArrayList<Chunk>) in.readObject();

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        // } catch (ClassNotFoundException e) {
        //     throw new RuntimeException(e);
        }
        finally {
            try {
                System.out.println("end of client");
                in.close(); out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
