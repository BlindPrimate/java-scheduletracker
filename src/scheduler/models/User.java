package scheduler.models;

import scheduler.dbAccessors.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private String username;
    private int userID;
    private String password;

    public User(int userID, String password) {
        this.userID = userID;
        this.password = password;
    }


    public boolean isAuthorized() {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("select user.id, user.password from user where user.id = ?");
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            // compare password to DB
            if (rs.next() && rs.getString("password").equals(password)) {
                this.username = rs.getString("userName");
                this.userID = rs.getInt("userId");
                return true;
            }
        } catch (
                SQLException e ) {
            e.printStackTrace();
        }
        return false;
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
