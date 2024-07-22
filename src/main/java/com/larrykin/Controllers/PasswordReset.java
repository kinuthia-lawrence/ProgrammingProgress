package com.larrykin.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PasswordReset {

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

}
