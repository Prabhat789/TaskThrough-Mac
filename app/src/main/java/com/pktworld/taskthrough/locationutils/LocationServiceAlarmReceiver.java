package com.pktworld.taskthrough.locationutils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by ubuntu1 on 10/12/15.
 */
public class LocationServiceAlarmReceiver  extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        context.startService(new Intent(context, LocationService.class));
    }
}
