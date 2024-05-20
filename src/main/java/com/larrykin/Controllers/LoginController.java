package com.larrykin.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
