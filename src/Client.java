import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Client extends Thread {
    
    private static String path = "gpxs/gpxs/";
    // private static SynchronizedHashMap<Integer, Results> resultsList = new SynchronizedHashMap<>();
    private static ArrayList<Results> resultsList = new ArrayList<>();
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
        // int indexFile = 0;
        // new Client(path + "route1.gpx", indexFile++).start();
        // new Client(path + "/route4.gpx", indexFile).start();

        // String clearScreen = "\033[H\033[2J";
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
     
       double totalDistance=0;
       double  totalElevation=0;
       double totalTime=0;

        for (Results result : Client.resultsList){

            totalDistance=totalDistance+ result.getTotalDistance();
            totalTime= totalTime +result.getTotalTime();
            totalElevation=totalElevation+result.getTotalElevation();

        }

        
        totalDistance=((totalDistance-stat.getGlobalAvgDistance())/stat.getGlobalAvgDistance())*100;
        totalTime=((totalTime-stat.getGlobalAvgTime())/stat.getGlobalAvgTime())*100;
        totalElevation=((totalElevation-stat.getGlobalAvgElevation())/stat.getGlobalAvgElevation())*100;
        

        System.out.println("\nTotal Distance is : "+totalDistance +"\n"+
        "Total Elevation is : "+ totalElevation+"\n"+
        "Total Time is : "+ "Hours: "+ (int) totalTime /3600 + " Minutes: " + (int) (totalTime%3600) / 60 + " Seconds: " + (int) totalTime % 60 + "\n");


    }


    

    private static void uiGpx() 
    {
        // new Client(path + "route1.gpx", indexFile++).start();
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
            
			try {
                // Route statistics
				Results results = (Results) in.readObject();
                System.out.println("\nYour results are ready!");
                stat=(Statistics) in.readObject(); // we get the latest statistics and we save them 
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
