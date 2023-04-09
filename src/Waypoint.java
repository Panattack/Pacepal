import java.time.LocalDateTime;
import java.io.*;

public class Waypoint implements Serializable {
    private String user;
    private double lon;
    private double lat;
    private double ele;
    private LocalDateTime time;

    public Waypoint(String user, double lon, double lat, double ele, LocalDateTime time)
    {
        this.user = user;
        this.lon = lon;
        this.lat = lat;
        this.ele = ele;
        this.time = time;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getEle() {
        return ele;
    }

    public void setEle(double ele) {
        this.ele = ele;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString()
    {
        return "User: " + this.user + 
        " lon: " + this.lon + 
        " lat: " + this.lat + 
        " ele: " + this.ele +
        " time: " + this.time.toString();
    }
}
