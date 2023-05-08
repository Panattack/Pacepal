import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.*;

public class Client extends Thread {
    
    private static String fileName;
    private static int serverPort;
    private static String path; 
    public static String host; 
    private String gpx;
    ObjectOutputStream out = null ;
    ObjectInputStream in = null ;
    private static Object lock_msg = new Object();
    // User id is static because threads must have a common id from the same user
    // IS THE ONLY VARIABLE THAT WILL BE CHANGED FROM US
    static private int userId = 0;
    // File id is unique for every thread
    private int fileId;
    static int indexFile = 0;
    static Scanner scanner = new Scanner(System.in);
    // static File file;
    static BufferedWriter writer;
    static long start;
    static long finish;

    public Client() {}

    public Client(String file, int fileIndex) {
        this.gpx = file;
        this.fileId = fileIndex;
    }

    public static void initDefault() {
        Properties prop = new Properties();
        String configName = "pacepal/config/client.cfg"; 
        
        try (FileInputStream fis = new FileInputStream(configName)) {
            prop.load(fis);
        } catch (IOException ex) {
            System.out.println("File not found !!!");
        }

        Client.path = prop.getProperty("path");
        Client.host = prop.getProperty("host");
        Client.fileName = prop.getProperty("fileName");
        Client.serverPort = Integer.parseInt(prop.getProperty("serverPort"));
    }

    public static void main(String[] args) {

        Client.initDefault();
        boolean flag = true;
        
        try {
            writer = new BufferedWriter(new FileWriter(fileName)); // this one 
            writer.close();
        } catch (IOException e) {
            System.err.println("Error in creating the writer for the client: " + userId);
        }

        while (flag)
        {
            System.out.println("You have the following options :");
            System.out.println("1. Send files");
            System.out.println("2. View your results");
            System.out.println("3. Check your statistics");
            System.out.println("4. Sent segments");
            System.out.println("5. Check the weather");
            System.out.println("6. Exit our app");
            System.out.print("Insert your answer : ");

            int answer = 0;
            try {
                answer = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Mismatch type!");
            }
            
            scanner.nextLine();

            switch (answer)
            {
                case 1:
                    // Send files
                    uiGpx();
                    break;
                case 2:
                    // View Results --> check in another time the HashMap.entrySet
                    uiResults();
                    break;
                case 3:
                    // Check your statistics
                    uiStatistics();
                    break;
                case 4:
                    uiBonus();
                    break;
                case 5:
                    uiWeather();
                    break;
                case 6:
                    flag = false;
                    break;
                default:
                    // Exit:
                    System.out.println("Insert a valid number next time");
                    break;
            }
        }
        scanner.close();
    }

    private static void uiWeather() {
        ObjectOutputStream out;
        ObjectInputStream in;

        /* Create the streams to send and receive data from server */
        try {
            /* Create socket for contacting the server on port 4321*/
            Socket requestSocket = new Socket(host, serverPort);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> Weather
            out.writeInt(3);
            out.flush();

            System.out.print("Insert the city : ");
            String name = scanner.nextLine();
            out.writeObject(name);
            out.flush();

            Weather weather = (Weather) in.readObject();
            System.out.println(weather);
            requestSocket.close();
        } catch (IOException e) {
            System.err.println("Connection Lost in statistic request");
        } catch (ClassNotFoundException e) {
            System.err.println("Error in connection -- cannot receive statistic object");
        }
    }

    private static void uiBonus() {
        Client client = new Client();
        System.out.print("Insert the file of the segment : ");
        String name = scanner.nextLine();

    }

