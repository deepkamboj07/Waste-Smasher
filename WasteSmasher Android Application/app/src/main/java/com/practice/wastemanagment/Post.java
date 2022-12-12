package com.practice.wastemanagment;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postId;
    private String tittle;
    private String city;
    private String description;
    private String postPicture;
    private String userId;
    private String userDP;
    private String userName;


    private Object timestamp;

    public Post()
    {

    }

    public Post(String tittle, String city, String description, String postPicture, String userId, String userDP, String userName) {
        this.tittle = tittle;
        this.city = city;
        this.description = description;
        this.postPicture = postPicture;
        this.userId = userId;
        this.userDP = userDP;
        this.timestamp = ServerValue.TIMESTAMP;
        this.userName=userName;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(String postPicture) {
        this.postPicture = postPicture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserDP() {
        return userDP;
    }

    public void setUserDP(String userDP) {
        this.userDP = userDP;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
