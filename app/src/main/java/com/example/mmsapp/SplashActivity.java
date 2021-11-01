package com.example.mmsapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmsapp.service.SharedPref;


public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 1000;
    String versionName = BuildConfig.VERSION_NAME;
    TextView version;
    Handler mHandler = new Handler();
    int intprogress = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        version = findViewById(R.id.version);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        version.setText("Version: "+ versionName);
        mHandler.post(runnable);
        checkversionapp();

    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            intprogress+=1;
            mHandler.postDelayed(this, SPLASH_DELAY/100);
            if (intprogress>99){
                intprogress=99;
            }
        }
    };


    private void checkversionapp() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DELAY);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}