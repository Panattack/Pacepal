package com.example.pacepal.view.results;

import com.example.pacepal.dao.ResultDAO;
import com.example.pacepal.model.Results;

import java.util.HashMap;
import java.util.List;

public class ResultsPresenter {


    private ResultDAO results;
    private ResultsView view;

    public ResultsPresenter(ResultsView view, ResultDAO r)
    {
        this.results = r;
        this.view = view;
    }

    public void initViewOfResults()
    {
        HashMap<String, String> map = new HashMap<>();
        List<Results> res = results.findAll();
        for(Results r:res)
        {
            map.put("GPX id", String.valueOf(r.getGpx_id()));
            map.put("User id", String.valueOf(r.getUser_id()));
            map.put("Average Speed", String.valueOf(r.getAvgSpeed()));
            map.put("Total Elevation", String.valueOf(r.getTotalElevation()));
            map.put("Total Time", String.valueOf(r.getTotalTime()));
            view.viewResults(map);
        }

    }


}
