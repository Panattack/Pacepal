package com.example.pacepal.view.LeaderBoard.ShowLeaderBoard;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pacepal.R;

public class ShowBoardActivity extends Activity implements ShowBoardView {
    LinearLayout scroll;
    ShowBoardPresenter presenter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_segment_fragment);

        presenter = new ShowBoardPresenter(this);
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("number");
        Log.e("DEBUGGER", String.valueOf(value));
        scroll =(LinearLayout) findViewById(R.id.tableLayout);
        //TODO
        //presenter.createBoard();
    }

    @Override
    public void createLeaderBoard(double timesec,String name, int num) {



        TextView position = new TextView(this);
        position.setText(String.valueOf(num));
        position.setPadding(30, 30, 30, 30);
        position.setTextSize(30);
        position.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        TextView user = new TextView(this);
        user.setText(name);
        user.setPadding(30, 30, 30, 30);
        user.setTextSize(30);

        TextView time = new TextView(this);
        time.setText(String.valueOf(timesec));
        time.setPadding(30, 30, 30, 30);
        time.setTextSize(30);


        LinearLayout lead = new LinearLayout(this);

        lead.setOrientation(lead.HORIZONTAL);
        lead.setBackgroundResource(R.drawable.leaderboard_backround);
        lead.addView(position);
        lead.addView(user);
        lead.addView(time);


        scroll.addView(lead);

        TextView space = new TextView(this);
        space.setText("");
        space.setTextSize(40);
        space.setPadding(20,20,20,20);

        scroll.addView(space);

    }





}