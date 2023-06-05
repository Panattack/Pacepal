package com.example.pacepal.messages;

import java.io.Serializable;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class Chunk implements Serializable{
    /* Chunk must implement Serializable in order to pass it in stream */

    /*
     * typeId : 
     *  1 -> segment
     *  2 -> file
     */
    private int typeId;
    private int segmentId;
    private ArrayList<Waypoint> ls_wpt;
    private int number;
    private int fileId;
    private int userId;
    private int key;

    // Output of mapper 
    // results: [totalDistance, totalTimeInHours, averageSpeed, totalElevation]
    private double totalDistance;
    private double totalTimeInHours;
    private double averageSpeed;
    private double totalElevation;
    private double totalTimeInSeconds;

    public Chunk(int typeId, int key, int num, int userId, int fileId)
    {
        this.typeId = typeId;
        // Keep the number and id of user
        this.key = key;
        this.userId = userId;
        this.fileId = fileId;
        this.number = num;
        ls_wpt = new ArrayList<Waypoint>();
    }

    public Chunk(Chunk other) {
        this.ls_wpt = other.ls_wpt;
        this.key = other.key;
        this.number = other.getNum();
        this.fileId = other.getFileId();
        this.userId = other.getUserId();
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getFileId() {
        return this.fileId;
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

    public double getTotalTimeInSeconds() {
        return this.totalTimeInSeconds;
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

    public void calcStatistics() {

        this.totalDistance = 0;
        this.totalElevation = 0;
        this.totalTimeInSeconds = 0;
        this.averageSpeed = 0;

        for (int i = 1; i < this.ls_wpt.size(); i++) {
            Waypoint prev = this.ls_wpt.get(i-1);
            Waypoint curr = this.ls_wpt.get(i);
            
            double distance = prev.distance(curr);
            // System.out.println(distance);
            double elevationGain = Math.max(0, curr.getEle() - prev.getEle()); // ignore elevation loss
            // long timeInSeconds = ChronoUnit.SECONDS.between(prev.getTime(), curr.getTime());
            long timeInSeconds = (curr.getTime().toEpochSecond(ZoneOffset.UTC) - prev.getTime().toEpochSecond(ZoneOffset.UTC));
            this.totalDistance += distance;
            this.totalElevation += elevationGain;
            this.totalTimeInSeconds += timeInSeconds;
        }

        this.totalTimeInHours = this.totalTimeInSeconds / 3600.0;

        if (this.totalTimeInSeconds != 0) {
            this.averageSpeed = this.totalDistance / this.totalTimeInHours;
        }
    }

    public String toString() {
        return "Chunk Number: " + this.number + ", user id : " + this.userId + ", size : " + this.ls_wpt.size() + ", file id : " + this.fileId + " . ";
    }
}
