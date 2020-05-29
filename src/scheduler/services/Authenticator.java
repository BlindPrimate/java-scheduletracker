package scheduler.services;

import scheduler.dbAccessors.DBConnection;
import scheduler.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authenticator {

    private static Authenticator authenticator;
    private User user = new User();
    private boolean isAuthorized = false;

    public static Authenticator getInstance() {
        if (authenticator == null) {
            authenticator = new Authenticator();
            return authenticator;
        }
        return authenticator;
    }

    private Authenticator() {

    }

    public int getUserId() {
        return user.getUserID();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    private void loadUser(int id, String username) {
        user = new User();
        user.setUserID(id);
        user.setUsername(username);
    }

    public boolean login(String username, String pass) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement( "select * from user where user.userName = ?" );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                if (rs.getString("password").equals(pass)) {
                    isAuthorized = true;
                    loadUser(rs.getInt("userid"), rs.getString("userName"));
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUserValid(String username) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement( "select * from user where user.userName = ?" );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
