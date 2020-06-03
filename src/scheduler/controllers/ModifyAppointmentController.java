package scheduler.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Appointment;
import scheduler.models.Customer;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ModifyAppointmentController extends AppointmentController {

    public ModifyAppointmentController(Appointment appointment) {
        super();
        this.appointment = appointment;
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
        choiceStartDate.setValue(appointment.getStartTimeStamp().toLocalDateTime().toLocalDate());


        // format times to display in 12hr am/pm format in combo boxes
        choiceStartTime.setCellFactory(cv -> timeFactory());
        choiceEndTime.setCellFactory(cv -> timeFactory());
        choiceStartTime.setButtonCell(timeFactory());
        choiceEndTime.setButtonCell(timeFactory());

        fieldTitle.setText(appointment.getTitle());
        comboType.setValue(appointment.getAppointmentType());

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


        choiceCustomer.setValue(appointment.getCustomer());
        comboType.setItems(FXCollections.observableArrayList("Scrum", "Presentation", "Strategic", "Project Managment"));


        // modify controller specific set values
        comboType.setValue(appointment.getAppointmentType());
        choiceStartTime.setValue(appointment.getStartTimeStamp().toLocalDateTime().toLocalTime());
        choiceEndTime.setValue(appointment.getEndTimeStamp().toLocalDateTime().toLocalTime());
        choiceStartDate.setValue(appointment.getStartTimeStamp().toLocalDateTime().toLocalDate());

    }

    public void handleSave() {  // modify

        AppointmentAccessor accessor = new AppointmentAccessor();

        // set appointment attributes
        appointment.setTitle(fieldTitle.getText().trim());
        appointment.setAppointmentType(comboType.getValue());
        appointment.setCustomerId(choiceCustomer.getSelectionModel().getSelectedItem().getId());

        LocalDateTime appointmentStart = LocalDateTime.of(choiceStartDate.getValue(), choiceStartTime.getValue());
        LocalDateTime appointmentEnd = LocalDateTime.of(choiceStartDate.getValue(), choiceEndTime.getValue());

        appointment.setStartTime(appointmentStart);
        appointment.setEndTime(appointmentEnd);
        accessor.modifyAppointment(appointment);

        // close window
        handleExit();

    }


}
