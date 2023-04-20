import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Chunk implements Serializable{
    /* Chunk must implement Serializable in order to pass it in stream */
    private ArrayList<Waypoint> ls_wpt;    
    private int number;
    private String user;
    private int id;

    // Output of mapper 
    // results:[totalDistance, totalTimeInHours, averageSpeed, totalElevation]
    private double totalDistance;
    private double totalTimeInHours;
    private double averageSpeed;
    private double totalElevation;
    
    public Chunk(int id, int num, String user)
    {
        // Keep the number and id of user
        this.id = id;
        this.number = num;
        this.user = user;
        ls_wpt = new ArrayList<Waypoint>();
    }

    public Chunk(Chunk other) {
        this.ls_wpt = other.ls_wpt;
        this.number = other.number;
        this.user = other.user;
        this.id = other.id;
    }
    public String getUser() {
        return this.user;
    }

    public int getId() {
        return this.id;
    }

    public int getNum() {
        return this.number;
    }

    public void add(Waypoint wpt)
    {
        this.ls_wpt.add(wpt);
    }

    public int size() {
        return this.ls_wpt.size();
    }

    public Waypoint get(int i) {
        return ls_wpt.get(i);
    }

    public double getTotalTime() {
        return this.totalTimeInHours;
    }

    public double getAvgSpeed() {
        return this.averageSpeed;
    }

    public double getTotalDistance() {
        return this.totalDistance;
    }

    public double getTotalElevation() {
        return this.totalElevation;
    }

    private double distance(Waypoint wpt1, Waypoint wpt2) {
        double EARTH_RADIUS_KM = 6371.0;
        double dLat = Math.toRadians(wpt2.getLat() - wpt1.getLat());
        double dLon = Math.toRadians(wpt2.getLon() - wpt1.getLon());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(wpt1.getLat())) * Math.cos(Math.toRadians(wpt2.getLat())) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;
        return distance;
    }

    public void calcStatistics() {

        this.totalDistance = 0;
        this.totalElevation = 0;
        long totalTimeInSeconds = 0;
        this.averageSpeed = 0;

        for (int i = 1; i < this.ls_wpt.size(); i++) {
            Waypoint prev = this.ls_wpt.get(i-1);
            Waypoint curr = this.ls_wpt.get(i);
            
            double distance = this.distance(prev, curr);
            // System.out.println(distance);
            double elevationGain = Math.max(0, curr.getEle() - prev.getEle()); // ignore elevation loss
            long timeInSeconds = ChronoUnit.SECONDS.between(prev.getTime(), curr.getTime());

            this.totalDistance += distance;
            this.totalElevation += elevationGain;
            totalTimeInSeconds += timeInSeconds;
        }

        this.totalTimeInHours = totalTimeInSeconds / 3600.0;

        if (totalTimeInSeconds != 0) {
            this.averageSpeed = this.totalDistance / totalTimeInHours;
        }
    }

    public String toString() {
        return "Chunk Number: " + this.number + ", user id : " + this.id + ", size : " + this.ls_wpt.size();
    }
}
