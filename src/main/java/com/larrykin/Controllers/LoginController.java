package com.larrykin.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;

import javafx.stage.Stage;




public class LoginController{
    @FXML
    private Label alertLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane childAnchorPane;

    @FXML
    private AnchorPane childAnchorPane2;

    @FXML
    private Hyperlink createNewAccountHyperlink;

    @FXML
    private TextField emailTextfield;

    @FXML
    private Hyperlink forgotPasswordHyperlink;

    @FXML
    private Button loginButton;

    @FXML
    private Label logo;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private PasswordField passwordField;

    public static String email;


    //! close window when cancel button is clicked
    public void cancelButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    public void loginButtonOnAction(ActionEvent actionEvent) {
        dashboard();
         email = "kinuthialwrence343@gmail.com";
    }

    private void dashboard() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Dashboard.fxml"));
            Stage dashboardStage = new Stage();
            Scene dashboardScene = new Scene(fxmlLoader.load());
            dashboardStage.setScene(dashboardScene);
            dashboardStage.setTitle( email + "   Programming Progress Tracker");
            Image icon = new Image(getClass().getResourceAsStream("/IMAGES/pexels-cottonbro-6804581.jpg"));
            dashboardStage.getIcons().add(icon);
            dashboardStage.show();

            Stage stage =(Stage) loginButton.getScene().getWindow();
            stage.close();
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}
