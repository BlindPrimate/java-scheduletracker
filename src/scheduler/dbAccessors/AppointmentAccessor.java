package scheduler.dbAccessors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.models.Appointment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class AppointmentAccessor extends DataAccessor {

    private ObservableList<Appointment> appointments;
    private Connection conn = null;

    public AppointmentAccessor() {
        this.conn = DBConnection.getConnection();
        this.appointments = FXCollections.observableArrayList();
    }


    public ObservableList<Appointment> getAllAppointments() {
        try {
            Statement stm = conn.createStatement();

            ResultSet res = stm.executeQuery("select customer.customerId, customer.customerName, user.userName, user.userId, appointment.title, appointment.start, appointment.end   \n" +
                    "From appointment\n" +
                    "inner join customer\n" +
                    "on appointment.customerId = customer.customerId\n" +
                    "inner join user\n" +
                    "on appointment.userId = user.userId;");
            while(res.next()) {
                String user = res.getString("userName");
                String customer = res.getString("customerName");
                Date startTime = res.getDate("start");
                Date endTime = res.getDate("end");
                appointments.add(new Appointment(startTime, endTime, customer, user));
            }
            return appointments;
        } catch (SQLException e) {
            System.out.println("Error retrieving customer appointment data");
            e.printStackTrace();
        } finally {
            stm.close();
        }

        return null;
    }
}
