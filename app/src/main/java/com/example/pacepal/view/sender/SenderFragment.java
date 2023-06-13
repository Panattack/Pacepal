package com.example.pacepal.view.sender;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO:Check the sender layout to make the boxes better in size
        View view = inflater.inflate(R.layout.fragment_sender, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);
        this.submitButton = view.findViewById(R.id.submitBtn);
        this.senderPresenter = new SenderPresenter(this);
        return view;
    }

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

    @Override
    public void popMsg(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void alertBox(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null).create().show();

    }

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
