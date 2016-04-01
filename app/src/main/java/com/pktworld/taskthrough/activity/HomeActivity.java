package com.pktworld.taskthrough.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pktworld.taskthrough.R;
import com.pktworld.taskthrough.adapter.TaskAdapter;
import com.pktworld.taskthrough.db.DatabaseModel;
import com.pktworld.taskthrough.db.TaskThruDatabase;
import com.pktworld.taskthrough.utils.ApplicationConstant;
import com.pktworld.taskthrough.utils.UserSessionManager;
import com.pktworld.taskthrough.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Prabhat on 12/03/16.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private LinearLayout llInternet, llNoInternet;
    private Button btnAppointment;
    private ListView listTask;
    private TaskThruDatabase db;
    private TaskAdapter mAdapter;
    private UserSessionManager glo;
    private WebView webView;
    private ProgressDialog mProgressDialog;
    private String userId = null;
    private String redirectUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        glo = new UserSessionManager(this);
        userId = glo.getUserDetails().get("userid");
        redirectUrl = glo.getUserDetails().get("redirect_url");
        /*if (glo.getUserId().toString().length() == 0){
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }*/
        glo.checkLogin();
        if (isLycenceExpire()){
            finish();
        }
        db = new TaskThruDatabase(this);
        llInternet = (LinearLayout)findViewById(R.id.llInternet);
        llNoInternet = (LinearLayout)findViewById(R.id.llNoInternet);
        btnAppointment = (Button)findViewById(R.id.btnAppointment);
        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);


        listTask = (ListView)findViewById(R.id.listTask);
        btnAppointment.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        if (Utils.isConnected(this)){
            llInternet.setVisibility(View.VISIBLE);
            llNoInternet.setVisibility(View.GONE);
            webView.setWebViewClient(new MyWebViewClient());
                webView.loadUrl(glo.getRemoteUrl()+redirectUrl);
                mProgressDialog = ProgressDialog.show(HomeActivity.this, "",
                        getResources().getString(R.string.processing), true);
        }else{
            llNoInternet.setVisibility(View.VISIBLE);
            llInternet.setVisibility(View.GONE);
            getAllTaskAndShowInList();
        }
        super.onResume();
    }

    void showClassDialog(final Context mContext, String title){

        final Dialog gameOver = new Dialog(mContext);
        gameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameOver.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameOver.setCancelable(false);
        gameOver.setContentView(R.layout.dialog_apointment);
        TextView txtTitle = (TextView)gameOver.findViewById(R.id.txtTitleDialog);
        txtTitle.setText(title);
        gameOver.setCancelable(true);

        final EditText editTitle = (EditText)gameOver.findViewById(R.id.editTitle);
        final EditText editReview = (EditText)gameOver.findViewById(R.id.editReview);
        Button btnSave = (Button)gameOver.findViewById(R.id.btnDialogSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTitle.getText().toString().trim().isEmpty()
                        || editTitle.getText().toString().trim().length() == 0){
                    Utils.showToastMessage(HomeActivity.this, "Please Enter Title");
                }else if (editReview.getText().toString().trim().isEmpty()
                        || editReview.getText().toString().trim().length() == 0){
                    Utils.showToastMessage(HomeActivity.this, "Please Enter Review");
                }
                else {
                    db.addTask(new DatabaseModel(userId,editTitle.getText().toString(),
                            editReview.getText().toString(),glo.getLatitude(),glo.getLongitude(),
                            "false",Utils.getCurrentTime()));
                    getAllTaskAndShowInList();
                    gameOver.dismiss();
                }

            }
        });

        gameOver.show();

    }

    @Override
    public void onClick(View v) {
        if (v == btnAppointment){
            showClassDialog(HomeActivity.this,"Appointment");
        }
    }

    void getAllTaskAndShowInList(){
        List<DatabaseModel> ScannList = db.getAllTask();
        mAdapter = new TaskAdapter(HomeActivity.this,ScannList);
        listTask.setAdapter(mAdapter);
    }

    private boolean isLycenceExpire(){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        int dayCount = Utils.get_count_of_days(currentDate, ApplicationConstant.EXPIRED_DATE);
        if (dayCount == 0){
            Utils.showToastMessage(HomeActivity.this, "Licence Expired !");
            return true;
        }else {
            return false;
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            mProgressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
