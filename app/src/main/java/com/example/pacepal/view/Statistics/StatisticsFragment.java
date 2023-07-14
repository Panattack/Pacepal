package com.example.pacepal.view.Statistics;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.pacepal.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsFragment extends Fragment implements StatisticsView {
    StatisticsPresenter presenter;
    BarChart timeChart;
    BarChart elevationChart;
    BarChart distanceChart;
    int serverPort;
    String host;
    int userId;

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        timeChart = view.findViewById(R.id.timeBar);
        elevationChart = view.findViewById(R.id.elevationBar);
        distanceChart = view.findViewById(R.id.distanceBar);
        Bundle arguments = getArguments();
        if (arguments != null) {
            userId = arguments.getInt("user_id");
            host = arguments.getString("host");
            serverPort = arguments.getInt("serverPort");
        }
        presenter = new StatisticsPresenter(this, serverPort, host, userId);

        return view;
    }

    /**
     * Calls the presenter to ask & receive the statistics from Master
     */
    @Override
    public void onStart() {
        super.onStart();
        try {
            presenter.receiveStatistics();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Shows an alert box
     *
     * @param title   the title of the info as a string
     * @param message the message of the info as a string
     */
    @Override
    public void alertBox(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null).create().show();
    }

    /**
     * Creates the time bar chart
     *
     * @param map a hashmap that contains the global avg time, personal total time and the difference in percentage
     */
    @Override
    public void createTimeBar(HashMap<String, Double> map) {
        List<BarEntry> entriesTime = new ArrayList<>();
        float globalAvgTime = Float.parseFloat(String.valueOf(map.get("globalAvgTime")));
        float totalTime = Float.parseFloat(String.valueOf(map.get("totalTime")));
        entriesTime.add(new BarEntry(0f, globalAvgTime, "Global Avg Time")); // First bar
        entriesTime.add(new BarEntry(1f, totalTime, "Personal Total Time")); // Second bar
        // String[] labelsTime = new String[]{"Global Avg Time", "Personal Total Time"};

        BarDataSet dataTime = new BarDataSet(entriesTime, "Global Avg Time & Personal Total Time in seconds");
        // dataTime.setStackLabels(labelsTime);
        dataTime.setColors(Color.rgb(98, 175, 144), Color.rgb(255, 102, 150));
        dataTime.setValueTextColor(Color.BLACK);
        dataTime.setValueTextSize(16f);
        BarData barTime = new BarData(dataTime);
        timeChart.setData(barTime);
        timeChart.getDescription().setEnabled(false);

        timeChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                alertBox("Time Bar Chart",
                        "Global Avg Time : " + globalAvgTime +
                                "\nTotal Time : " + totalTime +
                                "\nPercentage : " + map.get("pcTime"));
            }

            @Override
            public void onNothingSelected() {

            }
        });

        timeChart.invalidate();
    }

    /**
     * Creates the distance bar chart
     *
     * @param map a hashmap that contains global avg distance, total distance and the difference in percentage
     */
    @Override
    public void createDistanceBar(HashMap<String, Double> map) {
        List<BarEntry> entriesDistance = new ArrayList<>();
        float globalAvgDistance = Float.parseFloat(String.valueOf(map.get("globalAvgDistance")));
        float totalDistance = Float.parseFloat(String.valueOf(map.get("totalDistance")));
        entriesDistance.add(new BarEntry(0f, globalAvgDistance, "Global Avg Distance")); // First bar
        entriesDistance.add(new BarEntry(1f, totalDistance, "Personal Total Distance")); // Second bar
        // String[] labelsTime = new String[]{"Global Avg Time", "Personal Total Time"};

        BarDataSet dataDistance = new BarDataSet(entriesDistance, "Global Avg Distance & Personal Total Distance in kilometers");
        // dataTime.setStackLabels(labelsTime);
        dataDistance.setColors(Color.rgb(199, 199, 8), Color.rgb(102, 102, 255));
        dataDistance.setValueTextColor(Color.BLACK);
        dataDistance.setValueTextSize(16f);
        BarData barTime = new BarData(dataDistance);
        distanceChart.setData(barTime);
        distanceChart.getDescription().setEnabled(false);

        distanceChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                alertBox("Distance Bar Chart",
                        "Global Avg Distance : " + globalAvgDistance +
                                "\nTotal Distance : " + totalDistance +
                                "\nPercentage : " + map.get("pcDistance"));
            }

            @Override
            public void onNothingSelected() {
                // Handle event when nothing is selected (optional)
            }
        });

        distanceChart.invalidate();
    }

    /**
     * Creates the elevation bar chart
     *
     * @param map a hashmap that contains the global avg elevation, personal total elevation and the difference in percentage
     */
    @Override
    public void createElevationBar(HashMap<String, Double> map) {
        List<BarEntry> entriesElevation = new ArrayList<>();
        float globalAvgElevation = Float.parseFloat(String.valueOf(map.get("globalAvgElevation")));
        float totalElevation = Float.parseFloat(String.valueOf(map.get("totalElevation")));

        entriesElevation.add(new BarEntry(0f, globalAvgElevation, "Global Avg Elevation")); // First bar
        entriesElevation.add(new BarEntry(1f, totalElevation, "Personal Total Elevation")); // Second bar
        // String[] labelsTime = new String[]{"Global Avg Elevation", "Personal Total Elevation"};

        BarDataSet dataElevation = new BarDataSet(entriesElevation, "Global Avg Elevation & Personal Total Elevation in meters");
        // dataTime.setStackLabels(labelsTime);
        dataElevation.setColors(Color.rgb(153, 0, 153), Color.rgb(255, 153, 51));
        dataElevation.setValueTextColor(Color.BLACK);
        dataElevation.setValueTextSize(16f);
        BarData barTime = new BarData(dataElevation);
        elevationChart.setData(barTime);
        elevationChart.getDescription().setEnabled(false);

        elevationChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                alertBox("Elevation Bar Chart",
                        "Global Avg Elevation : " + globalAvgElevation +
                                "\nTotal Elevation : " + totalElevation +
                                "\nPercentage : " + map.get("pcElevation"));
            }

            @Override
            public void onNothingSelected() {

            }
        });

        elevationChart.invalidate();
    }
}