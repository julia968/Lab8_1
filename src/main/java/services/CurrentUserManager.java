package services;

public class CurrentUserManager {
    private String userName;

    public CurrentUserManager(String userName) {
        this.userName = userName;
    }

    public CurrentUserManager() {
    }

    public String getUserName() {
        return userName;
    }

    public CurrentUserManager setUserName(String userName) {
        this.userName = userName;
        return this;
    }


}
