import java.io.Serializable;
import java.text.DecimalFormat;

public class Results  implements Serializable {
    
    private int gpx_id;
    private int user_id;

    private double totalDistance ;
    private double avgSpeed;
    private double totalElevation;
    private double totalTime;
    DecimalFormat df = new DecimalFormat("##.##");
    
    public Results (Double d, Double s, Double e, Double t, int gid, int uid)
    {
        this.totalDistance = d;
        this.avgSpeed = s;
        this.totalElevation = e;
        this.totalTime = t;

        this.gpx_id = gid;
        this.user_id = uid;
    }
    
    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public Double getTotalElevation() {
        return totalElevation;
    }

    public void setTotalElevation(Double totalElevation) {
        this.totalElevation = totalElevation;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public int getGpx_id() {
        return gpx_id;
    }

    public void setGpx_id(int gpx_id) {
        this.gpx_id = gpx_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
   
    public String toString()
    {
        return "\n" + "User_id: " + user_id + " Gpx: " + gpx_id + "\n" + 
        "Total Dist: " + Float.valueOf(df.format(totalDistance)) + " km" + "\n" + 
        "Avg Speed: " + Float.valueOf(df.format(avgSpeed)) + " km per hour" + "\n" +
        "Total Elev: " + Float.valueOf(df.format(totalElevation)) + " m" + "\n" +
        "Hours: " + (int) totalTime / 3600 + " Minutes: " + (int) (totalTime%3600) / 60 + " Seconds: " + (int) totalTime % 60 + "\n";
    }
}
