package scheduler.dbAccessors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.models.Appointment;

import java.sql.*;
import java.util.Date;

public class AppointmentAccessor {

    private ObservableList<Appointment> appointments;
    private Connection conn = null;

    public AppointmentAccessor() {
        this.conn = DBConnection.getConnection();
        this.appointments = FXCollections.observableArrayList();
    }

    public ObservableList<Appointment> getAllAppointments() {
        try {
            Statement stm = conn.createStatement();

            // retrieve all appointments INNER JOIN with appointment, customer, and user tables
            ResultSet res = stm.executeQuery("select customer.customerId, customer.customerName, " +
                    "user.userName, user.userId, " +
                    "appointment.title, appointment.type, appointment.start, appointment.end " +
                    "from appointment " +
                    "inner join customer " +
                    "on appointment.customerId = customer.customerId " +
                    "inner join user " +
                    "on appointment.userId = user.userId");

            // loop results and create observable array of appointments
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

    public boolean addAppointment(Appointment appt) {
        try {
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO `appointment` VALUES " +
                    "(default,1,1,'not needed','not needed','not needed','not needed','Presentation','not needed','2019-01-01 00:00:00','2019-01-01 00:00:00','2019-01-01 00:00:00','test','2019-01-01 00:00:00','test')"
            );
            ResultSet rs = stm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving customer appointment data");
            e.printStackTrace();
        } finally {
            return true;
        }
    }
}
