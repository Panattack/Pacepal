package com.example.pacepal.view.results;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pacepal.R;
import com.example.pacepal.dao.Initializer;
import com.example.pacepal.memorydao.MemoryInitializer;

import java.util.HashMap;
import java.util.Map;


public class ResultsFragment extends Fragment implements ResultsView {
    private ResultsPresenter presenter;
    Initializer init;
    LinearLayout container_of_image_and_second_linear;
    View myView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_results, container, false);
        init = new MemoryInitializer();

        container_of_image_and_second_linear = (LinearLayout) myView.findViewById(R.id.linear_layout_);
        presenter = new ResultsPresenter(this, init.getResultDAO());

        return myView;
    }

    /**
     * Calls the presenter and gets the results
     */
    @Override
    public void onStart() {
        super.onStart();
        presenter.initViewOfResults();
    }

    /**
     * Shows the results in a formatted way
     *
     * @param results a hashmap with key the id of the attribute and value the attribute
     */
    @Override
    public void viewResults(HashMap<String, String> results) {
        // Create the parent LinearLayout (IMAGE AND RESULTS CONTAINER)
        LinearLayout parentLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        parentLayoutParams.setMargins(50, 50, 50, 15);
        parentLayout.setOrientation(LinearLayout.HORIZONTAL);
        //parentLayout.setBackgroundColor(Color.WHITE);
        parentLayout.setLayoutParams(parentLayoutParams);

        // Set background with rounded corners
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(50); // Adjust the corner radius as desired
        gradientDrawable.setColor(Color.WHITE);
        parentLayout.setBackground(gradientDrawable);

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

        // Getting the appropriate values
        for (Map.Entry<String, String> entry : results.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();


            TextView textView = new TextView(getActivity());
            textView.setText(key + ": " + value);
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

        container_of_image_and_second_linear.addView(parentLayout);
    }
}