package scheduler.models;

public class User {

    private String username;
    private int userID;
    private String password;

    public User() {

    }


    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
