package scheduler.dbAccessors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
    private static Connection conn = null;

    private DBConnection() {

    }

    public static Connection getConnection() {

        // return connection if already started
        if (conn != null) {
//            System.out.println(conn);
            return conn;
        }

        String serverName = "jdbc:mysql://3.227.166.251/U075i9";
        String dbUser = "U075i9";
        String pass = "53688948425";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e ) {
            System.out.println("Database driver failed to load");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(serverName, dbUser, pass);
        } catch(SQLException e) {
            System.out.println("Could not open connection to database");
            e.printStackTrace();
        }

        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Could not close connection to database");
            e.printStackTrace();
        }

    }

}
