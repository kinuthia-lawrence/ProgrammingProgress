package com.larrykin.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private BorderPane BorderPane;

    @FXML
    private Button addDashboardButton;

    @FXML
    private AnchorPane centerPane;

    @FXML
    private Button editDashboardButton;

    @FXML
    private HBox footerPane;

    @FXML
    private HBox headerPane1;

    @FXML
    private VBox leftPane;

    @FXML
    private AnchorPane logoImage;

    @FXML
    private ImageView notificationBellIcon;

    @FXML
    private Button notificationButton;

    @FXML
    private Label notificationCountLabel;

    @FXML
    private Button otherDashboardButton;

    @FXML
    private Button projectsDashboardButton;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<?> searchComboBox;

    @FXML
    private HBox searchPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private AnchorPane titleAnchorPane;

    @FXML
    private Label titleAnchorPane1;

    @FXML
    private Button todoDashboardButton;

    @FXML
    private Button viewDashboardButton;

}
