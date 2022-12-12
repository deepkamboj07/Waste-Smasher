package com.practice.wastemanagment;

public class organicHistory {
    String userID;
    String postImg;
    String type;
    String status;
    String address;

    public organicHistory(){

    }

    public organicHistory(String postImg, String type, String status, String address, String userID) {
        this.postImg = postImg;
        this.type = type;
        this.status = status;
        this.address = address;
        this.userID=userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
