package com.example.pacepal.view.sender;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pacepal.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SenderFragment extends Fragment implements SenderFragmentView {
    private TableLayout tableLayout;
    private Button submitButton;
    SenderPresenter senderPresenter;
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
        // TODO:Check the sender layout to make the boxes better in size
        View view = inflater.inflate(R.layout.fragment_sender, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);
        Bundle arguments = getArguments();
        if (arguments != null) {
            userId = arguments.getInt("user_id");
            host = arguments.getString("host");
            serverPort = arguments.getInt("serverPort");
        }
        this.submitButton = view.findViewById(R.id.submitBtn);
        this.senderPresenter = new SenderPresenter(this, serverPort, host, userId);
        return view;
    }

    /**
     * The presenter does two jobs:
     * 1. Loads the files from the download folder
     * 2. When the user clicks the submits button, the presenter sends the files to the Master server
     */
    @Override
    public void onStart() {
        super.onStart();
        senderPresenter.loadFilesFromDownloadFolder();
        submitButton.setOnClickListener(v -> {
            try {
                senderPresenter.submit();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Shows files with checkboxes to select
     *
     * @param files an arraylist that contains the titles of each file gpx
     */
    @Override
    public void showFiles(ArrayList<String> files) {
        tableLayout.setVisibility(View.VISIBLE); // shows the table

        tableLayout.removeAllViews(); // removes the old rows

        // Create the header row
        TableRow headerRow = new TableRow(requireContext());
        TableRow.LayoutParams headerLayoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        headerRow.setLayoutParams(headerLayoutParams);

        // Create the "Select" header
        TextView selectHeaderTextView = new TextView(requireContext());
        selectHeaderTextView.setText("Select");
        headerRow.addView(selectHeaderTextView);

        // Create the "Subject" header
        TextView subjectHeaderTextView = new TextView(requireContext());
        subjectHeaderTextView.setText("File");
        headerRow.addView(subjectHeaderTextView);

        // Add the header row to the table
        tableLayout.addView(headerRow);

        for (String title : files) {
            TableRow tableRow = new TableRow(requireContext());
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            tableRow.setLayoutParams(layoutParams);

            // Create the checkbox for the first column
            CheckBox checkBox = new CheckBox(requireContext());
            tableRow.addView(checkBox);

            // Create the TextView for the third column
            TextView thirdColumnTextView = new TextView(requireContext());
            thirdColumnTextView.setText(title);
            thirdColumnTextView.setTextSize(20);
            tableRow.addView(thirdColumnTextView);

            // Add the row to the table
            tableLayout.addView(tableRow);
        }

    }

    /**
     * Pops a message when an event occurs
     *
     * @param message the message as a string
     */
    @Override
    public void popMsg(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows an alert box when an event occurs
     *
     * @param title
     * @param message
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
     * After "submit" is clicked, it returns the names of the files that were selected
     *
     * @return the names of the files that were selected as strings
     */
    public ArrayList<String> submitClicked() {
        ArrayList<String> fileNames = new ArrayList<>();
        int rowCount = tableLayout.getChildCount();

        for (int i = 1; i < rowCount; i++) {
            View rowView = tableLayout.getChildAt(i);

            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                int columnCount = row.getChildCount();

                // Get the checkbox and string from the row
                CheckBox checkBox = (CheckBox) row.getChildAt(0);
                TextView textView = (TextView) row.getChildAt(1);

                if (checkBox.isChecked()) {
                    String selectedString = textView.getText().toString();
                    fileNames.add(selectedString);
                }
            }
        }
        return fileNames;
    }
}
