package com.pktworld.taskthrough.locationutils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.pktworld.taskthrough.utils.ApplicationConstant;


/**
 * Created by ubuntu1 on 10/12/15.
 */
public class LocationServiceBootReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationServiceBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent gpsTrackerIntent = new Intent(context, LocationServiceAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.APPLICATION_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int intervalInMinutes = sharedPreferences.getInt(ApplicationConstant.INTERVAL_IN_MINUTES, ApplicationConstant.INTERVAL_TIME);

            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    intervalInMinutes  * 1000,
                    pendingIntent);
        }
    }
