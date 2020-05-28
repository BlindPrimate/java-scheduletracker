package scheduler.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import scheduler.controllers.AppointmentController;
import scheduler.controllers.ModifyAppointmentController;
import scheduler.controllers.RootController;

public class SceneBuilder {
    private Node parentNode;
    private String title;
    private String data;
    private String xmlLocation;
    private Object objectToPass = null;
    private AppointmentController controller;


    public SceneBuilder(Node parentNode, AppointmentController controller, String xmlLocation) {
        this.controller = controller;
        this.parentNode = parentNode;
        this.xmlLocation = xmlLocation;
    }


    public void show() {
        // create and load dialog
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlLocation));
            loader.setController(controller);
            Parent root = loader.load();

            if (controller instanceof ModifyAppointmentController) {
                ((ModifyAppointmentController) controller).initData();
            }

            newStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, e -> {
                RootController.getInstance().populateAppointments();
            });
            newStage.initOwner(parentNode.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}


