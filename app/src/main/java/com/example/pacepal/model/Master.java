package com.example.pacepal.model;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.Optional;
// javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Master {
    public static int num_of_workers; // DEFINE IN Config file
    private static int num_of_wpt; // DEFINE IN Config file
    private static int worker_port;
    private static int user_port;
    private static int reducer_port;
    static public RobinQueue<ObjectOutputStream> workerHandlers; // related to the socket that every worker has made
    // It's a global id from clients --> workers and vice versa
    public static int requestNo = 0;
    // Global statistics
    public static Statistics statistics;
    // A hashmap that we keep the user records
    public static SynchronizedHashMap<Integer, User> userList; //id, user
    // A hashmap that we keep the locks to add results to the clientThread
    public static SynchronizedHashMap<Integer, ClientAction> clientLHandlers;
    // A hashmap that we keep all the intermediate results per file in order to reduce them
    public static SynchronizedHashMap<Integer, Pair<ArrayList<Chunk>, Integer>> intermediate_results;
    // A hashmap that we keep all the segments in order to reduce them
    public static SynchronizedHashMap<Integer, ArrayList<Waypoint>> segments;
    /* Define the socket that sends requests to workers */
    ServerSocket workerSocket;

    /* Define the socket that receives results from reducer */
    ServerSocket clientSocket;

    /* Define the socket that receives intermediate results from workers */
    ServerSocket reducerSocket;

   // public static OpenWeatherAPI api;

    /* Databases */
    public static SegmentDAO segmentDAO;

    public class SegmentDAO {
        // Key : segment id
        // Value : Array List of User objects
        public SynchronizedHashMap<Integer, ArrayList<Chunk>> segmentUserList;

        public SegmentDAO() {
            this.segmentUserList = new SynchronizedHashMap<>();
        }

        public synchronized void addRecord(int segmentId, Chunk chunk) {
            if (!this.segmentUserList.containsKey(segmentId)) {
                this.segmentUserList.put(segmentId, new ArrayList<Chunk>());
            }

            Optional<Chunk> user = this.segmentUserList.get(segmentId).stream().filter(us -> us.getUserId() == chunk.getUserId()).findFirst();
            
            if (user.isPresent()) {
                //user.get().updateStatistics(chunk.getTotalDistance(), chunk.getTotalTime(), chunk.getTotalElevation());
                // System.out.println(user.get().getId() + " " + chunk.getUserId());
                if (user.get().getTotalTimeInSeconds() > chunk.getTotalTimeInSeconds()) {
                    this.segmentUserList.get(segmentId).remove(user.get());
                    this.segmentUserList.get(segmentId).add(chunk);
                }
            }
            else {
                this.segmentUserList.get(segmentId).add(chunk);
            }
        }

        public synchronized ArrayList<Chunk> orderByTime(int segmentId) {
            ArrayList<Chunk> results = new ArrayList<>(this.segmentUserList.get(segmentId));
            Collections.sort(results, Comparator.comparingDouble(Chunk::getTotalTimeInSeconds).reversed().thenComparingDouble(Chunk::getTotalDistance).reversed());
            return results;
        }
    }

