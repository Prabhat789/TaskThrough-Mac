package com.pktworld.taskthrough.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pktworld.taskthrough.R;
import com.pktworld.taskthrough.utils.UserSessionManager;
import com.pktworld.taskthrough.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Prabhat on 12/03/16.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = SettingsActivity.class.getSimpleName();
    private EditText editUrl;
    private Button btnSaveUrl;
    private UserSessionManager glo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setting");

        glo = new UserSessionManager(SettingsActivity.this);
        editUrl = (EditText)findViewById(R.id.editUrl);
        btnSaveUrl = (Button)findViewById(R.id.btnSaveUrl);
        btnSaveUrl.setOnClickListener(this);

        editUrl.setText(glo.getRemoteUrl());
    }

    @Override
    public void onClick(View v) {
        if (v == btnSaveUrl){
            if (isUrlValid(editUrl.getText().toString())){
                glo.saveRemoteUrl(editUrl.getText().toString());
                finish();
            }else {
                Utils.showToastMessage(SettingsActivity.this,"Please enter valid Url");
            }
        }
    }

    boolean isUrlValid(String url){
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url);
        if(m.matches())
            return true;
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
