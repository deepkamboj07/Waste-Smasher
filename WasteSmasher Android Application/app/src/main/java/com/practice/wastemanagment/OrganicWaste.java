package com.practice.wastemanagment;

public class OrganicWaste {
    String BioRequestId;
    String username;
    String address;
    String pincode,city,type,userDp,userId,postImg,phoneNumber;
    String status;

    public OrganicWaste() {
    }

    public OrganicWaste(String username, String address, String pincode, String city, String type, String userDp, String userId, String postImg, String phoneNumber) {
        this.username = username;
        this.address = address;
        this.pincode = pincode;
        this.city = city;
        this.type = type;
        this.userDp = userDp;
        this.userId = userId;
        this.postImg = postImg;
        this.phoneNumber=phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBioRequestId() {
        return BioRequestId;
    }

    public void setBioRequestId(String bioRequestId) {
        BioRequestId = bioRequestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserDp() {
        return userDp;
    }

    public void setUserDp(String userDp) {
        this.userDp = userDp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }
}
