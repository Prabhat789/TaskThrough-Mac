package com.pktworld.taskthrough.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.pktworld.taskthrough.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Prabhat on 12/03/16.
 */
public class Utils {

    Context mContext;
    static final String TAG = Utils.class.getSimpleName();

    public Utils(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        } else{
            Toast.makeText(context,context.getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            return false;
        }

    }



    public static String getAddress(Double lat, Double lng, Context context) {

        String locationAddress;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
        } catch (IOException e1) {
            e1.printStackTrace();
            return ("Address not found..!");
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return "Address not found..!";
        }
        if (addresses != null && addresses.size() > 0) {
            Address fetchedAddress = addresses.get(0);
            Log.v(TAG,fetchedAddress.toString());
			/*StringBuilder strAddress = new StringBuilder();
			for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
				strAddress.append(fetchedAddress.geta);
			}*/
            //locationAddress = addresses.get(0).getSubLocality()+" ,"+addresses.get(0).getLocality();
            locationAddress = addresses.get(0).getAddressLine(0);


            return locationAddress;
        } else {
            return "Address not found..!";
        }

    }

    public static boolean checkLocationStatus(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(myIntent);
            return false;

        }

    }


   /* public static void showInfoAlert(Context mContext, String msg){

        try{
            Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_application_alert);
            dialog.setCancelable(true);

            TextView msgText = (TextView)dialog.findViewById(R.id.txtAlertMessage);
            msgText.setText(msg);
            dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }*/
}
