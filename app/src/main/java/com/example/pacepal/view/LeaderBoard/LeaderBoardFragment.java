package com.example.pacepal.view.LeaderBoard;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import com.example.pacepal.R;

import java.util.ArrayList;


public class LeaderBoardFragment extends Fragment implements LeaderBoardView {

    LinearLayout scroll;

    ArrayList<Integer> time = new ArrayList<>();
    LeaderBoardPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        presenter= new LeaderBoardPresenter(this);
        scroll = view.findViewById(R.id.tableLayout);
        presenter.createBoard();
        return view;

    }

    @Override
    public void createLeaderBoard(double timesec,String name, int num) {



        TextView position = new TextView(requireContext());
        position.setText(String.valueOf(num));
        position.setPadding(30, 30, 30, 30);
        position.setTextSize(30);
        position.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        TextView user = new TextView(requireContext());
        user.setText(name);
        user.setPadding(30, 30, 30, 30);
        user.setTextSize(30);

        TextView time = new TextView(requireContext());
        time.setText(String.valueOf(timesec));
        time.setPadding(30, 30, 30, 30);
        time.setTextSize(30);


        LinearLayout lead = new LinearLayout(requireContext());

        lead.setOrientation(lead.HORIZONTAL);
        lead.setBackgroundResource(R.drawable.leaderboard_backround);
        lead.addView(position);
        lead.addView(user);
        lead.addView(time);


        scroll.addView(lead);

        TextView space = new TextView(requireContext());
        space.setText("");
        space.setTextSize(40);
        space.setPadding(20,20,20,20);

        scroll.addView(space);

    }
}