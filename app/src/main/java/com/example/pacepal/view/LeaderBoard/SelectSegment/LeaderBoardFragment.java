package com.example.pacepal.view.LeaderBoard.SelectSegment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.pacepal.R;
import com.example.pacepal.view.LeaderBoard.ShowLeaderBoard.ShowBoardActivity;


public class LeaderBoardFragment extends Fragment implements LeaderBoardView {
    LeaderBoardPresenter presenter;
    Button select;
    EditText seg_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        presenter= new LeaderBoardPresenter(this);
        select = (Button) view.findViewById(R.id.selectButton);
        seg_number = (EditText) view.findViewById(R.id.segmentNumber);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

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
   @Override
   public void segmentHint(int number){
       seg_number.setHint("You have " + number+ " segments");
    }
   @Override
   public void sentOption(){
        int number = Integer.parseInt(String.valueOf(seg_number.getText()));
        boolean ok = presenter.checker(number);
        if (ok){
            Intent intent = new Intent(getContext(), ShowBoardActivity.class);
            Bundle b = new Bundle();
            b.putInt("number", number); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
        }
        else {
            new AlertDialog.Builder(requireContext())
                    .setCancelable(true)
                    .setTitle("Wrong Input")
                    .setMessage("Number is out of bounds . You can select segments until the number "+ presenter.getNum()+" except for 0 ")
                    .setPositiveButton(R.string.ok, null).create().show();

        }


   }


}