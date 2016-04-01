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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pktworld.taskthrough.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public static String getCurrentTime(){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        return currentDate;
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

    public static int get_count_of_days(String Created_date_String,String Expire_date_String) {
        Log.e(TAG,"CurrentDate : "+Created_date_String+" ,Enddate : "+Expire_date_String);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date Created_convertedDate=null,Expire_CovertedDate=null,todayWithZeroTime=null;
        try
        {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime =dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }


        int c_year=0,c_month=0,c_day=0;

        if(Created_convertedDate.after(todayWithZeroTime))
        {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        }
        else
        {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }

        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);


        return ((int) dayCount);
    }

    public static void showToastMessage(Context mContext, String msg) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View layout = li.inflate(R.layout.custom_toast, null);
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtToast);
        txtMsg.setText(msg);
        Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        // toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }
}
