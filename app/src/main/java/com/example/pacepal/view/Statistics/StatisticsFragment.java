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
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsFragment extends Fragment implements StatisticsView {
    StatisticsPresenter presenter;
    BarChart timeChart;
    BarChart elevationChart;
    BarChart distanceChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        timeChart = view.findViewById(R.id.timeBar);
        elevationChart = view.findViewById(R.id.elevationBar);
        distanceChart = view.findViewById(R.id.distanceBar);
        presenter = new StatisticsPresenter(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            presenter.receiveStatistics();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void alertBox(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null).create().show();
    }

    @Override
    public void createTimeBar(HashMap<String, Double> map) {
        List<BarEntry> entriesTime = new ArrayList<>();
        entriesTime.add(new BarEntry(0f, Float.parseFloat(String.valueOf(map.get("globalAvgTime"))), "Global Avg Time")); // First bar
        entriesTime.add(new BarEntry(1f, Float.parseFloat(String.valueOf(map.get("totalTime"))), "Personal Total Time")); // Second bar
        String[] labelsTime = new String[]{"Global Avg Time", "Personal Total Time"};

        BarDataSet dataTime = new BarDataSet(entriesTime, "Global Avg Time & Personal Total Time");
        dataTime.setStackLabels(labelsTime);
        dataTime.setColors(Color.rgb(98, 175, 144), Color.rgb(255, 102, 150));
        dataTime.setValueTextColor(Color.BLACK);
        dataTime.setValueTextSize(16f);
        BarData barTime = new BarData(dataTime);
        timeChart.setData(barTime);
        timeChart.getDescription().setEnabled(false);
        timeChart.invalidate();
    }

    @Override
    public void createDistanceBar(HashMap<String, Double> map) {
        List<BarEntry> entriesDistance = new ArrayList<>();
        entriesDistance.add(new BarEntry(0f, Float.parseFloat(String.valueOf(map.get("globalAvgDistance"))), "Global Avg Distance")); // First bar
        entriesDistance.add(new BarEntry(1f, Float.parseFloat(String.valueOf(map.get("totalDistance"))), "Personal Total Distance")); // Second bar
        // String[] labelsTime = new String[]{"Global Avg Time", "Personal Total Time"};

        BarDataSet dataDistance = new BarDataSet(entriesDistance, "Global Avg Distance & Personal Total Distance");
        // dataTime.setStackLabels(labelsTime);
        dataDistance.setColors(Color.rgb(199, 199, 8), Color.rgb(102, 102, 255));
        dataDistance.setValueTextColor(Color.BLACK);
        dataDistance.setValueTextSize(16f);
        BarData barTime = new BarData(dataDistance);
        distanceChart.setData(barTime);
        distanceChart.getDescription().setEnabled(false);
        distanceChart.invalidate();
    }

    @Override
    public void createElevationBar(HashMap<String, Double> map) {
        List<BarEntry> entriesElevation = new ArrayList<>();
        entriesElevation.add(new BarEntry(0f, Float.parseFloat(String.valueOf(map.get("globalAvgElevation"))), "Global Avg Elevation")); // First bar
        entriesElevation.add(new BarEntry(1f, Float.parseFloat(String.valueOf(map.get("totalElevation"))), "Personal Total Elevation")); // Second bar
        // String[] labelsTime = new String[]{"Global Avg Elevation", "Personal Total Elevation"};

        BarDataSet dataElevation = new BarDataSet(entriesElevation, "Global Avg Elevation & Personal Total Elevation");
        // dataTime.setStackLabels(labelsTime);
        dataElevation.setColors(Color.rgb(153, 0, 153), Color.rgb(255, 153, 51));
        dataElevation.setValueTextColor(Color.BLACK);
        dataElevation.setValueTextSize(16f);
        BarData barTime = new BarData(dataElevation);
        elevationChart.setData(barTime);
        elevationChart.getDescription().setEnabled(false);
        elevationChart.invalidate();
    }
}