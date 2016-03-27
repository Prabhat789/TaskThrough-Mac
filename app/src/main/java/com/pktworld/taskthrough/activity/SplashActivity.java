package com.pktworld.taskthrough.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.pktworld.taskthrough.R;

/**
 * Created by Prabhat on 12/03/16.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.postDelayed(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

            }
        }, 3000L);
    }
}
