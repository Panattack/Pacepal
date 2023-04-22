import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class Client extends Thread{

    private static String path = "pacepal/gpxs/gpxs/";
    static long start;
    private String gpx;
    ObjectOutputStream out = null ;
    ObjectInputStream in = null ;

    public Client(String file)
    {
        this.gpx = file;
    }
    public static void main(String[] args) {
        // start = System.currentTimeMillis();
        
        new Client(path + "route1.gpx").start();
        // new Client(path + "/route2.gpx").start();
        // new Client(path + "/route3.gpx").start();
        // new Client(path + "/route4.gpx").start();
        // new Client(path + "/route5.gpx").start();
        // new Client(path + "/route6.gpx").start();
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

    private void sendFile(String fileName) 
    {
        try
        {
            // Send the file name to the server
            this.out.write(fileName.getBytes());

            FileInputStream fileInputStream = new FileInputStream(this.gpx);
            // System.out.println("Sending file \"" + fileName + "\"...");

            byte[] buffer = new byte[1000];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                // Send the data to the server
                out.write(buffer, 0, len);
            }

            // Close the file input stream
            fileInputStream.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {

        Socket requestSocket = null;

        try {
            String host = "localhost";

            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, 4321);
 
            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            sendFile(this.gpx);
            //Sending GPX file
            // out.writeObject(gpx);
            // out.flush();
            
            Results results;
			try {
				results = (Results) in.readObject();
                System.out.println(results);
                // long end = System.currentTimeMillis();
                // long elapsedTime = end - start;
                // System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
                // System.out.println(results);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        // } catch (ClassNotFoundException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        } finally {
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
