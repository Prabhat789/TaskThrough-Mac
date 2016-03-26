package com.pktworld.taskthrough.model;

/**
 * Created by Prabhat on 26/03/16.
 */
public class LoginResponse {

    String response;
    String StaffId;
    String RedirectUrl;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String staffId) {
        StaffId = staffId;
    }

    public String getRedirectUrl() {
        return RedirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        RedirectUrl = redirectUrl;
    }
}
