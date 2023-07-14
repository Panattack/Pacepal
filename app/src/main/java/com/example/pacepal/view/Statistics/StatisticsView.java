package com.example.pacepal.view.Statistics;

import java.util.HashMap;

public interface StatisticsView {
    /**
     * Shows an alert box
     *
     * @param title   the title of the info as a string
     * @param message the message of the info as a string
     */
    public void alertBox(String title, String message);

    /**
     * Creates the time bar chart
     *
     * @param map a hashmap that contains the global avg time, personal total time and the difference in percentage
     */
    public void createTimeBar(HashMap<String, Double> map);

    /**
     * Creates the distance bar chart
     *
     * @param map a hashmap that contains global avg distance, total distance and the difference in percentage
     */
    public void createDistanceBar(HashMap<String, Double> map);

    /**
     * Creates the elevation bar chart
     *
     * @param map a hashmap that contains the global avg elevation, personal total elevation and the difference in percentage
     */
    public void createElevationBar(HashMap<String, Double> map);
}
