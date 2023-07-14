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

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
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

    /**
     * Sets the image based on the weather id
     *
     * @param id indicates the weather as an integer
     */
    @Override
    public void setImage(int id) {
        // TODO Based on the id, will be shown different icons
        weatherImage.setImageResource(id);
    }

    /**
     * Gets the city
     *
     * @return the written city as a string
     */
    @Override
    public String getText() {
        return searchText.getText().toString().trim();
    }

    /**
     * Creates the box with the weather info
     *
     * @param temp        the temperature as a string
     * @param pressure    the pressure as a string
     * @param humidity    the humidity as a string
     * @param main        the main weather as a string
     * @param description the description of the overall weather as a string
     */
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