import java.io.*;
import java.net.*;
import java.util.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Master
{
    public static int num_of_workers; // DEFINE IN Config file
    private int num_of_wpt; // DEFINE IN Config file
    private int worker_port;
    private int user_port;
    private int reducer_port;
    static public RobinQueue<ObjectOutputStream> workerHandlers; // related to the socket that every worker has made
    // It's a global id from clients --> workers and vice versa
    public static int inputFile = 0;
    // Global statistics
    public static Statistics statistics;
    // A hashmap that we keep the user records
    public static SynchronizedHashMap<Integer, User> userList; //id, user
    // A hashmap that we keep the locks to add results to the clientThread
    public static SynchronizedHashMap<Integer, ClientAction> clientLHandlers;
    // A hashmap that we keep all the intermediate results per file in order to reduce them
    public static SynchronizedHashMap<Integer, Pair<ArrayList<Chunk>, Integer>> intermediate_results;

    /* Define the socket that sends requests to workers */
    ServerSocket workerSocket;

    /* Define the socket that receives results from reducer */
    ServerSocket clientSocket;

    /* Define the socket that receives intermediate results from workers */
    ServerSocket reducerSocket;

    public Master(int num_workers, int num_of_wpt, int user_port, int worker_port, int reducer_port) {
        Master.statistics = new Statistics();
        this.user_port = user_port;
        this.worker_port = worker_port;
        this.reducer_port = reducer_port;
        this.num_of_wpt = num_of_wpt;
        Master.num_of_workers = num_workers;
        Master.workerHandlers = new RobinQueue<>();
        Master.userList = new SynchronizedHashMap<>();
        Master.clientLHandlers = new SynchronizedHashMap<>();
        Master.intermediate_results = new SynchronizedHashMap<>();
    }

    class SynchronizedHashMap<K, V> {

        private final Map<K, V> map;
    
        public SynchronizedHashMap() {
            this.map = new HashMap<>();
        }
    
        public synchronized void put(K key, V value) {
            map.put(key, value);
        }
    
        public synchronized V get(K key) {
            return map.get(key);
        }
    
        public synchronized void remove(K key) {
            map.remove(key);
        }
    
        public synchronized boolean containsKey(K key) {
            return map.containsKey(key);
        }
    
        public synchronized boolean containsValue(V value) {
            return map.containsValue(value);
        }
    
        public synchronized int size() {
            return map.size();
        }
    
        public synchronized boolean isEmpty() {
            return map.isEmpty();
        }
    
        public synchronized void clear() {
            map.clear();
        }
    
        public synchronized Set<Map.Entry<K, V>> entrySet() {
            return map.entrySet();
        }
    }

    class RobinQueue<T> {

        private int maxSize;
        private LinkedList<T> queue;
        private int index;
    
        public RobinQueue() {
            this.maxSize = 0;
            this.index = 0;
            this.queue = new LinkedList<>();
        }
    
        public synchronized int getIndex()
        {
            return this.index;
        }
    
        public synchronized void add(T element) {
            this.queue.addLast(element);
            this.maxSize++;
        }
    
        public synchronized boolean is_Empty() {
            if (this.queue.size() == 0) {
                return true;
            }
            
            return false;
        }
    
        public synchronized T get() {
            T o = queue.get(this.index % this.maxSize);
            this.index++;
            return o;
        }
    
        public synchronized int size() {
            return this.queue.size();
        }
    }

    public class Pair<K, V> implements Serializable{
        private K key;
        private V value;
        
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return key;
        }
        
        public V getValue() {
            return value;
        }
        
        public void setKey(K key) {
            this.key = key;
        }
        
        public void setValue(V value) {
            this.value = value;
        }
    
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) { // If the objects are the same reference, they are equal
                return true;
            }
    
            if (!(obj instanceof Pair)) { // If the object is not an instance of Pair, they are not equal
                return false;
            }
    
            Pair<?, ?> other = (Pair<?, ?>) obj; // Cast the object to Pair
    
            // Compare the key and value of the Pair objects
            if (key == null) {
                if (other.key != null) {
                    return false;
                }
            } else if (!key.equals(other.key)) {
                return false;
            }
    
            if (value == null) {
                if (other.value != null) {
                    return false;
                }
            } else if (!value.equals(other.value)) {
                return false;
            }
    
            return true; // If all comparisons are equal, the objects are equal
        }
    }

    class User {

        private SynchronizedHashMap<Integer, Results> resultList = new SynchronizedHashMap<>();
    
        private int avgSize;
        private int id;
        private double totalDistance;
        private double totalElevation;
        private double totalTime;
        private double avgDistance;
        private double avgElevation;
        private double avgTime;
    
        public User(int id) {
            this.id = id;
            this.totalDistance = 0.0;
            this.totalElevation = 0.0;
            this.totalTime = 0.0;
            this.avgDistance = 0.0;
            this.avgElevation = 0.0;
            this.avgTime = 0.0;
            this.avgSize = 0;
        }
    
        public int getId() {
            return this.id;
        }
    
        public void setName(int id) {
            this.id = id;
        }
    
        public double getTotalDistance() {
            return totalDistance;
        }
    
        public void setTotalDistance(double totalDistance) {
            this.totalDistance = totalDistance;
        }
    
        public double getTotalElevation() {
            return totalElevation;
        }
    
        public void setTotalElevation(double totalElevation) {
            this.totalElevation = totalElevation;
        }
    
        public double getTotalTime() {
            return totalTime;
        }
    
        public void setTotalTime(double totalTime) {
            this.totalTime = totalTime;
        }
    
        public double getAvgDistance() {
            return avgDistance;
        }
    
        public void setAvgDistance(double avgDistance) {
            this.avgDistance = avgDistance;
        }
    
        public double getAvgElevation() {
            return avgElevation;
        }
    
        public void setAvgElevation(double avgElevation) {
            this.avgElevation = avgElevation;
        }
    
        public double getAvgTime() {
            return avgTime;
        }
    
        public void setAvgTime(double avgTime) {
            this.avgTime = avgTime;
        }
    
        public SynchronizedHashMap<Integer, Results> getResultList() {
            return resultList;
        }
    
        public void setResultList(SynchronizedHashMap<Integer, Results> resultList) {
            this.resultList = resultList;
        }
        
        public void updateStatistics(double distance, double time, double elevation) 
        {
            this.totalDistance = this.totalDistance + distance;
            this.totalTime = this.totalTime + time;
            this.totalElevation = this.totalElevation + elevation;
    
            // New average size = (Old average size * Total number of values before new value + New value) / (Total number of values before new value + 1)
            this.avgDistance = (this.avgDistance * this.avgSize + distance) / (this.avgSize + 1);
            this.avgElevation = (this.avgElevation * this.avgSize + elevation) / (this.avgSize + 1);
            this.avgTime = (this.avgTime * this.avgSize + time) / (this.avgSize + 1);
    
            this.avgSize++;
        }
    }

    class ParserGPX {

        public ArrayList<Waypoint> parse(InputStream file)
        {
            ArrayList<Waypoint> wpt_list = new ArrayList<>();
            try {
                DocumentBuilderFactory dBfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dBfactory.newDocumentBuilder();
                
                // Fetch GPX File
                Document document = builder.parse(file);
                document.getDocumentElement().normalize();
        
                //Get root node - gpx
                Element root = document.getDocumentElement();
        
                //Get all waypoints
                NodeList nList = document.getElementsByTagName("wpt");
        
                for (int i = 0; i < nList.getLength(); i++)
                {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE)
                    {
                        // Waypoint's elements
                        Element element = (Element) node;
                        //Init waypoint 
                        String user = root.getAttribute("creator");
                        Double lon = Double.parseDouble(element.getAttribute("lon"));
                        Double lat = Double.parseDouble(element.getAttribute("lat"));
                        Double ele = Double.parseDouble(element.getElementsByTagName("ele").item(0).getTextContent());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        LocalDateTime time = LocalDateTime.parse(element.getElementsByTagName("time").item(0).getTextContent(), formatter);
                        Waypoint wpt = new Waypoint(user, lon, lat, ele, time);
        
                        wpt_list.add(wpt);
                        //System.out.println("User_id: " + user + " " + " Wpt_id: " + i);
                    }
                }
            }
            catch (ParserConfigurationException p)
            {
                System.err.println("Error: a DocumentBuilder cannot be created in Gpx Parser which satisfies the configuration requested.");
            }
            catch (SAXException s)
            {
                System.err.println("Error: parse errors occurin Gpx Parser");
            }
            catch (IOException io)
            {
                System.err.println("Error: In Gpx Parser please check the content of the file or the implementation of parsing");
            }
            return wpt_list;
        }
    }

    class RequestHandler extends Thread {

        ObjectInputStream in;
        ObjectOutputStream out;
    
        public RequestHandler(Socket connection) {
            try {
                this.in = new ObjectInputStream(connection.getInputStream());
            } catch (IOException e) {
                System.err.println("Error: Defining out in RequestHandler");
            }
        }
    
        @Override
        public void run() {
            try {
                // Send it to the reducer
                Chunk request = (Chunk) this.in.readObject();
                int inputFileId = request.getKey();
                int size;

                synchronized (Master.intermediate_results)
                {
                    // Add the intermediate result to the list
                    // 1st getKey is for chunk and 2nd getKey is for Pair
                    Master.intermediate_results.get(inputFileId).getKey().add(request);
                    size = Master.intermediate_results.get(inputFileId).getValue();
                    Master.intermediate_results.get(inputFileId).setValue(--size);
                }

                // If size == 0 then send signal to the Master and remove the element
                if (size == 0)
                {
                    // Doesn't need synchronized because there is only one client action per request
                    Master.clientLHandlers.get(inputFileId).setIntermResults(Master.intermediate_results.get(inputFileId).getKey());

                    // Delete the file record from the database
                    Master.intermediate_results.remove(inputFileId);
                    Master.clientLHandlers.remove(inputFileId);
                }
                // End of request socket
                in.close();
            } catch (ClassNotFoundException e) {
                System.err.println("Error: Didn't received Chunk object from worker");
            } catch (IOException e) {
                System.err.println("Connection Error in RequestHandler");
            }
        }

    }

    class ClientAction extends Thread {
        ObjectInputStream in;
        ObjectOutputStream out;
        private InputStream is;
        private Socket socket;
        // Given from the client : user_id
        private int userId;
        // Given from the client : file_id
        private int fileId;
        // Given from the Master : num of waypoints per chunk
        private int num_of_wpt;
        // Given from the Master : Global input file
        private int inputFile;
        // An arraylist that contains all the intermediate results of the specific file from Master
        ArrayList<Chunk> interResults;
        // Monitor
        Object lock;
        // Menu choice
        private int choice;
        private final ParserGPX parser = new ParserGPX();
    
        public ClientAction(Socket connection, int inputFile, int num_of_wpt) {
            try {
                // Socket connection to listen from the client
                this.socket = connection;
                // File id is the global variable and we use it as a key in  the chunk
                this.inputFile = inputFile;
                this.num_of_wpt = num_of_wpt;
                // Monitor
                this.lock = new Object();
    
                this.in = new ObjectInputStream(this.socket.getInputStream());
                this.out = new ObjectOutputStream(this.socket.getOutputStream());
            } catch (IOException e) {
                System.err.println("Error in defining \"out\" & \"in\" in ClientAction");
            }
        }
    
        public int getFileId() {
            return fileId;
        }
    
        public void setFileId(int fileId) {
            this.fileId = fileId;
        }
    
        private void receiveFile()
        {
            try {        
                int fileSize = this.in.readInt();
                byte[] buffer = new byte[fileSize];
                byte[] fileBytes = new byte[0];
                int bytesRead;
    
                while (fileBytes.length < fileSize) {
                    bytesRead = in.read(buffer);
                    fileBytes = concatenateByteArrays(fileBytes, buffer, bytesRead);
                }
                this.is = new ByteArrayInputStream(fileBytes);
            } catch (IOException e) {
                System.err.println("Error in receiving the bytestream from user in ClientAction");
            }
        }
    
        private byte[] concatenateByteArrays(byte[] array1, byte[] array2, int length) {
            byte[] result = new byte[array1.length + length];
            System.arraycopy(array1, 0, result, 0, array1.length);
            System.arraycopy(array2, 0, result, array1.length, length);
            return result;
        }
    
        private void create_chunk(ArrayList<Waypoint> wpt_list) {
            // When we create a chunk, we must keep a connection between the
            // the sequential chunks. Keep the last waypoint of the previous chunk
            // as the first waypoint to the next.
            int num_chunk = 0;
            ArrayList<Chunk> chunks = new ArrayList<>();
            
            while (wpt_list.size() != 0)
            {
                int endIndex = Math.min(this.num_of_wpt - 1, wpt_list.size());
                List<Waypoint> list =  wpt_list.subList(0, endIndex);
                // Create key to pass it in the hashmap in the reducer & in the chunk in order to use it 
                // to access the hashmap
                Chunk sublist = new Chunk(this.inputFile, num_chunk, wpt_list.get(0).getUser(), this.userId, this.fileId);
    
                int k = 0;
                for (k = 0; k < endIndex; k++) 
                {
                    sublist.add((Waypoint) list.remove(0));
                }
                if (wpt_list.size() != 0) 
                {
                    sublist.add(wpt_list.get(0));
                }
                if (list.size() == 1 && k == num_of_wpt) {
                    // if in the next chunk is there only one wpt, remove it and add it in the previous chunk
                    wpt_list.remove(0);
                }
                chunks.add(sublist);
                num_chunk++;
            }
            
            // Update the intermediate results
            Master.intermediate_results.put(this.inputFile, new Pair<ArrayList<Chunk>, Integer>(new ArrayList<Chunk>(), num_chunk));
            
            for (Chunk c : chunks) { 
                //send chunk in RR sequence with random gpx order
                ObjectOutputStream outstream;
                
                outstream = Master.workerHandlers.get();
                
                try {
                    // Sync in order to send a chunk in the worker 
                    // but if two or more client threads have the same outstream, lock it
                    synchronized (outstream)
                    {
                        outstream.writeObject(c);
                        outstream.flush();
                    }
    
                } catch (IOException e) {
                    System.err.println("Error in sending the chunk to the worker of the User: " + c.getUserId() + " and from the file: " + c.getFileId());
                }
            }
        }
    
        private boolean checkBuffer()
        {
            try
            {
                if (Master.workerHandlers.size() < Master.num_of_workers)
                {
                    this.out.writeInt(0);
                    this.out.flush();
                    return false;
                }
                else 
                {
                    this.out.writeInt(1);
                    this.out.flush();
                }
            } catch (IOException e) {
                System.err.println("Error in checking Master.WorkerHandlers");
            }
            
            return true;
        }
    
        private void setIds() 
        {
            // Listen from the client it's id and the file id 
            try {
                this.userId = this.in.readInt();
                this.fileId = this.in.readInt();
            } catch (IOException e) {
                System.err.println("Error in reading the ids from the user in file reading");
            }
        }
    
        private void create_user(int user) {
            synchronized (Master.userList)
            {
                if (Master.userList.get(user) == null) 
                {
                    Master.userList.put(user, new User(user));
    
                    // Update globalSize in statistics
                    Master.statistics.addGlobalSize();
                }
            }
        }
        
        public void setIntermResults(ArrayList<Chunk> list)
        {
            this.interResults = list;
            synchronized (this.lock)
            {
                this.lock.notify();
            }
        }
    
        private Results reduceResults()
        {
            // Final Results
            double distanceResult = 0.0;
            //double timeResult = 0.0;
            double avgSpeedResult = 0.0;
            double elevationResult = 0.0;
            double num_chunks = 0.0;
            double timeInSeconds = 0.0;
     
            for (Chunk c : this.interResults) {
                distanceResult += c.getTotalDistance();
                elevationResult += c.getTotalElevation();
                avgSpeedResult += c.getAvgSpeed();
                timeInSeconds += c.getTotalTimeInSeconds();
                num_chunks++;
            }
     
            avgSpeedResult = avgSpeedResult / num_chunks;

            Results results = new Results(distanceResult, avgSpeedResult, elevationResult, timeInSeconds, this.fileId, this.userId);

            return results;
        }
    
        private void sendResults()
        {
            if (this.interResults == null)
            {
                synchronized (this.lock)
                {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Error of \"wait()\" in receiving the results");
                    }
                }
            }
    
            try {
                Results results = reduceResults();
                // Check for the computation of the personal record
                synchronized (Master.userList)
                {
                    // register the new route for the Database
                    Master.userList.get(this.userId).getResultList().put(this.fileId, results);
                    // update the personal record
                    Master.userList.get(this.userId).updateStatistics(results.getTotalDistance(), results.getTotalTime(), results.getTotalElevation());
                }
                // Update statistics
                Master.statistics.updateValues(results.getTotalTime(), results.getTotalDistance(), results.getTotalElevation());
    
                this.out.writeObject(results);
                this.out.flush();
            } catch (IOException e) {
                System.err.println("error in sending the results of the user : " + this.userId + " & file: " + this.fileId);
            }
        }
    
        private void uploadStatistics()
        {
            try {
                // Read user Id
                this.userId = this.in.readInt();
                User user = Master.userList.get(this.userId);
    
                if (user == null)
                {
                    // End the thread
                    this.out.writeInt(0);
                    this.out.flush();
                    return;
                }
                
                this.out.writeInt(1);
                this.out.flush();
                
                double totalDistance = 0;
                double totalElevation = 0;
                double totalTime = 0;
    
                for (Map.Entry<Integer, Results> tuple : user.getResultList().entrySet())
                {
                    totalDistance = totalDistance + tuple.getValue().getTotalDistance();
                    totalTime = totalTime + tuple.getValue().getTotalTime();
                    totalElevation = totalElevation + tuple.getValue().getTotalElevation();
                }
    
                // Create a replica to avoid synchronizationa problems with different actions 
                // that may alter the statistic (global) variable
                Statistics stat;
                synchronized (Master.statistics)
                {
                    stat = new Statistics(Master.statistics);
                }
    
                double presentageDistance = ((totalDistance - stat.getGlobalAvgDistance()) / stat.getGlobalAvgDistance()) * 100;
                double presentageTime = ((totalTime - stat.getGlobalAvgTime()) / stat.getGlobalAvgTime()) * 100;
                double presentageElevation = ((totalElevation - stat.getGlobalAvgElevation()) / stat.getGlobalAvgElevation()) * 100;
    
                stat.defUS(presentageDistance, presentageElevation, presentageTime);
    
                this.out.writeObject(stat);
                this.out.flush();
            } catch (IOException e) {
                System.err.println("False in statistics from clientThread");
            }
        }
    
        @Override 
        public void run()
        {
            /*
            Menu :
                1. Recei file
                2. Send statistics
            */
            
            try {
                this.choice = this.in.readInt();
                switch (this.choice)
                {
                    case 1:
                        // Check workerHandler size
                        boolean workersOK = checkBuffer();
                        if (!workersOK)
                        {
                            break;
                        }
    
                        // Send file
                        setIds();
                        // Create the user record
                        create_user(this.userId);
                        receiveFile();
    
                        ArrayList<Waypoint> wpt_list = parser.parse(this.is);
                        create_chunk(wpt_list);
    
                        sendResults();
    
                        break;
                    case 2: 
                        // Change this line to update the statistics
                        // Update the global statistics -- for all users
                        uploadStatistics();
                        // Send statistics
                        break;
                }
                this.in.close();this.out.close();
                this.socket.close();
            } catch (IOException e) {
                System.err.println("Connection Error : Master <--> Client");
            }
        }
    }
    

    void openServer() {
        try {
            clientSocket = new ServerSocket(user_port, 4);
            workerSocket = new ServerSocket(worker_port, 4);
            reducerSocket = new ServerSocket(reducer_port, 4);

            Thread client = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket connectionSocket = clientSocket.accept();
                        ClientAction clienThread = new ClientAction(connectionSocket, inputFile, num_of_wpt);
                        Master.clientLHandlers.put(Master.inputFile, clienThread);
                        Master.inputFile++;
                        clienThread.start();
                    } catch (IOException e) {
                        System.err.println("Connection Error with Client <--> Master");
                    }
                }
            });
            client.start();

            Thread reducer = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket requestSocket = reducerSocket.accept();
                        Thread reduceThread = new RequestHandler(requestSocket);
                        reduceThread.start();
                    } catch (IOException e) {
                        System.err.println("Connection Error with Reducer <--> Master");
                    }
                }
            });
            reducer.start();

            Thread worker = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket communicationSocket = workerSocket.accept();
                        System.out.println("Accept");
                        Master.workerHandlers.add(new ObjectOutputStream(communicationSocket.getOutputStream()));
                    } catch (IOException e) {
                        System.err.println("Connection Error with Worker <--> Master");
                    } 
                }
            });
            worker.start();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        String fileName = "pacepal/src/config.cfg";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException ex) {
            System.out.println("File not found !!!");
        }
        new Master(Integer.parseInt(prop.getProperty("num_of_workers")), 
                    Integer.parseInt(prop.getProperty("num_wpt")), 
                    Integer.parseInt(prop.getProperty("user_port")),
                    Integer.parseInt(prop.getProperty("worker_port")),
                    Integer.parseInt(prop.getProperty("reducer_port"))).openServer();
    }
}
