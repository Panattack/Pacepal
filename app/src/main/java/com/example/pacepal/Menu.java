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

import java.io.IOException;
import java.util.Properties;

public class Menu extends AppCompatActivity {

    FloatingActionButton weather;
    String host;
    int serverPort;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView((R.layout.menu));

        Properties prop = new Properties();

        try {
            prop.load(this.getAssets().open("client.properties"));
        } catch (IOException ex) {
            System.out.println("File not found !!!");
        }

        host = prop.getProperty("host");
        serverPort = Integer.parseInt(prop.getProperty("serverPort"));

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
                        SenderFragment senderFragment = new SenderFragment();
                        replaceFragment(senderFragment);
                        break;
                    case R.id.ResultsFragment:
                        ResultsFragment resultsFragment = new ResultsFragment();
                        replaceFragment(resultsFragment);
                        break;
                    case R.id.StatisticsFragment:
                        StatisticsFragment statisticsFragment = new StatisticsFragment();
                        replaceFragment(statisticsFragment);
                        break;
                    case R.id.LeaderBoardFragment:
                        LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();
                        replaceFragment(leaderBoardFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("host", host);
        bundle.putInt("serverPort", serverPort);
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    public void weatherButtonClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("host", host);
        bundle.putInt("serverPort", serverPort);
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
