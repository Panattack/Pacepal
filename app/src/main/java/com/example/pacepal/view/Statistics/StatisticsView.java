package com.example.pacepal.view.Statistics;

import java.util.HashMap;

public interface StatisticsView {
    public void alertBox(String title, String message);

    public void createTimeBar(HashMap<String, Double> map);

    public void createDistanceBar(HashMap<String, Double> map);

    public void createElevationBar(HashMap<String, Double> map);
}
