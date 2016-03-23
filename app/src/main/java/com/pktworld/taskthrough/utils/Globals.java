package com.pktworld.taskthrough.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Globals {

	public final static String TAG = "TASKTHROUGH";
	// wifi and data connection constants
	public static int wifiStatus;
	public static String wifiStatusString;
	public static boolean isWifiAvailable;
	public static boolean isMobileDataAvailable;
	public static int airplaneModeStatus;
	Context context;

	public static int width;
	public static int height;
	public static int AWS_ONNECTION_TIMEOUT = 30000;
	private SharedPreferences sharedPref;
	private Editor editor;
	

	private static final String SHARED = "TASKTHROUGH";
	private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String REMOTE_URL = "REMOTE_URL";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";




    public Globals(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		this.context = context;
	}

	public void setUserId(String status) {
		editor.putString(USER_ID, status);
		editor.commit();
	}
    public String getUserId() {
        return sharedPref.getString(USER_ID, "");
    }

    public void setUserName(String status) {
        editor.putString(USER_NAME, status);
        editor.commit();
    }
    public String getUserName() {
        return sharedPref.getString(USER_NAME, "");
    }
    public void setUserEmail(String status) {
        editor.putString(USER_EMAIL, status);
        editor.commit();
    }
    public String getUserEmail() {
        return sharedPref.getString(USER_EMAIL, "");
    }
    public void setUserPassword(String status) {
        editor.putString(USER_PASSWORD, status);
        editor.commit();
    }
    public String getUserPassword() {
        return sharedPref.getString(USER_PASSWORD, "");
    }

    public void setRemoteUrl(String status) {
        editor.putString(REMOTE_URL, status);
        editor.commit();
    }
    public String getRemoteUrl() {
        return sharedPref.getString(REMOTE_URL, "www.taskthrough.com");
    }

    public void setLatitude(String status) {
        editor.putString(LATITUDE, status);
        editor.commit();
    }
    public String getLatitude() {
        return sharedPref.getString(LATITUDE, "0.00");
    }
    public void setLongitude(String status) {
        editor.putString(LONGITUDE, status);
        editor.commit();
    }
    public String getLongitude() {
        return sharedPref.getString(LONGITUDE, "0.00");
    }


}

