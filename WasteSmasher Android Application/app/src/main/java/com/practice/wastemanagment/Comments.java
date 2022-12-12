package com.practice.wastemanagment;

import com.google.firebase.database.ServerValue;

public class Comments {
    String userId;
    String userImg;
    String content;
    String userName;

    Object timeStamp;

    public Comments() {
    }

    public Comments(String userId, String userImg, String content, String userName) {
        this.userId = userId;
        this.userImg = userImg;
        this.content = content;
        this.userName = userName;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
