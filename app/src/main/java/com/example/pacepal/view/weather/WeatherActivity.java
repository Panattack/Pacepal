package com.example.pacepal.view.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pacepal.R;

public class WeatherActivity extends AppCompatActivity implements WeatherView {
    EditText searchText;
    Button searchButton;
    ImageView weatherImage;
    WeatherPresenter presenter;
    int serverPort;
    String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        LinearLayout linearLayout = findViewById(R.id.weatherLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            host = bundle.getString("host");
            serverPort = bundle.getInt("serverPort");
        }
        searchText = (EditText) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);
        weatherImage = (ImageView) findViewById(R.id.myImageView);
        presenter = new WeatherPresenter(this, serverPort, host);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.sendCity();
            }
        });

    }

    @Override
    public void setImage(int id) {
        // TODO Based on the id, will be shown different icons
        weatherImage.setImageResource(id);
    }

    @Override
    public String getText() {
        return searchText.getText().toString().trim();
    }

    @Override
    public void setStatus(String temp, String pressure, String humidity, String main, String description) {
        // Assuming you have a reference to the TableLayout
        TableLayout tableLayout = findViewById(R.id.WeatherTable);

        TableRow temperaturerow = (TableRow) tableLayout.getChildAt(0);
        TextView temperaturecolumn = (TextView) temperaturerow.getChildAt(1);
        temperaturecolumn.setText(String.format("%.2f", Float.parseFloat(temp)));

        TableRow pressurerow = (TableRow) tableLayout.getChildAt(1);
        TextView pressurecolumn = (TextView) pressurerow.getChildAt(1);
        pressurecolumn.setText(pressure);

        TableRow humidityrow = (TableRow) tableLayout.getChildAt(2);
        TextView humiditycolumn = (TextView) humidityrow.getChildAt(1);
        humiditycolumn.setText(humidity);

        TableRow mainrow = (TableRow) tableLayout.getChildAt(3);
        TextView maincolumn = (TextView) mainrow.getChildAt(1);
        maincolumn.setText(main);

        TableRow descrow = (TableRow) tableLayout.getChildAt(4);
        TextView desccolumn = (TextView) descrow.getChildAt(1);
        desccolumn.setText(description);
    }
}