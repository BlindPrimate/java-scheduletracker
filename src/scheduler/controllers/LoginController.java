package scheduler.controllers;

import javafx.fxml.FXML;
import scheduler.dbAccessors.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class LoginController implements Controller {

    @FXML
    private void initialize() {
        Connection conn = DBConnection.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet set = stm.executeQuery("select * from country");
            while(set.next()) {
                System.out.println(set.getString("country"));
            }
        } catch (SQLException e ) {

        }

    }
}
