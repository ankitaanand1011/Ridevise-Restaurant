package com.restaurant.ridewise.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.restaurant.ridewise.R;

public class SplashActivity extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms


               /* if(globalClass.getLogin_status()){

                    intent = new Intent(SplashScreen.this, HomeScreen.class);
                }else {
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                }*/

                intent = new Intent(SplashActivity.this, LoginActivity.class);

                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}