package com.example.pacepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Menu extends AppCompatActivity {

    Handler handler;
    private Fragment homeFragment;
    private Fragment senderFragment;
    private Fragment resultsFragment;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView((R.layout.menu));

        //Initialize the fragments

        homeFragment = new MenuFragment();
        resultsFragment = new ResultsFragment();
        senderFragment = new SenderFragment();

        // Set the initial fragment to be displayed
        replaceFragment(new MenuFragment());

        // Set up the bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           @SuppressLint("NonConstantResourceId")
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.MenuFragment:
                       replaceFragment(new MenuFragment());
                       Log.e("DEBUGGER", "Home");
                       break;
                   case R.id.SenderFragment:
                       replaceFragment(new SenderFragment());
                       Log.e("DEBUGGER", "Submission");
                       break;
                   case R.id.ResultsFragment:
                       replaceFragment(new ResultsFragment());
                       Log.e("DEBUGGER", "Progress");
                       break;
               }
               return true;
           }
       });

        handler= new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {

                return true ;
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