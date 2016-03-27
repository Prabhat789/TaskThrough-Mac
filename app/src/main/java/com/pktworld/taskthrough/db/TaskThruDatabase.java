package com.pktworld.taskthrough.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabhat on 27/03/16.
 */
public class TaskThruDatabase extends SQLiteOpenHelper {

    private static final String TAG = TaskThruDatabase.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskThru";
    private static final String TABLE_LOCATION = "TableLocation";
    private static final String TABLE_TASK = "tableTask";
    private static final String ID = "id";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private static final String UPLOAD_FLAG = "uploadFlag";
    private static final String DATE_TIME = "dateTime";
    private static final String STAFF_ID = "staffId";
    private static final String REVIEW = "review";
    private static final String TITLE = "title";



    public TaskThruDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
                + ID + " INTEGER PRIMARY KEY," + LATITUDE + " TEXT,"
                + LONGITUDE + " TEXT," + DATE_TIME + " TEXT" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
                + ID + " INTEGER PRIMARY KEY," + STAFF_ID + " TEXT,"
                + TITLE + " TEXT," +REVIEW + " TEXT,"+LATITUDE +
                " TEXT,"+LONGITUDE + " TEXT,"+UPLOAD_FLAG +
                " TEXT," + DATE_TIME + " TEXT" + ")";
        db.execSQL(CREATE_TASK_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        // Create tables again
        onCreate(db);
    }

    // ADD TASK
    public void addTask(DatabaseModel conModel) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STAFF_ID, conModel.getStaffId());
        values.put(TITLE, conModel.getTitle());
        values.put(REVIEW, conModel.getReview());
        values.put(LONGITUDE, conModel.getLongitude());
        values.put(UPLOAD_FLAG, conModel.getUpladFlag());
        values.put(DATE_TIME, conModel.getDateTime());
        db.insert(TABLE_TASK, null, values);
        db.close();

    }
    // ADD LOCATION
    public void addLocation(DatabaseModel conModel) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LATITUDE, conModel.getLatitude());
        values.put(LONGITUDE, conModel.getLongitude());
        values.put(DATE_TIME, conModel.getDateTime());
        db.insert(TABLE_LOCATION, null, values);
        Log.v(TAG,values.toString());
        db.close();

    }

    /*Get Location*/
    public List<DatabaseModel> getAllLocation() {
        // TODO Auto-generated method stub
        List<DatabaseModel> Location = new ArrayList<DatabaseModel>();
        String selectQuery = "SELECT * FROM " + TABLE_LOCATION;
       // String selectQuery = "SELECT * FROM " + TABLE_IMAGES+" WHERE "+UPLOAD_FLAG+ "
       // LIKE"+"'false'"+" ORDER BY Id DESC LIMIT 1";

        SQLiteDatabase adb = this.getWritableDatabase();
        Cursor cursor = adb.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DatabaseModel contact = new DatabaseModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setLatitude(cursor.getString(1));
                contact.setLongitude(cursor.getString(2));
                contact.setDateTime(cursor.getString(3));
                // Adding contact to list
                Location.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return Location;

    }
    public int getLocationCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        cursor.close();
        return count;
    }
}
