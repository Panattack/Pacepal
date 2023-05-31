package com.example.pacepal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends Activity {

    ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.login));
        pic = (ImageView) findViewById(R.id.logoImage);

    }

    @Override
    protected void onStart(){
        super.onStart();
        pic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e("DEBUGGPXAPP","before clicking");
                Intent myIntent = new Intent(LogIn.this,Menu.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }


}


