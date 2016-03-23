package com.pktworld.taskthrough.locationutils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

import com.pktworld.taskthrough.utils.ApplicationConstant;

/**
 * Created by Prabhat on 12/03/16.
 */
public class GpsLocationReceiver extends BroadcastReceiver {
    private static String TAG = GpsLocationReceiver.class.getSimpleName();
    private AlarmManager alarmManager;
    private Intent gpsTrackerIntent;
    private PendingIntent pendingIntent;
    private int intervalInMinutes;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                startAlarmManager(context);
            }else{
                cancelAlarmManager(context);

            }
        }
    }

    private void startAlarmManager(Context mContext) {

        Log.v(TAG, "LocationService Start");
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                    ApplicationConstant.APPLICATION_PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(ApplicationConstant.INTERVAL_IN_MINUTES, ApplicationConstant.INTERVAL_TIME);
            alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            gpsTrackerIntent = new Intent(mContext, LocationServiceAlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(mContext, 0, gpsTrackerIntent, 0);
            intervalInMinutes = sharedPreferences.getInt(ApplicationConstant.INTERVAL_IN_MINUTES, ApplicationConstant.INTERVAL_TIME);
            alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), intervalInMinutes  * 1000 ,
                    pendingIntent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void cancelAlarmManager(Context mContext) {
        Log.d(TAG, "cancelAlarmManager");
        Intent gpsTrackerIntent = new Intent(mContext,
                LocationServiceAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                gpsTrackerIntent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
