package com.example.pacepal.view.results;

import com.example.pacepal.dao.ResultDAO;
import com.example.pacepal.model.Results;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class ResultsPresenter {
    private ResultDAO results;
    private ResultsView view;

    /**
     * Constructor that initializes the variables
     *
     * @param view the view that will be used to call the methods in the activity
     * @param r    the dao that contains results
     */
    public ResultsPresenter(ResultsView view, ResultDAO r) {
        this.results = r;
        this.view = view;
    }

    /**
     * Initializes the result boxes with the help of the dao
     */
    public void initViewOfResults() {
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        List<Results> res = results.findAll();
        for (Results r : res) {
            HashMap<String, String> map = new HashMap<>();
            map.put("GPX id", String.valueOf(r.getGpx_id()));
            map.put("Average Speed", decimalFormat.format(r.getAvgSpeed()) + " m/s");
            map.put("Total Elevation", decimalFormat.format(r.getTotalElevation()));
            map.put("Total Distance", decimalFormat.format(r.getTotalDistance()) + " km");
            map.put("Total Hours ", String.valueOf((int) (Float.parseFloat(decimalFormat.format(r.getTotalTime())) / 3600)));
            map.put("Total Minutes", String.valueOf((int) (Float.parseFloat(decimalFormat.format(r.getTotalTime())) % 3600) / 60));
            map.put("Total seconds", String.valueOf((int) (Float.parseFloat(decimalFormat.format(r.getTotalTime())) % 60)));
            view.viewResults(map);
        }
    }
}
