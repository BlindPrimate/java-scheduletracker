package scheduler.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.controllers.Controller;

public class SceneBuilder {
    private Controller controller;
    private Node parentNode;
    private String title;
    private String data;
    private String xmlLocation;
    private Object objectToPass = null;


    public SceneBuilder(Node parentNode, String xmlLocation) {
        this.parentNode = parentNode;
        this.xmlLocation = xmlLocation;
    }


    public void show() {
        // create and load dialog
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlLocation));
            Parent root = loader.load();
            if (this.objectToPass != null) {
                System.out.println("passed object");
            }
            newStage.initOwner(parentNode.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void passObject(Object object) {
        this.objectToPass = object;
    }


    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
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


