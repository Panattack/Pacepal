package com.example.pacepal.view.sender;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.pacepal.R;

import java.util.ArrayList;
import java.util.List;

public class SenderFragment extends Fragment {

    private LinearLayout container;
    private Button addButton;
    private Button sendButton;
    private List<EditText> inputList;
    SenderPresenter senderPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO:Check the sender layout to make the boxes better in size

        View view = inflater.inflate(R.layout.fragment_sender, container, false);
        this.container = view.findViewById(R.id.container);
        this.addButton = view.findViewById(R.id.buttonAdd);
        this.sendButton = view.findViewById(R.id.buttonSend);
        this.inputList = new ArrayList<>();
        this.senderPresenter = new SenderPresenter();
        //Set click Listeners
        addButton.setOnClickListener(v -> addInputList());
        sendButton.setOnClickListener(v -> saveFileNames());

        return view;
    }

    private void addInputList() {
        if (inputList.size() == 0) {
            //Create and add the fisrt EditText
            EditText editText = new EditText(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            editText.setLayoutParams(params);
            editText.setHint("Enter file name");
            container.addView(editText);
            inputList.add(editText);
        } else {
            //Create and add subsequent EditText fields
            EditText editText = new EditText(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            editText.setLayoutParams(params);
            editText.setHint("Enter file name");
            container.addView(editText);
            inputList.add(editText);
        }
    }

    private void saveFileNames() {
        //TODO change the list into another databaset
        List<String> fileNames = new ArrayList<>();
        for (EditText editText : inputList) {
            String fileName = editText.getText().toString().trim();
            // Log.e("DEBUGGER", fileName);
            fileNames.add(fileName);
        }
//        senderPresenter.sendFiles(fileNames);
        // TODO : empty the array with the Edit texts
        // TODO : create threads end send?
    }
}