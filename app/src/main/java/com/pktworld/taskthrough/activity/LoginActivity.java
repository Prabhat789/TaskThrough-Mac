package com.pktworld.taskthrough.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pktworld.taskthrough.R;
import com.pktworld.taskthrough.model.LoginResponse;
import com.pktworld.taskthrough.utils.ApplicationConstant;
import com.pktworld.taskthrough.utils.GsonRequestResponseHelper;
import com.pktworld.taskthrough.utils.UrlString;
import com.pktworld.taskthrough.utils.UserSessionManager;
import com.pktworld.taskthrough.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements OnMapReadyCallback ,View.OnClickListener{

    private static final String TAG = LoginActivity.class.getSimpleName();
    private GoogleMap mMap;
    private Button btnLogin,btnSocialLogin;
    private EditText editUsername, editPassword;
    private ProgressDialog mProgressDialog;
    private int doneId;
    private UserSessionManager glo;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utils.checkLocationStatus(this);

        glo = new UserSessionManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLogin  = (Button)findViewById(R.id.btnLogin);
        btnSocialLogin  = (Button)findViewById(R.id.btnSocial);
        editUsername = (EditText)findViewById(R.id.editUsername);
        editPassword = (EditText)findViewById(R.id.editPassword);

        btnLogin.setOnClickListener(this);
        btnSocialLogin.setOnClickListener(this);
       /* try{
            editPassword.setText(glo.getUserPassword());
            editUsername.setText(glo.getUserName());
        }catch (Exception e){
            e.printStackTrace();
        }*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setBuildingsEnabled(true);
        LatLng sydney = new LatLng(Double.parseDouble(glo.getLatitude()),Double.parseDouble(glo.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.
                fromBitmap(resizeMapIcons("ic_marker_50", 70, 70))).
                title(Utils.getAddress(Double.parseDouble(glo.getLatitude()), Double.parseDouble(glo.getLongitude()), LoginActivity.this)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sydney.latitude, sydney.longitude), 14));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, doneId, 0, getString(R.string.action_settings))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == doneId) {
            Intent i = new Intent(LoginActivity.this,SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            if (validate()){
                if (Utils.isConnected(LoginActivity.this)){
                    login(LoginActivity.this,"userjson.asmx/SetUserLogin",
                            editUsername.getText().toString(),editPassword.getText().toString());
                }
            }

        }else if (v == btnSocialLogin){
            Utils.showToastMessage(LoginActivity.this,"Work in progress");
        }
    }

    private boolean validate(){
        if (editUsername.getText().toString().trim().length() == 0 ){
            editUsername.setError(getResources().getString(R.string.invalid_username));
            return false;
        }else if (editPassword.getText().toString().trim().length() == 0){
            editPassword.setError(getResources().getString(R.string.invalid_password));
            return false;
        }else{
            return true;
        }

    }


    private void login(Context mContext, String url, String userId, String Password) {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = UrlString.BASE_URL+url;
        Map<String,String> params = new HashMap<String, String>();
        params.put("lsEmail", userId);
        params.put("lsPassword", Password);
        params.put("lsLatitude", glo.getLatitude());
        params.put("lsLongitude", glo.getLongitude());
        params.put("lsLocation", Utils.getAddress(Double.parseDouble(glo.getLatitude()),
                Double.parseDouble(glo.getLongitude()),mContext));
        params.put("lsDeviceId", ApplicationConstant.DEVICE_ID);



        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<LoginResponse> myReq = new GsonRequestResponseHelper<LoginResponse>(
                com.android.volley.Request.Method.POST,
                REQUEST_URL,
                LoginResponse.class,
                params,
                createMyReqSuccessListener(),
                createMyReqErrorListener());


        mRequestQueue.add(myReq);
    }

    private Response.Listener<LoginResponse> createMyReqSuccessListener() {
        return new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                try {
                    if (response.getResponse().equals("Success")){
                        Log.e(TAG,response.getStaffId());
                        Log.e(TAG, response.getRedirectUrl());
                        glo.createUserLoginSession(response.getStaffId(),response.getRedirectUrl());

                        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }else {
                        Utils.showToastMessage(LoginActivity.this,
                                getString(R.string.unable_to_process_tequest));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showToastMessage(LoginActivity.this,
                            getString(R.string.unable_to_process_tequest));
                    Log.e(TAG, "TryCatch");
                }
            };
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                Utils.showToastMessage(LoginActivity.this,
                        getString(R.string.unable_to_process_tequest));
                Log.e(TAG, "Error");

            }
        };
    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


}
