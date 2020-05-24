package scheduler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduler.dbAccessors.AppointmentAccessor;
import scheduler.models.Appointment;

import java.util.Date;


public class RootController {

    @FXML private TableView appointmentTable;
    @FXML private TableColumn<Appointment, Date> colTime;
    @FXML private TableColumn<Appointment, String> colCustomer;


    @FXML
    public void initialize() {

//        colCustomer.setCellValueFactory(customer -> {
//            System.out.println(customer);
//            return new SimpleStringProperty("knock");
//        });
        colTime.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("startTime"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customer"));

        appointmentTable.setItems(new AppointmentAccessor().getAllAppointments());

    }
}
