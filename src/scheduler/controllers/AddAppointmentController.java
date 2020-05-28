package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Appointment;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Predicate;

public class AddAppointmentController extends AppointmentController {

    public AddAppointmentController() {
        super();
    }

    @FXML
    public void initialize() {

        // load customer names in dropdown
        CustomerAccessor access = new CustomerAccessor();
        choiceCustomer.setItems(access.getAllCustomerNames());

        choiceStartDate.setValue(LocalDate.now());
        choiceEndDate.setValue(LocalDate.now());

        // set opening hours to 8am
        LocalTime time = LocalTime.of(8, 0);

        // create dropdown items for appointment hours
        while (!time.equals(LocalTime.of(18, 0))) {
            appointmentStartTimes.add(time.format(parseTime));
            appointmentEndTimes.add(time.format(parseTime));
            time = time.plusMinutes(15);
        }
        choiceStartTime.setItems(appointmentStartTimes);
        choiceStartTime.setValue(appointmentStartTimes.get(0));
        choiceEndTime.setItems(filteredEndTimes);
        choiceEndTime.setValue(filteredEndTimes.get(1));


        // filter predicate to remove times earlier than start time from end time list
        Predicate<String> predicate = (endTime) -> {
            // formatter to read time strings -- turn into LocalTime
            LocalTime startTime = LocalTime.parse(choiceStartTime.getValue(), parseTime);
            LocalTime endTimeToFilter = LocalTime.parse(endTime.toString(), parseTime);
            return endTimeToFilter.isAfter(startTime);
        };


        // filter list to remove times later than chosen start time
        choiceStartTime.setOnAction((event) -> {

            // keep end times where they are if chosen end time is after chosen start time at time of choice
            int starTimeIndex = choiceStartTime.getSelectionModel().getSelectedIndex();
            int endTimeIndex = choiceEndTime.getSelectionModel().getSelectedIndex();
            int indexDiff = endTimeIndex - starTimeIndex;

            // filter end times
            filteredEndTimes.setPredicate(null);
            filteredEndTimes.setPredicate(predicate);

            // smart end time setting after filter
            if (indexDiff > 0) {
                choiceEndTime.setValue(filteredEndTimes.get(indexDiff));
            } else {
                choiceEndTime.setValue(filteredEndTimes.get(0));
            }
        });
    }

    public void handleSave() {

        AppointmentAccessor accessor = new AppointmentAccessor();
        Appointment appointment = new Appointment();

        // set appointment attributes
        currentAppointment.setTitle(fieldTitle.getText().trim());
        currentAppointment.setDescription(fieldDescription.getText().trim());

        LocalTime startTime = LocalTime.parse(choiceStartTime.getValue(), parseTime);
        LocalTime endTime = LocalTime.parse(choiceEndTime.getValue(), parseTime);
        LocalDateTime appointmentStart = LocalDateTime.of(choiceStartDate.getValue(), startTime);
        LocalDateTime appointmentEnd = LocalDateTime.of(choiceEndDate.getValue(), endTime);

        Timestamp sqlStartTime = Timestamp.valueOf(appointmentStart);
        Timestamp sqlEndTime = Timestamp.valueOf(appointmentEnd);

        appointment.setStartTime(sqlStartTime);
        appointment.setEndTime(sqlEndTime);
        appointment.setAppointmentType("scrum");
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
