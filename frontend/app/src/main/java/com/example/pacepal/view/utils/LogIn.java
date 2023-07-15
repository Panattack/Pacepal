package com.example.pacepal.view.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import android.os.Handler;

import com.example.pacepal.R;

public class LogIn extends Activity {
    ImageView pic;
    private RectangleProgressBar progressBar;
    private static final long PROGRESS_BAR_DURATION = 5000; // Duration in milliseconds

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.login));
        pic = (ImageView) findViewById(R.id.logoImage);
        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1
        );

        progressBar = findViewById(R.id.progressBar);
        startProgressBarAnimation();
    }

    /**
     * Creates and starts the animation of the progress bar
     */
    private void startProgressBarAnimation() {
        progressBar.startProgressAnimation();

        // Stop the progress bar animation after 5 seconds
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(100);  // Set progress to maximum

                //go to next page
                Intent myIntent = new Intent(LogIn.this, Menu.class);
                startActivityForResult(myIntent, 0);
                //  finish();  // Optional: Finish the current activity if needed
            }
        }, PROGRESS_BAR_DURATION); // 5 seconds delay (adjust as needed)
    }
}


