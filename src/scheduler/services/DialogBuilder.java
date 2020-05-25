package scheduler.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import scheduler.controllers.Controller;

public class DialogBuilder {
    private Controller controller;
    private Node parentNode;
    private String title;
    private String data;
    private String xmlLocation;


    public DialogBuilder(Node parentNode, String xmlLocation) {
        this.parentNode = parentNode;
        this.xmlLocation = xmlLocation;
    }


    public void show() throws Exception  {
        // create and load dialog
        Dialog<Parent> dialog = new Dialog<> ();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(xmlLocation));
        Parent root = loader.load();

        dialog.getDialogPane().setContent(root);
        dialog.initOwner(parentNode.getScene().getWindow());
        System.out.println("check");
        dialog.show();
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


