package com.pktworld.taskthrough.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pktworld.taskthrough.R;
import com.pktworld.taskthrough.utils.Utils;

/**
 * Created by Prabhat on 12/03/16.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout llInternet, llNoInternet;
    private Button btnAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        llInternet = (LinearLayout)findViewById(R.id.llInternet);
        llNoInternet = (LinearLayout)findViewById(R.id.llNoInternet);
        btnAppointment = (Button)findViewById(R.id.btnAppointment);
        btnAppointment.setOnClickListener(this);


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

    void showClassDialog(Context mContext, String title){

        final Dialog gameOver = new Dialog(mContext);
        gameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameOver.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameOver.setCancelable(false);
        gameOver.setContentView(R.layout.dialog_apointment);
        TextView txtTitle = (TextView)gameOver.findViewById(R.id.txtTitleDialog);
        txtTitle.setText(title);


        gameOver.show();

    }

    @Override
    public void onClick(View v) {
        if (v == btnAppointment){
            showClassDialog(HomeActivity.this,"Appointment");
        }
    }
}
