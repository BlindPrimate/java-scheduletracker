package scheduler.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Appointment;
import scheduler.models.Customer;
import scheduler.services.localization.UserLocalization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddAppointmentController extends AppointmentController {


    public AddAppointmentController() {

    }

    @FXML
    public void initialize() {


        // load customer names in dropdown
        CustomerAccessor access = new CustomerAccessor();
        choiceCustomer.setItems(access.getAllCustomers());

        // load times
        appointmentStartTimes = AppointmentController.buildAppointmentTime();
        appointmentEndTimes = AppointmentController.buildAppointmentTime();

        // set times
        choiceStartTime.setItems(appointmentStartTimes);
        choiceStartTime.setValue(LocalTime.of(8, 0)); // default start time
        choiceEndTime.setItems(appointmentEndTimes);
        choiceEndTime.setValue(LocalTime.of(8, 15));  // default end time


        // set dates
        choiceStartDate.setValue(LocalDate.now());



        // format times to display in 12hr am/pm format in combo boxes
        choiceStartTime.setCellFactory(cv -> timeFactory());
        choiceEndTime.setCellFactory(cv -> timeFactory());
        choiceStartTime.setButtonCell(timeFactory());
        choiceEndTime.setButtonCell(timeFactory());

        // format names of customers in combo box
        choiceCustomer.setCellFactory(combo -> new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (customer == null || empty) {
                    setText(null);
                } else {
                    setText(customer.getName());
                }
            }
        });
        choiceCustomer.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (customer == null || empty) {
                    setText(null);
                } else {
                    setText(customer.getName());
                }
            }
        });


        comboType.setItems(FXCollections.observableArrayList("Scrum", "Presentation", "Strategic", "Project Managment"));


    }

    public void handleSave() {

        AppointmentAccessor accessor = new AppointmentAccessor();
        Appointment appointment = new Appointment();

        // set appointment attributes
        appointment.setTitle(fieldTitle.getText().trim());


        LocalDateTime appointmentStart = LocalDateTime.of(choiceStartDate.getValue(), choiceStartTime.getValue());
        LocalDateTime appointmentEnd = LocalDateTime.of(choiceStartDate.getValue(), choiceEndTime.getValue());

        appointment.setStartTime(appointmentStart);
        appointment.setEndTime(appointmentEnd);
        appointment.setAppointmentType(comboType.getValue());
        appointment.setCustomerId(choiceCustomer.getValue().getId());

        ResourceBundle bundle = UserLocalization.getBundle();

        // check for overlap
        boolean hasOverlap = accessor.hasAppointmentOverlap(appointment);
        if (hasOverlap) {
            alertText.setText(bundle.getString("alertOverlap"));
        } else {
            accessor.addAppointment(appointment);
            // close window
            handleExit();
        }
    }


    public void handleExit() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}
