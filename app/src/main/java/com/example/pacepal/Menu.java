package com.example.pacepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

import com.example.pacepal.view.LeaderBoard.LeaderBoardFragment;
import com.example.pacepal.view.Statistics.StatisticsFragment;
import com.example.pacepal.view.results.ResultsFragment;
import com.example.pacepal.view.sender.SenderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Menu extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView((R.layout.menu));

        // Set the initial fragment to be displayed
        replaceFragment(new SenderFragment());

        // Set up the bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
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
}