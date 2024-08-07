package com.larrykin.Controllers;

import com.larrykin.Utils.DatabaseConn;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.fxml.FXML;

import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private Hyperlink helpHyperlink;
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

    //create a new instance of the database connection
    DatabaseConn connectNow = new DatabaseConn();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        helpHyperlink.setOnAction(event -> openHelpPage());
        createNewAccountHyperlink.setOnAction(event -> handlePasswordDialog());
        forgotPasswordHyperlink.setOnAction(event -> handlePasswordDialog());

        launchBackgroundImage();

    }

    private void launchBackgroundImage() {
        // Load the image as a resource
        Image backgroundImage = new Image(getClass().getResourceAsStream("/IMAGES/free-photo-of-shiny-water-at-sunset.jpeg"));
        // Create a BackgroundImage
        // Create a BackgroundSize with cover-like behavior
        BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, true);
// Create the BackgroundImage with the new BackgroundSize
        BackgroundImage bgImage = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
        // Set the BackgroundImage to the AnchorPane
        mainAnchorPane.setBackground(new Background(bgImage));
    }

    //? Hadle reset password or Create new account
    private void handlePasswordDialog() {
        //? opne an alert dialog to confirm if the user wants to reset their password
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ACCOUNT SETTINGS");
        alert.setHeaderText("Account Settings");
        alert.setContentText("Do you want to alter with Account Settings?");
        //? Set the text color to red
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.lookup(".header-panel").setStyle("-fx-text-fill: red;");
        dialogPane.lookup(".content").setStyle("-fx-text-fill: red;");
        //? Add an icon
        Image image = new Image(getClass().getResourceAsStream("/IMAGES/passwordLock.png"));
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);

        alert.showAndWait();
        //? if the user clicks the OK button, the reset password dialog will be shown
        if (alert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/AccountSettings.fxml"));
                Stage resetPasswordStage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                resetPasswordStage.setScene(scene);
                resetPasswordStage.setTitle("Account Settings");
                resetPasswordStage.setResizable(false);
                resetPasswordStage.centerOnScreen();
                resetPasswordStage.sizeToScene();
//                resetPasswordStage.initStyle(StageStyle.UNDECORATED);
                resetPasswordStage.show();
                Image icon = new Image(getClass().getResourceAsStream("/IMAGES/passwordLock.png"));
                resetPasswordStage.getIcons().add(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //? Handle Help
    private void openHelpPage() {
        try {
            String markdownContent = new String(Files.readAllBytes(Paths.get("HELP.md")));
            Parser parser = Parser.builder().build();
            Document document = parser.parse(markdownContent);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String htmlContent = renderer.render(document);

            WebView webView = new WebView();
            webView.getEngine().loadContent(htmlContent);
            webView.getEngine().setUserStyleSheetLocation(getClass().getResource("/STYLES/darkTheme.css").toString());

            Stage helpStage = new Stage();
            helpStage.setScene(new Scene(webView, 800.0, 600.0));
            helpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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


        //query to check if the login credentials are correct
        String verifyLogin = "SELECT count(1) FROM users WHERE email = '" + email + "' AND password = '" + password + "'";

        try {
            Connection connectDB = connectNow.getConnection(); //! try with resources closes the connection automatically

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
            dashboardStage.setFullScreen(true);
            dashboardStage.show();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
        }
    }

}
