package com.pktworld.taskthrough.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.pktworld.taskthrough.R;
import com.pktworld.taskthrough.utils.Utils;

/**
 * Created by Prabhat on 12/03/16.
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout llInternet, llNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        llInternet = (LinearLayout)findViewById(R.id.llInternet);
        llNoInternet = (LinearLayout)findViewById(R.id.llNoInternet);


    }

    @Override
    protected void onResume() {
        if (Utils.isConnected(this)){
            llInternet.setVisibility(View.VISIBLE);
            llNoInternet.setVisibility(View.GONE);
        }else{
            llNoInternet.setVisibility(View.VISIBLE);
            llInternet.setVisibility(View.GONE);
        }
        super.onResume();
    }
}
