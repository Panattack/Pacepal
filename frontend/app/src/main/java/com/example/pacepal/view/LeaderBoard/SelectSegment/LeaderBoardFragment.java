package com.example.pacepal.view.LeaderBoard.SelectSegment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pacepal.R;
import com.example.pacepal.view.LeaderBoard.ShowLeaderBoard.ShowBoardActivity;

import java.util.ArrayList;

public class LeaderBoardFragment extends Fragment implements LeaderBoardView {
    LeaderBoardPresenter presenter;
    Button select;
    String host;
    int serverPort;
    TextView id;
    int user_id;
    Spinner segmentSpinner;

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
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        select = (Button) view.findViewById(R.id.selectButton);
        segmentSpinner = (Spinner) view.findViewById(R.id.spinner);
        id = (TextView) view.findViewById(R.id.textUser);

        Bundle arguments = getArguments();
        if (arguments != null) {
            host = arguments.getString("host");
            serverPort = arguments.getInt("serverPort");
            user_id = arguments.getInt("user_id");
        }
        presenter = new LeaderBoardPresenter(this, host, serverPort);
        return view;
    }

    /**
     * 1. Choosing the id of the segment
     * 2. Calling the controller when the set button is pushed
     */
    @Override
    public void onStart() {
        super.onStart();
        id.setText("YOUR USER ID IS: " + user_id);
        try {
            presenter.getNumber();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendNumber();
            }
        });
    }

    /**
     * Create the segment list, according to the id that the Master server sent
     *
     * @param num a list of id as integers
     */
    @Override
    public void createSegmentList(ArrayList<Integer> num) {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, num);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        segmentSpinner.setAdapter(adapter);
    }

    /**
     * Get the selected segment from the spinner
     *
     * @return selected id as an integer
     */
    @Override
    public int getSelectedSegment() {
        return Integer.parseInt(segmentSpinner.getSelectedItem().toString());
    }

    /**
     * Moving from the LeaderBoard fragment to ShowBoard Activity
     *
     * @param id the id of the segment
     */
    @Override
    public void sentOption(int id) {
        Intent intent = new Intent(getContext(), ShowBoardActivity.class);
        Bundle b = new Bundle();
        b.putInt("number", id);
        b.putString("host", host);//Your id
        b.putInt("serverPort", serverPort);
        b.putInt("user_id", user_id);
        intent.putExtras(b);
        startActivity(intent);
    }

}