    private static void uiStatistics() {
        ObjectOutputStream out;
        ObjectInputStream in;

        /* Create the streams to send and receive data from server */
        try {
            /* Create socket for contacting the server on port 4321*/
            Socket requestSocket = new Socket(host, serverPort);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> Statistics
            out.writeInt(2);
            out.flush();

            // Send user id to check if there is a user in the database
            out.writeInt(userId);
            out.flush();

            int msg = in.readInt();

            if (msg == 0)
            {
                // There is no user in the database
                System.out.println("You haven't registered in our app.\nPlease do so by choosing \"Send files\" !");
                requestSocket.close();
                return;
            }

            System.out.println("Here comes the statistics : ");
            Statistics stat = (Statistics) in.readObject();

            System.out.println(stat);
            DecimalFormat df = new DecimalFormat("##.##");

            System.out.println(
            "\nPercentage Distance is : "+ Float.valueOf(df.format(stat.getPcDistance())) + " %" + "\n"+
            "Percentage Elevation is : "+ Float.valueOf(df.format(stat.getPcElevation())) + " %" + "\n"+
            "Percentage Time is : "+ Float.valueOf(df.format(stat.getPcTime())) + " %" + "\n"
            );

            requestSocket.close();
        } catch (IOException e) {
            System.err.println("Connection Lost in statistic request");
        } catch (ClassNotFoundException e) {
            System.err.println("Error in connection -- cannot receive statistic object");
        }
    }
  
    private static void uiGpx() {
        String name;
        ArrayList<Thread> threadList = new ArrayList<>();
        // start = System.currentTimeMillis();
        while (!Thread.interrupted())
        {
            System.out.print("Insert the file of the route : ");
            name = scanner.nextLine();

            Client client = new Client(path + name, indexFile++);
            threadList.add(client);
            client.start();

            synchronized (Client.lock_msg)
            {
                try {
                    Client.lock_msg.wait();
                    System.out.print("Do you want to insert another file (y or n) : ");
                } catch (InterruptedException e) {
                    System.err.println("Error in wait() method in uiGpx in main thread");
                }
            }

            String choice = scanner.nextLine();

            if (choice.equals("n"))
            {
                break;
            }
        }

        System.out.println("\nLoading...\n");

        for (Thread cl : threadList)
        {
            try {
                cl.join();
            } catch (InterruptedException e) {
                System.err.println("Wrong join in " );
            }
        }
        // finish = System.currentTimeMillis();
        // System.out.println((finish - start));
    }

    private static  void uiResults() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            if (line == null)
            {
                System.out.println("\n********** You haven't sent a file **********");
                reader.close();
                return;
            }
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("An error occurred in reading the file.");
        }
    }

    private void sendFile(String fName, ObjectOutputStream out) {
        try
        { 
            File file = new File(fName);
            byte[] buffer = new byte[(int) file.length()];
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
            reader.read(buffer, 0, buffer.length);
            reader.close();
            
            out.writeInt(buffer.length);
            out.flush();
            out.write(buffer, 0, buffer.length);
            out.flush();

        }  catch (Exception e) {
            System.err.println("An error occured in the contexts in sendFile");
        }
    }

    @Override
    public void run() {
        Socket requestSocket = null;

        try {
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, serverPort);
            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Send id request --> File
            out.writeInt(1);
            out.flush();
            
            int msg = in.readInt();

            synchronized (Client.lock_msg) 
            {
                if (msg == 0)
                {
                    System.out.println("Not enough workers... Try again");
                    Client.lock_msg.notify();
                    return;
                }
                else 
                {
                    System.out.println("Enough workers --> approved");
                    Client.lock_msg.notify();
                }
            }
            
            // Send user id
            out.writeInt(Client.userId);
            out.flush();

            //Send file id
            out.writeInt(this.fileId);
            out.flush();

            sendFile(this.gpx, out);
            //Sending GPX file
            
            // Route statistics
            Results results = (Results) in.readObject();
            
            // Write the results in a list
            synchronized (Client.writer)
            {
                Client.writer = new BufferedWriter(new FileWriter(fileName, true));
                Client.writer.write(results.toString());
                Client.writer.close();
            }

            in.close(); out.close();
            requestSocket.close();
			       
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            System.err.println("Error: unusual context --> \"in\"  or \"out\" or writing in the file to run");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: ClassNotFound in \"in\" to receiving result");
        }
    }
}
