package com.example.pacepal.model;
import java.io.Serializable;
import java.text.DecimalFormat;

public record Results(int gpx_id, int user_id, double totalDistance, double avgSpeed, double totalElevation, double totalTime) implements Serializable {
    
    public Double getTotalDistance() {
        return totalDistance;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public Double getTotalElevation() {
        return totalElevation;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public int getGpx_id() {
        return gpx_id;
    }

    public int getUser_id() {
        return user_id;
    }
   
    public String toString()
    {
        DecimalFormat df = new DecimalFormat("##.##");
        return "\n" + "User_id: " + user_id + " Gpx: " + gpx_id + "\n" + 
        "Total Dist: " + Float.valueOf(df.format(totalDistance)) + " km" + "\n" + 
        "Avg Speed: " + Float.valueOf(df.format(avgSpeed)) + " km per hour" + "\n" +
        "Total Elev: " + Float.valueOf(df.format(totalElevation)) + " m" + "\n" +
        "Hours: " + (int) totalTime / 3600 + " Minutes: " + (int) (totalTime%3600) / 60 + " Seconds: " + (int) totalTime % 60 + "\n";
    }
}
