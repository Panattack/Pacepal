package com.example.pacepal.view.results;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pacepal.R;
import com.example.pacepal.dao.Initializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultsFragment extends Fragment implements ResultsView{


    private ResultsPresenter presenter;
    Initializer init;
    LinearLayout container_of_image_and_second_linear;
    View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_results, container, false);
        container_of_image_and_second_linear =(LinearLayout) myView.findViewById(R.id.linear_layout_);
        presenter = new ResultsPresenter(this, init.getResultDAO());

        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.initViewOfResults();
    }


    @Override
    public void viewResults(HashMap<String, String> results)
    {

        // Create the parent LinearLayout (IMAGE AND RESULTS CONTAINER)
        LinearLayout parentLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parentLayoutParams.setMargins(0, 30, 0, 0);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        parentLayout.setLayoutParams(parentLayoutParams);

        // Create the ImageView
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.mipmap.blue_runner_mine_foreground);
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageLayoutParams);

        // Create the child LinearLayout (RESULTS CONTAINER)
        LinearLayout childLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        childLayoutParams.setMargins(15, 0, 0, 0);
        childLayout.setOrientation(LinearLayout.VERTICAL);
        childLayout.setLayoutParams(childLayoutParams);


        //getting the appropriate values
        for (Map.Entry<String, String> entry : results.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            TextView textView = new TextView(getActivity());
            textView.setText(key+": "+value);
            textView.setTextSize(20);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(textLayoutParams);

            childLayout.addView(textView);
        }
        // Add the ImageView and child LinearLayout to the parent LinearLayout
        parentLayout.addView(imageView);
        parentLayout.addView(childLayout);


// Add the parent LinearLayout to the fragment's root view
        //View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        container_of_image_and_second_linear.addView(parentLayout);
        //return rootView;
    }

}