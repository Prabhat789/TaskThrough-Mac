package com.pktworld.taskthrough.db;

/**
 * Created by Prabhat on 27/03/16.
 */
public class DatabaseModel {

    int id;
    String staffId;
    String title;
    String review;
    String latitude;
    String longitude;
    String upladFlag;
    String dateTime;

    public DatabaseModel() {
    }

    public DatabaseModel(String staffId, String title, String review, String latitude, String longitude, String upladFlag, String dateTime) {
        this.staffId = staffId;
        this.title = title;
        this.review = review;
        this.latitude = latitude;
        this.longitude = longitude;
        this.upladFlag = upladFlag;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DatabaseModel(String latitude, String longitude,String dateTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
    }
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUpladFlag() {
        return upladFlag;
    }

    public void setUpladFlag(String upladFlag) {
        this.upladFlag = upladFlag;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