//    class OpenWeatherAPI {
//
//        String openWeatherApiKey = "f211a2250af488644b66a17fc05ae350";
//        String openWeatherApiUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
//        String mapBaseUrl = "https://www.mapquestapi.com/staticmap/v5/map";
//        String mapKey = "N39iOmpm6KwTBEN7r5uHmbgEmNG4rhtg";
//
//        public JSONObject getPlace(String city) {
//
//            String jsonResponse = "";
//            System.out.println(city);
//            try {
//                openWeatherApiUrl += city + "&appid=" + openWeatherApiKey;
//                URL url = new URL(openWeatherApiUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    String line;
//                    StringBuilder response = new StringBuilder();
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    reader.close();
//
//                    jsonResponse = response.toString();
//                    // System.out.println(jsonResponse);
//                } else {
//                    System.out.println("Error: " + responseCode);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return new JSONObject(jsonResponse);
//        }
//
//        public String getWeatherElements(JSONObject ob, String elem) {
//            // Get the weather array
//            JSONArray weatherArray = (JSONArray) ob.get("weather");
//
//            // Get the first weather object
//            JSONObject weatherObject = (JSONObject) weatherArray.get(0);
//
//            // Get the value of the "elem" field
//            String weather = (String) weatherObject.get(elem);
//            return weather;
//        }
//
//        public String getMainElements(JSONObject ob, String elem) {
//            // Get the "main" object
//            JSONObject mainObject = (JSONObject) ob.get("main");
//
//            // Extract the temperature value
//            Object main = mainObject.get(elem);
//
//            return String.valueOf(main);
//        }
//
//        public void generateWeatherIcon(String icon) {
//            // Get the icon URL
//            String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
//            // Download the icon image
//            try {
//                URL url = new URL(iconUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
//                    File file = new File("weather_icon.png");
//                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
//                        byte[] buffer = new byte[1024];
//                        int bytesRead;
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            outputStream.write(buffer, 0, bytesRead);
//                        }
//                    }
//                    System.out.println("Weather icon saved successfully!");
//                }
//                connection.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void generateMapIcon(JSONObject ob) {
//
//            // Get the "coord" object
//            JSONObject coordObject = (JSONObject) ob.get("coord");
//
//            // Extract the latitude and longitude values
//            BigDecimal latitude = (BigDecimal) coordObject.get("lat");
//            BigDecimal longitude = (BigDecimal) coordObject.get("lon");
//
//            String center = String.valueOf(latitude) + "," + String.valueOf(longitude); // latitude,longitude of center point
//            int zoom = 14;
//            int width = 600;
//            int height = 400;
//            String markers = "marker-red-A|" + center; // color-label-letter|latitude,longitude of marker
//
//            String urlString = mapBaseUrl + "?key=" + mapKey + "&center=" + center + "&zoom=" + zoom + "&size=" + width + "," + height + "&locations=" + markers;
//            System.out.println(urlString);
//
//            try {
//                URL url = new URL(urlString);
//                BufferedImage image = ImageIO.read(url);
//                ImageIO.write(image, "png", new File("map.png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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

        public synchronized int getIndex() {
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

    public class Pair<K, V> implements Serializable {
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
        public boolean equals(Object obj) {
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

        public void setId(int id) {
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

        public synchronized void addResult(int key, Results value) {
            this.resultList.put(key, value);
        }

        public synchronized void updateStatistics(double distance, double time, double elevation) {
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

        public ArrayList<Waypoint> parse(InputStream file) {
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

                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        // Waypoint's elements
                        Element element = (Element) node;
                        //Init waypoint 
                        String user = root.getAttribute("creator");
                        Double lon = Double.parseDouble(element.getAttribute("lon"));
                        Double lat = Double.parseDouble(element.getAttribute("lat"));
                        Double ele = Double.parseDouble(element.getElementsByTagName("ele").item(0).getTextContent());
                        DateTimeFormatter formatter = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        }
                        LocalDateTime time = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            time = LocalDateTime.parse(element.getElementsByTagName("time").item(0).getTextContent(), formatter);
                        }
                        Waypoint wpt = new Waypoint(user, lon, lat, ele, time);

                        wpt_list.add(wpt);
                        //System.out.println("User_id: " + user + " " + " Wpt_id: " + i);
                    }
                }
            } catch (ParserConfigurationException p) {
                System.err.println("Error: a DocumentBuilder cannot be created in Gpx Parser which satisfies the configuration requested.");
            } catch (SAXException s) {
                System.err.println("Error: parse errors occurin Gpx Parser");
            } catch (IOException io) {
                System.err.println("Error: In Gpx Parser please check the content of the file or the implementation of parsing");
            }
            return wpt_list;
        }
    }

    class RequestHandler extends Thread {

        ObjectInputStream in;

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
                Chunk request = (Chunk) this.in.readObject();
                int choice = request.getTypeId();

                switch (choice)
                {
                    case 1: 
                        System.out.println(request);
                        segmentDAO.addRecord(request.getSegmentId(), request);
                        break;
                    case 2:
                        //  results handler
                        int inputFileId = request.getKey();
                        int size;
                        synchronized (Master.intermediate_results) {
                            // Add the intermediate result to the list
                            // 1st getKey is for chunk and 2nd getKey is for Pair
                            Master.intermediate_results.get(inputFileId).getKey().add(request);
                            size = Master.intermediate_results.get(inputFileId).getValue();
                            Master.intermediate_results.get(inputFileId).setValue(--size);
        
                            // If size == 0 then send signal to the Master and remove the element
                            if (size == 0) {
                                synchronized (Master.clientLHandlers) {
                                    // Doesn't need synchronized because there is only one client action per request
                                    Master.clientLHandlers.get(inputFileId).setIntermResults(Master.intermediate_results.get(inputFileId).getKey());
        
                                    // Delete the file record from the database
                                    Master.intermediate_results.remove(inputFileId);
                                    Master.clientLHandlers.remove(inputFileId);
                                }
        
                            }
                        }
                        break;
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
        // Given from the Master : Global request number
        private int requestId;
        // An arraylist that contains all the intermediate results of the specific file from Master
        ArrayList<Chunk> interResults;
        // Monitor
        Object lock;
        // Menu choice
        private int choice;
        private int num_of_workers;
        private final ParserGPX parser = new ParserGPX();

        public ClientAction(Socket connection, int request, int num_of_wpt, int num_of_workers) {
            try {
                // Socket connection to listen from the client
                this.socket = connection;
                // File id is the global variable and we use it as a key in  the chunk
                this.requestId = request;
                this.num_of_wpt = num_of_wpt;
                // Monitor
                this.lock = new Object();
                this.num_of_workers = num_of_workers;
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

        private void receiveFile() {
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
            ArrayList<Waypoint> filewpt = new ArrayList<Waypoint>(wpt_list);

            // Check if there are segments and sent them if they exists
            sendSegments(new ArrayList<Waypoint>(filewpt));

            while (wpt_list.size() != 0) {
                int endIndex = Math.min(this.num_of_wpt - 1, wpt_list.size());
                List<Waypoint> list = wpt_list.subList(0, endIndex);
                // Create key to pass it in the hashmap in the reducer & in the chunk in order to use it 
                // to access the hashmap
                Chunk sublist = new Chunk(2, this.requestId, num_chunk, this.userId, this.fileId);

                int k = 0;
                for (k = 0; k < endIndex; k++) {
                    sublist.add((Waypoint) list.remove(0));
                }
                if (wpt_list.size() != 0) {
                    // Put the the first waypoint of the next chunk
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
            intermediate_results.put(this.requestId, new Pair<ArrayList<Chunk>, Integer>(new ArrayList<Chunk>(), num_chunk));

            for (Chunk c : chunks) {
                //send chunk in RR sequence with random gpx order
                ObjectOutputStream outstream;

                outstream = workerHandlers.get();

                try {
                    // Sync in order to send a chunk in the worker 
                    // but if two or more client threads have the same outstream, lock it
                    synchronized (outstream) {
                        outstream.writeObject(c);
                        outstream.flush();
                    }

                } catch (IOException e) {
                    System.err.println("Error in sending the chunk to the worker of the User: " + c.getUserId() + " and from the file: " + c.getFileId());
                }
            }
        }

        private void sendSegments(ArrayList<Waypoint> wptFile) {
            for (Map.Entry<Integer, ArrayList<Waypoint>> entry : Master.segments.entrySet()) {
                checkSegment(entry.getValue(), wptFile, entry.getKey());
            }
        }

        private void checkSegment(ArrayList<Waypoint> segment, ArrayList<Waypoint> wptFile, int segmentId) {
            int segmentIdx = 0;
            int fileIdx = 0;
            Chunk segmChunk = new Chunk(1, segmentId, 0, this.userId, this.fileId);
            // System.out.println(this.userId);
            while (fileIdx < wptFile.size()) {
                //System.out.println(segment.get(segmentIdx).distance(wptFile.get(fileIdx)));
                //System.out.println(segmentId + " " + fileIdx + " " + segmentIdx + " " + segment.size());
                if ((segment.get(segmentIdx).distance(wptFile.get(fileIdx)) * 1000) <= 8) {
                    segmChunk.add(wptFile.get(fileIdx));
                    segmentIdx++;
                    if (segmentIdx == segment.size()) {
                        try {
                            segmentIdx = 0;
                            // System.out.println(segmentId);
                            ObjectOutputStream outstream = workerHandlers.get();
                            // Sync in order to send a chunk in the worker 
                            // but if two or more client threads have the same outstream, lock it
                            synchronized (outstream) {
                                segmChunk.setSegmentId(segmentId);                                
                                outstream.writeObject(segmChunk);
                                outstream.flush();
                            }
                        } catch (IOException e) {
                            System.err.println("Error in sending the chunk - segment to the worker of the User: ");
                        }
                    }
                } else {
                    segmentIdx = 0;
                    segmChunk = new Chunk(1, segmentId, 0, this.userId, this.fileId);
                    if (segment.get(segmentIdx).distance(wptFile.get(fileIdx)) * 1000 <= 8) {
                        segmChunk.add(wptFile.get(fileIdx));
                        segmentIdx++;
                    }
                }
                fileIdx++;
            }
        }

        private boolean checkBuffer() {
            try {
                if (workerHandlers.size() < this.num_of_workers) {
                    this.out.writeInt(0);
                    this.out.flush();
                    return false;
                } else {
                    this.out.writeInt(1);
                    this.out.flush();
                }
            } catch (IOException e) {
                System.err.println("Error in checking Master.WorkerHandlers");
            }
            return true;
        }

        private void setIds() {
            // Listen from the client it's id and the file id 
            try {
                this.userId = this.in.readInt();
                this.fileId = this.in.readInt();
            } catch (IOException e) {
                System.err.println("Error in reading the ids from the user in file reading");
            }
        }

        private void create_user(int user) {
            if (userList.get(user) == null) {
                userList.put(user, new User(user));

                // Update globalSize in statistics
                statistics.addGlobalSize();
            }
        }

        public void setIntermResults(ArrayList<Chunk> list) {
            this.interResults = list;
            synchronized (this.lock) {
                this.lock.notify();
            }
        }

        private Results reduceResults() {
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

            //Results results = new Results(this.fileId, this.userId, distanceResult, avgSpeedResult, elevationResult, timeInSeconds);
            //TODO FIX RESULTS
            return null;//results;
        }

        private void sendResults() {
            if (this.interResults == null) {
                synchronized (this.lock) {
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
                // register the new route for the Database
                userList.get(this.userId).addResult(this.fileId, results);

                // update the personal record
                userList.get(this.userId).updateStatistics(results.getTotalDistance(), results.getTotalTime(), results.getTotalElevation());
                
                // Update statistics
                statistics.updateValues(results.getTotalTime(), results.getTotalDistance(), results.getTotalElevation());

                this.out.writeObject(results);
                this.out.flush();
            } catch (IOException e) {
                System.err.println("error in sending the results of the user : " + this.userId + " & file: " + this.fileId);
            }
        }

        private void uploadStatistics() {
            try {
                // Read user Id
                this.userId = this.in.readInt();
                User user = Master.userList.get(this.userId);

                if (user == null) {
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

                for (Map.Entry<Integer, Results> tuple : user.getResultList().entrySet()) {
                    totalDistance = totalDistance + tuple.getValue().getTotalDistance();
                    totalTime = totalTime + tuple.getValue().getTotalTime();
                    totalElevation = totalElevation + tuple.getValue().getTotalElevation();
                }

                // Create a replica to avoid synchronizationa problems with different actions 
                // that may alter the statistic (global) variable
                Statistics stat;
                synchronized (statistics) {
                    stat = new Statistics(statistics);
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

//        private void checkWeather() {
//            try {
//                String city = (String) this.in.readObject();
//                OpenWeatherAPI api = new OpenWeatherAPI();
//                JSONObject place = api.getPlace(city);
//
//                // Kelvin -> Celsius
//                String temperature = String.valueOf(Float.parseFloat(api.getMainElements(place, "temp")) - 273.15);
//                String pressure = api.getMainElements(place, "pressure");
//                String humidity = api.getMainElements(place, "humidity");
//                String main = api.getWeatherElements(place, "main");
//                String description = api.getWeatherElements(place, "description");
//
//                Weather weather = new Weather(temperature, pressure, humidity, main, description, city);
//
//                this.out.writeObject(weather);
//                this.out.flush();
//            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
//            }
//
//        }

        private void makeLeaderboard() {
            try {
                int segmentId = this.in.readInt();

                ArrayList<Chunk> leaderboard = new ArrayList<>(Master.segmentDAO.orderByTime(segmentId));

                this.out.writeObject(leaderboard);

            } catch (IOException e) {
                System.err.println("Something went wrong while sending the leaderboard");
            }

        }

        @Override
        public void run() {
            /*
            Menu :
                1. Receive file
                2. Send statistics
            */
            try {
                this.choice = this.in.readInt();
                switch (this.choice) {
                    case 1:
                        // Check workerHandler size
                        boolean workersOK = checkBuffer();
                        if (!workersOK) {
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
                    case 3:
                        // Check weather 
                        //checkWeather();
                        break;
                    case 4:
                        // Check segment statistics in leaderboard
                        makeLeaderboard();
                        break;

                }
                this.in.close();
                this.out.close();
                this.socket.close();
            } catch (IOException e) {
                System.err.println("Connection Error : Master <--> Client");
            }
        }
    }

    void openServer() {
        try {
            clientSocket = new ServerSocket(user_port, 4);
            workerSocket = new ServerSocket(Master.worker_port, 4);
            reducerSocket = new ServerSocket(reducer_port, 4);

            Thread client = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket connectionSocket = clientSocket.accept();
                        ClientAction clienThread = new ClientAction(connectionSocket, requestNo, num_of_wpt, num_of_workers);
                        System.out.println("accept");
                        clientLHandlers.put(requestNo, clienThread);
                        requestNo++;
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
                        workerHandlers.add(new ObjectOutputStream(communicationSocket.getOutputStream()));
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

    public void initDefault() {
        Properties prop = new Properties();
        String fileName = "pacepal/config/master.cfg";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException ex) {
            System.out.println("File not found !!!");
        }

        Master.user_port = Integer.parseInt(prop.getProperty("user_port"));
        Master.worker_port = Integer.parseInt(prop.getProperty("worker_port"));
        Master.reducer_port = Integer.parseInt(prop.getProperty("reducer_port"));
        Master.num_of_wpt = Integer.parseInt(prop.getProperty("num_wpt"));

        Master.num_of_workers = Integer.parseInt(prop.getProperty("num_of_workers"));
        Master.workerHandlers = new RobinQueue<>();
        Master.userList = new SynchronizedHashMap<>();
        Master.clientLHandlers = new SynchronizedHashMap<>();
        Master.intermediate_results = new SynchronizedHashMap<>();
        Master.segments = new SynchronizedHashMap<>();
        Master.statistics = new Statistics();
        //Master.api = new OpenWeatherAPI();
        Master.segmentDAO = new SegmentDAO();
        ParserGPX gpParser = new ParserGPX();

        try {
            ArrayList<Waypoint> segment1 = gpParser.parse(new FileInputStream("pacepal/gpxs/gpxs/segment1.gpx"));
            ArrayList<Waypoint> segment2 = gpParser.parse(new FileInputStream("pacepal/gpxs/gpxs/segment2.gpx"));
            Master.segments.put(0, segment1);
            Master.segments.put(1, segment2);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public Master() {}

    public static void main(String[] args) {
        Master mas = new Master();
        mas.initDefault();
        mas.openServer();
    }
}
