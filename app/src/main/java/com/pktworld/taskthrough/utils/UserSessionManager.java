package com.pktworld.taskthrough.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.pktworld.taskthrough.activity.LoginActivity;

import java.util.HashMap;

public class UserSessionManager {
	
	private SharedPreferences pref;
	private Editor editor;
	private Context _context;
	private int PRIVATE_MODE = 0;
	private static final String PREFER_NAME = "TaskThru";
	private static final String IS_USER_LOGIN = "IsUserLoggedIn";
	public static final String KEY_USER_ID = "userid";
	public static final String REDIRECT_URL = "redirect_url";
	public static final String REMOTE_URL = "remote_url";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	// Constructor
	public UserSessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	//Create login session
	public void createUserLoginSession( String userid , String redirectUrl ){
		// Storing login value as TRUE
		editor.putBoolean(IS_USER_LOGIN, true);
		
		// Storing name in pref

		editor.putString(KEY_USER_ID,userid);
		editor.putString(REDIRECT_URL, redirectUrl);
		// commit changes
		editor.commit();
	}

	public void saveRemoteUrl( String remoteUrl ){
		editor.putString(REMOTE_URL, remoteUrl);
		// commit changes
		editor.commit();
	}
	public String getRemoteUrl(){
		return pref.getString(REMOTE_URL, "http://mapweb.vacationdealsworld.com/");
	}
	public void saveLatitude( String remoteUrl ){
		editor.putString(LATITUDE, remoteUrl);
		// commit changes
		editor.commit();
	}
	public String getLatitude(){
		return pref.getString(LATITUDE, "0.00");
	}
	public void saveLongitude( String remoteUrl ){
		editor.putString(LONGITUDE, remoteUrl);
		// commit changes
		editor.commit();
	}
	public String getLongitude(){
		return pref.getString(LONGITUDE, "0.00");
	}

	/**
	 * Check login method will check user login status
	 * If false it will redirect user to login page
	 * Else do anything
	 * */
	public boolean checkLogin(){
		// Check login status
		if(!this.isUserLoggedIn()){
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
			return true;
		}
		return false;
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		
		//Use hashmap to store user credentials
		HashMap<String, String> user = new HashMap<String, String>();
		
		// user name
		user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
		user.put(REDIRECT_URL, pref.getString(REDIRECT_URL, null));
		// return user
		return user;
	}
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		
		// Clearing all user data from Shared Preferences
		editor.clear();
		editor.commit();
		// After logout redirect user to Login Activity
		Intent i = new Intent(_context, LoginActivity.class);
		//i.putExtra("FLAG",ApplicationConstant.ACCOUNT);
		/*// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Staring Login Activity*/
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		_context.startActivity(i);
	}
	
	// Check for login
	public boolean isUserLoggedIn(){
		return pref.getBoolean(IS_USER_LOGIN, false);
	}

	
}
