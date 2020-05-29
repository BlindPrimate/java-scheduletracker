package scheduler.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Appointment;
import scheduler.models.Customer;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Predicate;

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
        choiceEndTime.setItems(appointmentEndTimes);


        // set dates
        choiceStartDate.setValue(LocalDate.now());
        choiceEndDate.setValue(LocalDate.now());



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


        // set initial values for combo boxes
        choiceStartTime.setValue(LocalTime.of(8, 00));
        choiceEndTime.setValue(LocalTime.of(8, 15));

        // filter predicate to remove times earlier than start time from end time list
        Predicate<LocalTime> predicate = (endTime) -> {
            // formatter to read time strings -- turn into LocalTime
//            LocalTime startTime = choiceStartTime.getValue();
//            LocalTime endTimeToFilter = LocalTime.parse(endTime.toString(), parseTime);
//            return endTimeToFilter.isAfter(startTime);
            return true;
        };


        // filter list to remove times later than chosen start time
//        choiceStartTime.setOnAction((event) -> {
//
//            // keep end times where they are if chosen end time is after chosen start time at time of choice
//            int starTimeIndex = choiceStartTime.getSelectionModel().getSelectedIndex();
//            int endTimeIndex = choiceEndTime.getSelectionModel().getSelectedIndex();
//            int indexDiff = endTimeIndex - starTimeIndex;
//
//            // filter end times
            filteredEndTimes.setPredicate(null);
            filteredEndTimes.setPredicate(predicate);
//
//            // smart end time setting after filter
//            if (indexDiff > 0) {
//                choiceEndTime.setValue(filteredEndTimes.get(indexDiff));
//            } else {
//                choiceEndTime.setValue(filteredEndTimes.get(0));
//            }
//        });
    }

    public void handleSave() {

        AppointmentAccessor accessor = new AppointmentAccessor();
        Appointment appointment = new Appointment();

        // set appointment attributes
        currentAppointment.setTitle(fieldTitle.getText().trim());


        LocalDateTime appointmentStart = LocalDateTime.of(choiceStartDate.getValue(), choiceStartTime.getValue());
        LocalDateTime appointmentEnd = LocalDateTime.of(choiceEndDate.getValue(), choiceEndTime.getValue());

        Timestamp sqlStartTime = Timestamp.valueOf(appointmentStart);
        Timestamp sqlEndTime = Timestamp.valueOf(appointmentEnd);

        appointment.setStartTime(sqlStartTime);
        appointment.setEndTime(sqlEndTime);
        appointment.setAppointmentType(comboType.getValue());
        appointment.setCreatedBy("bonkers");
        appointment.setCustomerId(1);
        appointment.setCreatedById(1);

        accessor.addAppointment(appointment);


        // close window
        handleExit();

    }

//    public boolean isValidForm() {
//
//
//    }

    public void handleExit() {
        Stage stage = (Stage)mainPane.getScene().getWindow();
        stage.close();
    }
}
