package com.example.pacepal.view.LeaderBoard.ShowLeaderBoard;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

        if (b != null) {
            segment_id = b.getInt("number");
            host = b.getString("host");
            serverPort = b.getInt("serverPort");

            //Log.e("Debugger", String.valueOf(segment_id));
        }

        presenter = new ShowBoardPresenter(this, host, serverPort);

        scroll = (LinearLayout) findViewById(R.id.tableLayout);
        try {
            presenter.show();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        presenter.createBoard();
    }

    @Override
    public void createLeaderBoard(double timesec, int name, int num) {

        if (num == 1) {
            TextView position = new TextView(this);
            position.setText("POSITION");
            position.setPadding(30, 30, 40, 30);
            position.setTextSize(25);

            TextView user = new TextView(this);
            user.setText("USER-ID");
            user.setPadding(30, 30, 20, 30);
            user.setTextSize(25);
            //user.setma

            TextView time = new TextView(this);
            time.setText("TIME");
            time.setPadding(50, 30, 20, 30);
            time.setTextSize(25);

            LinearLayout lead = new LinearLayout(this);

            lead.addView(position);
            lead.addView(user);
            lead.addView(time);
            lead.setOrientation(lead.HORIZONTAL);
            lead.setBackgroundResource(R.drawable.leaderboard_backround);
            scroll.addView(lead);

            TextView space = new TextView(this);
            space.setText("");
            space.setTextSize(40);
            space.setPadding(20, 20, 20, 20);

            scroll.addView(space);

        }


        TextView position = new TextView(this);
        position.setText(String.valueOf(num));
        position.setPadding(100, 20, 100, 20);
        position.setTextSize(30);
        position.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        TextView user = new TextView(this);
        user.setText(String.valueOf(name));
        user.setPadding(100, 30, 100, 30);
        user.setTextSize(30);

        TextView time = new TextView(this);
        time.setText(String.valueOf(timesec));
        time.setPadding(30, 30, 0, 30);
        time.setTextSize(30);


        LinearLayout lead = new LinearLayout(this);

        lead.setOrientation(lead.HORIZONTAL);
        lead.setBackgroundResource(R.drawable.leaderboard_backround);


        if (num == 1) {
            ImageView firstPlace = new ImageView(this);
            firstPlace.setImageResource(R.drawable.firstplace);
            firstPlace.setPadding(20, 20, 10, 10);
            lead.addView(firstPlace);
        }
        if (num == 2) {
            ImageView firstPlace = new ImageView(this);
            firstPlace.setImageResource(R.drawable.secondname);
            firstPlace.setPadding(20, 20, 10, 10);
            lead.addView(firstPlace);
        }
        if (num == 3) {
            ImageView firstPlace = new ImageView(this);
            firstPlace.setImageResource(R.drawable.thirdplace);
            firstPlace.setPadding(20, 20, 10, 10);
            lead.addView(firstPlace);
        }

        lead.addView(position);
        lead.addView(user);
        lead.addView(time);


        scroll.addView(lead);

        TextView space = new TextView(this);
        space.setText("");
        space.setTextSize(40);
        space.setPadding(20, 20, 20, 20);

        scroll.addView(space);

    }

    public int getSegmentId() {

        return segment_id;
    }


}