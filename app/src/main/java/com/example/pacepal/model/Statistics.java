package com.example.pacepal.model;
import java.io.Serializable;
import java.text.DecimalFormat;

public class Statistics implements Serializable {
    
    private double globalAvgTime;
    private double globalAvgDistance;
    private double globalAvgElevation;
    private int globalSize;
    private int preSize;

    private double pcDistance;
    private double pcTime;
    private double pcElevation;

    public Statistics() {
        this.globalAvgTime = 0.0;
        this.globalAvgDistance = 0.0;
        this.globalAvgElevation = 0.0;
        this.globalSize = 0;
        this.preSize = 0;
    }

    public Statistics(Statistics other) {
        this.globalAvgTime = other.globalAvgTime;
        this.globalAvgDistance = other.globalAvgDistance;
        this.globalAvgElevation = other.globalAvgElevation;
        this.globalSize = other.globalSize;
        this.preSize = other.preSize;
    }

        
    public double getPcDistance() {
        return pcDistance;
    }

    public double getPcTime() {
        return pcTime;
    }

    public double getPcElevation() {
        return pcElevation;
    }

    public synchronized double getGlobalAvgTime() 
    {
        return globalAvgTime;
    }

    public synchronized void setGlobalAvgTime(double globalAvgTime) 
    {
        this.globalAvgTime = globalAvgTime;
    }
    public synchronized double getGlobalAvgDistance() 
    {
        return globalAvgDistance;
    }

    public synchronized void setGlobalAvgDistance(double globalAvgDistance) 
    {
        this.globalAvgDistance = globalAvgDistance;
    }

    public synchronized double getGlobalAvgElevation() 
    {
        return globalAvgElevation;
    }

    public synchronized void setGlobalAvgElevation(double globalAvgElevation) 
    {
        this.globalAvgElevation = globalAvgElevation;
    }

    public synchronized int getGlobalSize() {
        return globalSize;
    }

    public synchronized void setGlobalSize(int globalSize) 
    {
        this.globalSize = globalSize;
    }

    public synchronized void addGlobalSize()
    {
        this.globalSize++;
    }
    // Only defined in user request -- copy statistic
    public void defUS(double distance, double elevation, double time)
    {
        this.pcDistance = distance;
        this.pcElevation = elevation;
        this.pcTime = time;
    }

    public synchronized void updateValues(double time, double distance, double elevation) 
    {
        this.globalAvgTime = (this.globalAvgTime * this.preSize + time) / (this.globalSize);
        this.globalAvgDistance = (this.globalAvgDistance * this.preSize + distance) / (this.globalSize);
        this.globalAvgElevation = (this.globalAvgElevation * this.preSize + elevation) / (this.globalSize);

        this.preSize = this.globalSize;
    }

    @Override
    public String toString()
    {
        DecimalFormat df = new DecimalFormat("##.##");

        return "\n"  
        + "Global Distance : " + Float.valueOf(df.format(this.globalAvgDistance)) + " km" + "\n" 
        + "Global Elevation : " + Float.valueOf(df.format(this.globalAvgElevation)) + " m" + "\n" 
        + "Global Hours: " + (int) (Float.valueOf(df.format(this.globalAvgTime)) /3600) + " Global Minutes: " + (int) (Float.valueOf(df.format(this.globalAvgTime)) % 3600) / 60 + " Global Seconds: " + (int) (Float.valueOf(df.format(this.globalAvgTime)) % 60) + "\n";
    }
}
