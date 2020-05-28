package scheduler.controllers;

import javafx.fxml.FXML;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Appointment;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Predicate;

public class ModifyAppointmentController extends AppointmentController {

    public ModifyAppointmentController(Appointment appointment) {
        super();
        this.appointment = appointment;
    }


    @FXML
    public void initialize() {

        // fill observables for appointment time listings
        appointmentStartTimes = AppointmentController.buildAppointmentTime();
        appointmentEndTimes = AppointmentController.buildAppointmentTime();

        // load customer names in dropdown
        CustomerAccessor access = new CustomerAccessor();
        choiceCustomer.setItems(access.getAllCustomerNames());


        choiceStartTime.setItems(appointmentStartTimes);
        choiceEndTime.setItems(filteredEndTimes);


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


    public void initData() {

        // fill start end times with appointment data
//        choiceStartTime.setValue(appointment.getStartTimeStamp().toLocalDateTime().format(parseTime));
//        choiceEndTime.setValue(appointment.getEndTimeStamp().toLocalDateTime().format(parseTime));

        fieldTitle.setText(appointment.getTitle());
        fieldDescription.setText(appointment.getDescription());
        fieldType.setText(appointment.getAppointmentType());

    }

    public void handleSave() {

        AppointmentAccessor accessor = new AppointmentAccessor();

        // set appointment attributes
        currentAppointment.setTitle(fieldTitle.getText().trim());
        currentAppointment.setDescription(fieldDescription.getText().trim());

        LocalTime startTime = LocalTime.parse(choiceStartTime.getValue(), parseTime);
        LocalDateTime appointmentStart = LocalDateTime.of(choiceStartDate.getValue(), startTime);

        Timestamp sqlStartTime = Timestamp.valueOf(appointmentStart);

        appointment.setStartTime(sqlStartTime);
        appointment.setEndTime(sqlStartTime);
        appointment.setAppointmentType("scrum");
        appointment.setCreatedBy("bonkers");
        appointment.setCustomerId(1);
        appointment.setCreatedById(1);

        accessor.modifyAppointment(appointment);

    }

//    public boolean isValidForm() {
//
//
//    }


}
