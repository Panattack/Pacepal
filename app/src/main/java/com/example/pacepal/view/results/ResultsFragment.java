package com.example.pacepal.view.results;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pacepal.R;


public class ResultsFragment extends Fragment {

    LinearLayout parentLayout;
    LinearLayout linearLayout;
    View view;
    int numFiles;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_results, container, false);
        parentLayout = view.findViewById(R.id.linear_layout_);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

}