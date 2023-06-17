package com.example.pacepal.view.results;

import com.example.pacepal.dao.ResultDAO;
import com.example.pacepal.model.Results;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class ResultsPresenter {
    private ResultDAO results;
    private ResultsView view;

    public ResultsPresenter(ResultsView view, ResultDAO r) {
        this.results = r;
        this.view = view;
    }

    public void initViewOfResults() {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        List<Results> res = results.findAll();
        for (Results r : res) {
            HashMap<String, String> map = new HashMap<>();
            map.put("GPX id", String.valueOf(r.getGpx_id()));
            map.put("User id", String.valueOf(r.getUser_id()));
            map.put("Average Speed", String.valueOf(decimalFormat.format(r.getAvgSpeed())));
            map.put("Total Elevation", String.valueOf(decimalFormat.format(r.getTotalElevation())));
            map.put("Total Distance", String.valueOf(decimalFormat.format(r.getTotalDistance())));
            map.put("Total Time", String.valueOf(decimalFormat.format(r.getTotalTime())));
            view.viewResults(map);
        }
    }
}
