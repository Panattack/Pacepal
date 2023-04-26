import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

public class Client extends Thread{

    private static String path = "pacepal/gpxs/gpxs/";
    static long start;
    private String gpx;
    ObjectOutputStream out = null ;
    ObjectInputStream in = null ;

    // User id is static because threads must have a common id from the same user
    // IS THE ONLY VARIABLE THAT WILL BE CHANGED FROM US
    static private int userId = 0;
    // File id is unique for every thread
    private int fileId;
    static int indexFile = 0;
    static Scanner scanner = new Scanner(System.in);
    static String clearCommand = "";

    public Client(String file, int fileIndex)
    {
        this.gpx = file;
        this.fileId = fileIndex;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // start = System.currentTimeMillis();
        // int indexFile = 0;
        // new Client(path + "route1.gpx", indexFile++).start();
        // new Client(path + "/route4.gpx", indexFile).start();

        // String clearScreen = "\033[H\033[2J";
        boolean flag = true;

        while (flag)
        {
            System.out.println("You have the following options :");
            System.out.println("1. Send files");
            System.out.println("2. Check your statistics");
            System.out.println("3. Exit our app");
            System.out.print("Insert your answer : ");
            int answer = scanner.nextInt();

            switch (answer)
            {
                case 1:
                    // TODO: Send files
                    uiGpx();
                    break;
                case 2:
                    // TODO: Check your statistics
                    break;
                default:
                    // Exit:
                    flag = false;
                    break;
            }
        }
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

    private static void uiGpx() 
    {
        while (true)
        {
            System.out.print("Insert the file name : ");
            String name = scanner.nextLine();
            new Client(path + name, indexFile++).start();
            System.out.print(" Do you want to insert another file (y or n)");
            String choice = scanner.nextLine();

            if (choice == "n")
            {
                break;
            }
        }
    }

    private void sendFile(String fileName) 
    {
        try
        {
            // Send the file name to the server
            File file = new File(fileName);
            // out.writeObject(file.getName());
            // out.flush();
            byte[] buffer = new byte[(int) file.length()];
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
            reader.read(buffer, 0, buffer.length);
            reader.close();
            out.writeInt(buffer.length);
            out.flush();
            out.write(buffer, 0, buffer.length);
            out.flush();

            // Close the file input stream
            // fileInputStream.close();
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

            // Send user id
            out.writeInt(Client.userId);
            out.flush();

            //Send file id
            out.writeInt(this.fileId);
            out.flush();

            sendFile(this.gpx);
            //Sending GPX file
            // out.writeObject(gpx);
            // out.flush();
            
			try {
                // Route statistics
				Results results = (Results) in.readObject();
                System.out.println(results);
                // long end = System.currentTimeMillis();
                // long elapsedTime = end - start;
                // System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
                // System.out.println(results);
                in.close(); out.close();
                requestSocket.close();
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
        // } finally {
        //     try {
        //         System.out.println("end of client");
        //         // in.close(); out.close();
        //         // requestSocket.close();
        //     } catch (IOException ioException) {
        //         ioException.printStackTrace();
        //     }
        // }
        }
    }
}
