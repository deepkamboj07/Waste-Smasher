package Model;

public class User {
    private String id;
    private String UserName;
    private String UserEmail;
    private String UserAddress;
    private String UserCity;
    private String UserPin;
    private String UserPass;

    public User(String id, String userName, String userEmail, String userAddress, String userCity, String userPin, String userPass) {
        this.id = id;
        UserName = userName;
        UserEmail = userEmail;
        UserAddress = userAddress;
        UserCity = userCity;
        UserPin = userPin;
        UserPass = userPass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String userAddress) {
        UserAddress = userAddress;
    }

    public String getUserCity() {
        return UserCity;
    }

    public void setUserCity(String userCity) {
        UserCity = userCity;
    }

    public String getUserPin() {
        return UserPin;
    }

    public void setUserPin(String userPin) {
        UserPin = userPin;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

}
