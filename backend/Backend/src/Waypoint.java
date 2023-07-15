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

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
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

    public double distance(Waypoint wpt2) {
        double EARTH_RADIUS_KM = 6371.0;
        double dLat = Math.toRadians(wpt2.getLat() - this.getLat());
        double dLon = Math.toRadians(wpt2.getLon() - this.getLon());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(this.getLat())) * Math.cos(Math.toRadians(wpt2.getLat())) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;
        return distance;
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
