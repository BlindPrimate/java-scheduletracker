package scheduler.controllers;

import javafx.fxml.FXML;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.dbAccessors.CustomerAccessor;
import scheduler.models.Appointment;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.function.Predicate;

public class ModifyAppointmentController extends AppointmentController {

    public ModifyAppointmentController(Appointment appointment) {
        this.appointment = appointment;
    }


    @FXML
    public void initialize() {

//        field.setText()

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
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendPattern("h:mm a")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
            LocalTime startTime = LocalTime.parse(choiceStartTime.getValue(), formatter);
            LocalTime endTimeToFilter = LocalTime.parse(endTime.toString(), formatter);
            return endTimeToFilter.isAfter(startTime);
        };

        // filter list to remove times later than chosen start time
        choiceStartTime.setOnAction((event) -> {
            filteredEndTimes.setPredicate(null);
            filteredEndTimes.setPredicate(predicate);
        });
    }


    public void initData() {

        // fill start end times with appointment data
        System.out.println(appointment.getStartTimeStamp().toLocalDateTime().format(parseTime));
        System.out.println(appointment.getEndTimeStamp().toLocalDateTime().format(parseTime));
        choiceStartTime.setValue(appointment.getStartTimeStamp().toLocalDateTime().format(parseTime));
        choiceEndTime.setValue(appointment.getEndTimeStamp().toLocalDateTime().format(parseTime));

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
