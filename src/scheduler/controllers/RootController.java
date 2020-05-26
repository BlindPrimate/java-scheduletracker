package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.models.Appointment;
import scheduler.services.SceneBuilder;

import java.util.Date;


public class RootController implements Controller {

    @FXML private TableView appointmentTable;
    @FXML private TableColumn<Appointment, Date> colStartTime;
    @FXML private TableColumn<Appointment, Date> colEndTime;
    @FXML private TableColumn<Appointment, String> colCustomer;
    @FXML private TableColumn<Appointment, String> colType;


    @FXML
    public void initialize() {
        colType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appointmentType"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("endTime"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customer"));

        appointmentTable.setItems(new AppointmentAccessor().getAllAppointments());

    }

    // event handlers
    public void handleAddButton() {
        SceneBuilder scene = new SceneBuilder(appointmentTable, "../views/makeAppointment.fxml");
        scene.show();
    }
    
    public void handleModifyButton() {
        Appointment targetAppt = (Appointment)appointmentTable.getSelectionModel().getSelectedItem();
        SceneBuilder scene = new SceneBuilder(appointmentTable, "../views/makeAppointment.fxml");
        scene.passObject(targetAppt);
        scene.show();
    }
    


    public void handleExit() {
        Stage stage = (Stage)appointmentTable.getScene().getWindow();
        stage.close();
    }
}
