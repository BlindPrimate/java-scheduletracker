package scheduler.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.models.Appointment;
import scheduler.services.Authenticator;
import scheduler.services.localization.TimeUtil;
import scheduler.services.localization.UserLocalization;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class RootController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> colTitle;
    @FXML private TableColumn<Appointment, String> colStartTime;
    @FXML private TableColumn<Appointment, String> colEndTime;
    @FXML private TableColumn<Appointment, String> colCustomer;
    @FXML private TableColumn<Appointment, String> colType;
    @FXML private TableColumn<Appointment, String> colDate;

    @FXML private ToggleButton toggleAll;
    @FXML private ToggleButton toggleWeek;
    @FXML private ToggleButton toggleMonth;


    private ObservableList<Appointment> appointments;
    private AppointmentAccessor accessor;
    private AppointmentAccessor apptAccessor = new AppointmentAccessor();

    public RootController() {
    }

    @FXML
    public void initialize() {


        /*
        EVALUATOR -- Plenty of lambda examples throughout this controller -- various uses, mostly to reduce boiler-plate code
         */

        appointments = apptAccessor.getAllAppointments(Authenticator.getInstance().getUserId());
        appointmentTable.setItems(appointments);

        // set time span toggle buttons to allow one clicked at a time
        List<ToggleButton> timeToggles = List.of(toggleAll, toggleMonth, toggleWeek);
        timeToggles.forEach(toggle -> {
            toggle.setOnAction(e -> {
                timeToggles.forEach(otherToggle -> {
                    otherToggle.setSelected(false);
                });
                toggle.setSelected(true);
                if (toggleAll.isSelected()) {
                    populateAll();
                } else if (toggleWeek.isSelected()) {
                    populateWeekly();
                } else if (toggleMonth.isSelected()) {
                    populateMonthly();
                }

            });
        });


        DateTimeFormatter readableTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

        // set column cell values
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));


        /*
        EVALUATOR  -- use of lambdas here to avoid confusing and difficult to read boiler-plate code
        */
        colStartTime.setCellValueFactory(columnData -> {
            String t = columnData.getValue().getStartTimeStamp().toLocalDateTime().format(readableTime);
            return new SimpleObjectProperty<>(t);
        });
        colEndTime.setCellValueFactory(columnData -> {
            String t = columnData.getValue().getEndTimeStamp().toLocalDateTime().format(readableTime);
            return new SimpleObjectProperty<>(t);
        });
        colDate.setCellValueFactory(columnData -> {
            String t = columnData.getValue()
                    .getEndTimeStamp()
                    .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return new SimpleObjectProperty<>(t);
        });
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        // set appointment display to all on open
        toggleAll.fire();
        // select first item
        appointmentTable.getSelectionModel().select(0);


        // check for upcoming appointments (next 15 min)
        AppointmentAccessor accessor = new AppointmentAccessor();
        Appointment upcoming = accessor.checkUpcomingAppointments();
        if (upcoming != null) {
            String start = TimeUtil.toUserTime(upcoming.getStartTime()).format(DateTimeFormatter.ofPattern("hh:mm a"));
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("You have an upcoming appointment with " +
                    upcoming.getCustomerName() +
                    " at " + start);
            alert.showAndWait();
        }
    }

    public void populateAll() {
        appointments = apptAccessor.getAllAppointments(Authenticator.getInstance().getUserId());
        appointmentTable.setItems(appointments);
    }

    public void populateWeekly() {
        appointments = apptAccessor.getWeeklyAppointments(Authenticator.getInstance().getUserId());
        appointmentTable.setItems(appointments);
    }

    public void populateMonthly() {
        appointments = apptAccessor.getMonthlyAppointments(Authenticator.getInstance().getUserId());
        appointmentTable.setItems(appointments);
    }

    // event handlers
    public void handleAddButton() {
        try {
            Stage newStage = new Stage();
            ResourceBundle bundle = UserLocalization.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/makeAppointment.fxml"), bundle);
            loader.setController(new AddAppointmentController());
            Parent root = loader.load();

            // refresh table of appointments on return to main screen
            newStage.initOwner(appointmentTable.getScene().getWindow());
            newStage.setTitle(bundle.getString("addAppointment"));
            newStage.setScene(new Scene(root));
            newStage.showAndWait();
            populateAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleModifyButton() {
        try {
            Stage newStage = new Stage();
            ResourceBundle bundle = UserLocalization.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/makeAppointment.fxml"), bundle);

            // load controller with selected appointment
            ModifyAppointmentController controller = new ModifyAppointmentController(appointmentTable.getSelectionModel().getSelectedItem());
            loader.setController(controller);
            Parent root = loader.load();

            /*
            * EVALUATOR -- Good lambda use to keep from having a lot of boiler-plate code with method overrides
            * */

            // refresh table of appointments on return to main screen
            newStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, e -> {
                populateAll();
            });
            newStage.setTitle(bundle.getString("modifyAppointment"));
            newStage.initOwner(appointmentTable.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.showAndWait();
            populateAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteButton() {
        AppointmentAccessor accessor = new AppointmentAccessor();
        Appointment appt = appointmentTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you wish to delete this appointment?");
        alert.setTitle("Delete Appointment");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            accessor.deleteAppointment(appt.getId());
            appointments.remove(appointmentTable.getSelectionModel().getSelectedIndex());
        } else {
            alert.close();
        }
    }

    public void handleManageCustomers() {
        try {
            Stage newStage = new Stage();
            ResourceBundle bundle = UserLocalization.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/customers.fxml"), bundle);

            Parent root = loader.load();

            newStage.setTitle(bundle.getString("manageCustomers"));
            newStage.initOwner(appointmentTable.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleReports() {
        try {
            Stage newStage = new Stage();
            ResourceBundle bundle = UserLocalization.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/reports.fxml"), bundle);

            Parent root = loader.load();

            newStage.setTitle(bundle.getString("reports"));
            newStage.initOwner(appointmentTable.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLogout() {
        try {
            Stage primaryStage = new Stage();
            ResourceBundle bundle = UserLocalization.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/login.fxml"), bundle);
            Parent root = loader.load();
            primaryStage.setTitle(bundle.getString("login"));
            primaryStage.setScene(new Scene(root));
            Stage stage = (Stage)appointmentTable.getScene().getWindow();
            stage.close();
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void handleExit() {
        Stage stage = (Stage)appointmentTable.getScene().getWindow();
        stage.close();
    }
}
