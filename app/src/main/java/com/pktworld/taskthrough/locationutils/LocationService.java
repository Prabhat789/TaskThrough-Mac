package com.pktworld.taskthrough.locationutils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pktworld.taskthrough.db.DatabaseModel;
import com.pktworld.taskthrough.db.TaskThruDatabase;
import com.pktworld.taskthrough.utils.Globals;
import com.pktworld.taskthrough.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ubuntu1 on 10/12/15.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    protected static final String TAG = "LocationService";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    protected String mLastUpdateTime;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    private Globals glo;
    private TaskThruDatabase db;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        return START_NOT_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            Log.e("Latitude",""+mCurrentLocation.getLatitude());
            Log.e("Longitude", "" + mCurrentLocation.getLongitude());
            glo.setLatitude(Double.toString(mCurrentLocation.getLatitude()));
            glo.setLongitude(Double.toString(mCurrentLocation.getLongitude()));

            db.addLocation(new DatabaseModel(glo.getLatitude(),glo.getLongitude(), Utils.getCurrentTime()));
            Toast.makeText(LocationService.this,"Location Count is : "+db.getLocationCount(),Toast.LENGTH_SHORT).show();;
            stopLocationUpdates();
            stopSelf();


        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.v("LocationChange", "Location Change");
        Log.e("Latitude",""+mCurrentLocation.getLatitude());
        Log.e("Longitude", "" + mCurrentLocation.getLongitude());


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();
        } else {
            Log.e(TAG, "unable to connect to google play services.");
        }

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }
        }else{
            Log.e("GoogleApiClient", "GoogleApiClient is Not Connected");
        }

    }

    protected void stopLocationUpdates() {
        try{
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        try{
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        glo =  new Globals(this);
        db = new TaskThruDatabase(this);
        buildGoogleApiClient();

    }
}
