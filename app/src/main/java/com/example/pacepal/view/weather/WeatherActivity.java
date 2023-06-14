package com.example.pacepal.view.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pacepal.R;

public class WeatherActivity extends AppCompatActivity implements WeatherView{
    EditText searchText;
    Button searchButton;
    WeatherPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchText = (EditText) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);

        setContentView(R.layout.activity_weather);
        presenter = new WeatherPresenter(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                String title = presenter.getTitle();
//                presenter.decide(title);
            }
        });

    }

    @Override
    public void setIcon(int id) {
        // TODO Based on the id, will be shown different icons
        // Group 2xx: Thunderstorm
        // Group 3xx: Drizzle
        // Group 5xx: Rain
    }

    @Override
    public String getText() {
        return searchText.getText().toString();
    }
}