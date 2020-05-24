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

            ResultSet res = stm.executeQuery("select customer.customerId, customer.customerName, " +
                    "user.userName, user.userId, " +
                    "appointment.title, appointment.type, appointment.start, appointment.end " +
                    "from appointment " +
                    "inner join customer " +
                    "on appointment.customerId = customer.customerId " +
                    "inner join user " +
                    "on appointment.userId = user.userId");
            while(res.next()) {
                String user = res.getString("userName");
                String customer = res.getString("customerName");
                Date startTime = res.getDate("start");
                String type = res.getString("type");
                Date endTime = res.getDate("end");
                appointments.add(new Appointment(startTime, endTime, customer, user, type));
            }
            return appointments;
        } catch (SQLException e) {
            System.out.println("Error retrieving customer appointment data");
            e.printStackTrace();
        }

        return null;
    }
}
