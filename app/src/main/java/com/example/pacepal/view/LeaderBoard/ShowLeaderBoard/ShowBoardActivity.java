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
    int segment_id = -1; // or other values
    String host;
    int serverPort;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_segment_fragment);


        Bundle b = getIntent().getExtras();

        if(b != null) {
            segment_id = b.getInt("number");
            host = b.getString("host");
            serverPort = b.getInt("serverPort");

            //Log.e("Debugger", String.valueOf(segment_id));
        }

        presenter = new ShowBoardPresenter(this,host,serverPort);

      scroll =(LinearLayout) findViewById(R.id.tableLayout);
        try {
            presenter.show();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        presenter.createBoard();
    }

    @Override
    public void createLeaderBoard(double timesec,int name, int num) {


        TextView position = new TextView(this);
        position.setText(String.valueOf(num));
        position.setPadding(30, 30, 30, 30);
        position.setTextSize(30);
        position.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        TextView user = new TextView(this);
        user.setText(String.valueOf(name));
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

    public int getSegmentId(){

        return segment_id;
    }




}