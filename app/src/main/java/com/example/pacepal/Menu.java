package com.example.pacepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.pacepal.view.LeaderBoard.SelectSegment.LeaderBoardFragment;
import com.example.pacepal.view.Statistics.StatisticsFragment;
import com.example.pacepal.view.results.ResultsFragment;
import com.example.pacepal.view.sender.SenderFragment;
import com.example.pacepal.view.weather.WeatherActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class Menu extends AppCompatActivity {

    FloatingActionButton weather;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView((R.layout.menu));

        // Set the initial fragment to be displayed
        replaceFragment(new SenderFragment());

        // Set up the bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        weather = (FloatingActionButton) findViewById(R.id.weatherButton);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherButtonClicked();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.SenderFragment:
                        replaceFragment(new SenderFragment());
                        break;
                    case R.id.ResultsFragment:
                        replaceFragment(new ResultsFragment());
                        break;
                    case R.id.StatisticsFragment:
                        replaceFragment(new StatisticsFragment());
                        break;
                    case R.id.LeaderBoardFragment:
                        replaceFragment(new LeaderBoardFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    public void weatherButtonClicked() {
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
    }
}
