import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Client extends Thread {
    
    private static String path = "pacepal/gpxs/gpxs/";
    // private static SynchronizedHashMap<Integer, Results> resultsList = new SynchronizedHashMap<>();
    private static ArrayList<Results> resultsList = new ArrayList<>();
    public static String host = "localhost";
    static long start;
    private String gpx;
    ObjectOutputStream out = null ;
    ObjectInputStream in = null ;

    private static Statistics stat;
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
        boolean flag = true;

        while (flag)
        {
            System.out.println();
            System.out.println("You have the following options :");
            System.out.println("1. Send files");
            System.out.println("2. View your results");
            System.out.println("3. Check your statistics");
            System.out.println("4. Exit our app");
            System.out.print("Insert your answer : ");
            int answer = scanner.nextInt();
            scanner.nextLine();
            // System.out.println();
            switch (answer)
            {
                case 1:
                    // Send files
                    uiGpx();
                    break;
                case 2:
                    // TODO: View Results -->check in another time the HashMap.entrySet
                    uiResults();
                    break;
                case 3:
                    // TODO: Check your statistics
                    uiStatistics();
                    break;
                default:
                    // Exit:
                    flag = false;
                    break;
            }
        }
        scanner.close();
    }

    private static void uiStatistics(){
        ObjectOutputStream out;
        ObjectInputStream in;

        /* Create the streams to send and receive data from server */
        
        try {
            /* Create socket for contacting the server on port 4321*/
            Socket requestSocket = new Socket(host, 4321);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());
            out.writeInt(2);
            out.flush();

            double totalDistance = 0;
            double  totalElevation = 0;
            double totalTime = 0;

            for (Results result : Client.resultsList){

                totalDistance = totalDistance + result.getTotalDistance();
                totalTime = totalTime + result.getTotalTime();
                totalElevation = totalElevation + result.getTotalElevation();
            }
            Statistics stat = (Statistics) in.readObject();

            System.out.println(stat);
            
            totalDistance = ((totalDistance - stat.getGlobalAvgDistance()) / stat.getGlobalAvgDistance()) * 100;
            totalTime = ((totalTime - stat.getGlobalAvgTime()) / stat.getGlobalAvgTime()) * 100;
            totalElevation = ((totalElevation - stat.getGlobalAvgElevation()) / stat.getGlobalAvgElevation()) * 100;
            
            DecimalFormat df = new DecimalFormat("##.##");

            System.out.println(
            "\nPercentage Distance is : "+ Float.valueOf(df.format(totalDistance)) + "%" + "\n"+
            "Percentage Elevation is : "+ Float.valueOf(df.format(totalElevation)) + "%" + "\n"+
            "Percentage Time is : "+ Float.valueOf(df.format(totalTime)) + "%" + "\n"
            );
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
  
    private static void uiGpx() 
    {
        String name;
        while (true)
        {
            System.out.print("Insert the file name : ");
            name = scanner.nextLine();
            new Client(path + name, indexFile++).start();
            System.out.print("Do you want to insert another file (y or n) : ");
            String choice = scanner.nextLine();

            if (choice.equals("n"))
            {
                break;
            }
        }
    }

    private static  void uiResults()
    {
        System.out.println();
        if (Client.resultsList.isEmpty())
        {
            System.out.println("********** You haven't sent a file **********");
            return;
        }
        for (Results result : Client.resultsList)
        {
            System.out.println(result);
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
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, 4321);
 
            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> File
            out.writeInt(1);
            out.flush();

            // Send user id
            out.writeInt(Client.userId);
            out.flush();

            //Send file id
            out.writeInt(this.fileId);
            out.flush();

            sendFile(this.gpx);
            //Sending GPX file
            
			try {
                // Route statistics
				Results results = (Results) in.readObject();
                // System.out.println("\nYour results are ready!");
                // System.out.println(results);
                // TODO : Fix EntrySet in HashMap to initialize resultList as SyncHashMap --> Results List is already in sync so there is no need for synchronized
                //Client.resultsList.put(this.fileId, results);
                synchronized (Client.resultsList)
                {
                    Client.resultsList.add(results);
                }
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
        }
    }
}
