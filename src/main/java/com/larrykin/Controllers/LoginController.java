package com.larrykin.Controllers;

import com.larrykin.Utils.DatabaseConn;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;

import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class LoginController {
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
    public static String password;


    //! close window when cancel button is clicked
    public void cancelButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    public void loginButtonOnAction(ActionEvent actionEvent) {
        if (!emailTextfield.getText().isBlank() && !passwordField.getText().isBlank()) {
            email = emailTextfield.getText();
            password = passwordField.getText();
            //? check login credentials
            verifyLoginCredentials(email, password);
        } else {
            alertLabel.setText("Please enter your email and password");
            if (passwordField.getText().isBlank()) {
                alertLabel.setText("Please enter your password");
            }
            if (emailTextfield.getText().isBlank()) {
                alertLabel.setText("Please enter your email");
            }
        }
        //! Set accelerator for login button (Shift+E)
        KeyCombination keyCombination = new KeyCodeCombination(KeyCode.E, KeyCombination.SHIFT_DOWN);
        loginButton.getScene().getAccelerators().put(keyCombination, this::dashboard);
    }

    private void verifyLoginCredentials(String email, String password) {
        //create a new instance of the database connection
        DatabaseConn connectNow = new DatabaseConn();
        Connection connectDB = connectNow.getConnection();

        //query to check if the login credentials are correct
        String verifyLogin = "SELECT count(1) FROM users WHERE email = '" + email + "' AND password = '" + password + "'";

        try {
            Statement statement = connectDB.createStatement(); //create a statement object
            ResultSet queryResult = statement.executeQuery(verifyLogin); //execute the query

            //? the queryResult will return a 1 if the login credentials are correct
            //? and a 0 if the login credentials are incorrect
            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    dashboard();
                    Stage loginWindow = (Stage) loginButton.getScene().getWindow();
                    loginWindow.close();
                } else {
                    passwordField.clear();
                    emailTextfield.clear();

                    alertLabel.setText("Invalid Login. Please try again.");

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Error");
                    alert.setHeaderText("Invalid Login");
                    alert.setContentText("Invalid Login. Please try again.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            System.out.println("There was a problem with the SQL query or the database connection.");
            e.printStackTrace();
            e.getCause();
        } catch (NullPointerException e) {
            System.out.println("An object was not initialized before it was used.");
            e.printStackTrace();
            e.getCause();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            e.printStackTrace();
            e.getCause();
        }
    }

    private void dashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Dashboard.fxml"));
            Stage dashboardStage = new Stage();
            Scene dashboardScene = new Scene(fxmlLoader.load());
            dashboardStage.setScene(dashboardScene);
            dashboardStage.setTitle(email + "   Programming Progress Tracker");
            Image icon = new Image(getClass().getResourceAsStream("/IMAGES/pexels-cottonbro-6804581.jpg"));
            dashboardStage.getIcons().add(icon);
            dashboardStage.show();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
        }
    }

}
