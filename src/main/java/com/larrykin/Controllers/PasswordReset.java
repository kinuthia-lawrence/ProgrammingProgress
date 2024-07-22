package com.larrykin.Controllers;

import com.larrykin.Utils.CREDENTIALS;
import com.larrykin.Utils.DatabaseConn;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.UUID;

public class PasswordReset implements Initializable {

    @FXML
    private Label EmailAlert;

    @FXML
    private TextField adminEmailTextField;

    @FXML
    private Label codeAlert;

    @FXML
    private TextField confirmNewPassword;

    @FXML
    private TextField emailCodeTextField;

    @FXML
    private TextField enterNewPassword;

    @FXML
    private Button getCodeButton;

    @FXML
    private AnchorPane passwordReset;

    @FXML
    private Button setNewPasswordButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getCodeButton.setOnAction(event -> OpenPasswordReset());
    }

    private void OpenPasswordReset() {
        if (!adminEmailTextField.getText().isBlank()) {
            String email = adminEmailTextField.getText();
            EmailAlert.setText("A password reset code has been sent to " + email);
            //check if email exists in the database. if email exists, generate a random code and send it to the email
            //if email does not exist, display an error message
            DatabaseConn connectNow = new DatabaseConn();//Creating an instance of the DatabaseConn class


            //? Check if the email exists in the database
            String checkEmail = "SELECT * FROM users WHERE email = '" + email + "'";
            try {
                Connection connectDB = connectNow.getConnection(); //this is the connection to the database
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(checkEmail);
                if (queryResult.next()) {
                    // Generate a random code
                    String randomCode = UUID.randomUUID().toString().substring(0, 6);
                    try {
                        // Compose the email content
                        String emailContent = "Your password reset code is: " + randomCode;

                        // Set up the email server properties
                        Properties properties = new Properties();
                        properties.put("mail.smtp.host", CREDENTIALS.getSmtpHost());
                        properties.put("mail.smtp.port", CREDENTIALS.getSmtpPort());
                        properties.put("mail.smtp.auth", "true");
                        properties.put("mail.smtp.socketFactory.port", CREDENTIALS.getSmtpPort());
                        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

                        // Create a new session with an authenticator
                        Authenticator auth = new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(CREDENTIALS.getEmailUsername(), CREDENTIALS.getEmailPassword());
                            }
                        };

                        Session session = Session.getInstance(properties, auth);

                        // Create a new email message
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(CREDENTIALS.getEmailUsername()));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                        message.setSubject("Password Reset Code");
                        message.setText(emailContent);

                        // Send the email
                        Transport.send(message);
                    } catch (MessagingException e) {
                        if (e.getCause() instanceof ConnectException) {
                            showAlert("Network Error", "Unable to connect to the email server. Please check your network connection and try again.");
                        } else if (e.getCause() instanceof UnknownHostException) {
                            showAlert("Network Error", "Unable to connect to the email server. Please contact the developer to fix the server address.");
                        } else if (e instanceof AuthenticationFailedException) {
                            showAlert("Authentication Error", "Invalid email username or password. Please contact Developer.");
                        } else {
                            showAlert("Email Error", "An error occurred while sending the email. Please try again later.");
                        }
                    } catch (Exception e) {
                        showAlert("Unexpected Error", "An unexpected error occurred while processing your request. Please try again.");
                    }
                    codeAlert.setText("Enter password reset code from " + email);
                    codeAlert.setVisible(true);
                    //?Disable the email textField and the password reset button
                    adminEmailTextField.setDisable(true);
                    getCodeButton.setDisable(true);
                    //? Display the password reset form
                    setNewPasswordButton.setDisable(false);
                    enterNewPassword.setDisable(false);
                    confirmNewPassword.setDisable(false);
                    emailCodeTextField.setDisable(false);
                    setNewPasswordButton.setOnAction(event -> SetNewPassword(randomCode, statement, email));
                } else {
                    EmailAlert.setText("Email does not exist !!!");
                    EmailAlert.setStyle("-fx-text-fill: red");
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred");
                alert.setContentText("An error occurred while processing your request. Please try again.");
                alert.showAndWait();

                e.printStackTrace();
                e.getCause();
            }
        } else {
            EmailAlert.setText("Please enter Admin email address !!!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void SetNewPassword(String randomCode, Statement statement, String email) {
        if (!emailCodeTextField.getText().isBlank() && !enterNewPassword.getText().isBlank() && !confirmNewPassword.getText().isBlank()) {
            if (emailCodeTextField.getText().equals(randomCode)) {
                if (enterNewPassword.getText().equals(confirmNewPassword.getText())) {
                    EmailAlert.setDisable(true);
                    //? Update the password in the database
                    String updatePassword = "UPDATE users SET password = '" + enterNewPassword.getText() + "' WHERE email = '" + email + "'";
                    try {
                        statement.executeUpdate(updatePassword);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Password Changed");
                        alert.setHeaderText("Password Changed Successfully");
                        alert.setContentText("Password changed successfully. You can now login with your new password.");
                        alert.showAndWait();

                        //? Clear the form and disable the form fields
                        adminEmailTextField.clear();
                        emailCodeTextField.clear();
                        enterNewPassword.clear();
                        confirmNewPassword.clear();
                        EmailAlert.setText("");
                        codeAlert.setText("");
                        EmailAlert.setDisable(true);

                        codeAlert.setDisable(true);
                        enterNewPassword.setDisable(true);
                        confirmNewPassword.setDisable(true);
                        emailCodeTextField.setDisable(true);
                        setNewPasswordButton.setDisable(true);


                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password Change Error");
                        alert.setHeaderText("Password Change Error");
                        alert.setContentText("An error occurred while changing your password. Please try again.");
                        alert.showAndWait();

                        e.printStackTrace();
                        e.getCause();
                    }
                } else {
                    EmailAlert.setText("Passwords do not match !!!");
                    codeAlert.setText("Passwords do not match !!!");
                    codeAlert.setStyle("-fx-text-fill: red");
                    EmailAlert.setStyle("-fx-text-fill: red");
                }
            } else {
                codeAlert.setText("Invalid code !!!. Please enter the correct code !!!");
                codeAlert.setStyle("-fx-text-fill: red");
            }
        } else {
            codeAlert.setText("Fill all fields !!!");
            codeAlert.setStyle("-fx-text-fill: red");
        }
    }
}
