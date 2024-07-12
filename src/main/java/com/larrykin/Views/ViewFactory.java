package com.larrykin.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewFactory {

    private final ObjectProperty dashboardSelectedItem;
    private AnchorPane addAnchorPane, editAnchorPane, viewAnchorPane, otherAnchorPane, projectAnchorPane, todoAnchorPane;


    //?constructor


    public ViewFactory() {
        this.dashboardSelectedItem = new SimpleObjectProperty();
    }


    //?getting the selected item
    public ObjectProperty<DashboardOptions> getDashboardSelectedItem() {
        return dashboardSelectedItem;
    }

    //? getting resources
    public AnchorPane getAddAnchorPane() {
        if (addAnchorPane == null) {
            try {
                addAnchorPane = new FXMLLoader(getClass().getResource("/FXML/Add.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return addAnchorPane;
    }

    //? Show login
    public void showLogin() {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
        Scene loginScene = null;
        try {
            loginScene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
        }
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Programming Progress Tracker");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}