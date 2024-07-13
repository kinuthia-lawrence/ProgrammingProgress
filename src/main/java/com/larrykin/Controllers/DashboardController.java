package com.larrykin.Controllers;

import com.larrykin.Main;
import com.larrykin.Models.Model;
import com.larrykin.Views.DashboardOptions;
import com.larrykin.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

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
    private Button todoDashboardButton;

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
    private Button logoutButton;

    @FXML
    private Button viewDashboardButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            addListeners();

            Model.getInstance().getViewFactory().getDashboardSelectedItem().addListener((observable, oldVal,newVal)->{
                switch (newVal){
                    case ADD ->BorderPane.setCenter(Model.getInstance().getViewFactory().getAddAnchorPane());
                    case EDIT ->BorderPane.setCenter(Model.getInstance().getViewFactory().getEditAnchorPane());
                    case VIEW ->BorderPane.setCenter(Model.getInstance().getViewFactory().getViewAnchorPane());
                    case PROJECT ->BorderPane.setCenter(Model.getInstance().getViewFactory().getProjectAnchorPane());
                    case TODO ->BorderPane.setCenter(Model.getInstance().getViewFactory().getTodoAnchorPane());

                }
            });
    }

    //? adding listeners to the dashboard buttons
    private void addListeners() {
        viewDashboardButton.setOnAction(event -> viewDashboardButtonClicked());
        todoDashboardButton.setOnAction(event ->todoDashboardButtonClicked());
        addDashboardButton.setOnAction(actionEvent -> addDashboardButtonClicked());
        editDashboardButton.setOnAction(event -> editDashboardButtonClicked());
        projectsDashboardButton.setOnAction(event ->projectsDashboardButtonClicked());
        logoutButton.setOnAction(event ->logoutButtonClicked());
    }

    private void projectsDashboardButtonClicked() {
        Model.getInstance().getViewFactory().getDashboardSelectedItem().set(DashboardOptions.PROJECT);
    }

    private void editDashboardButtonClicked() {
        Model.getInstance().getViewFactory().getDashboardSelectedItem().set(DashboardOptions.EDIT);
    }

    private void addDashboardButtonClicked() {
        Model.getInstance().getViewFactory().getDashboardSelectedItem().set(DashboardOptions.ADD);
    }

    private void todoDashboardButtonClicked() {
        Model.getInstance().getViewFactory().getDashboardSelectedItem().set(DashboardOptions.TODO);
    }

    private void viewDashboardButtonClicked() {
        Model.getInstance().getViewFactory().getDashboardSelectedItem().set(DashboardOptions.VIEW);
    }
    //? Logout
    public void logoutButtonClicked(){
        Model.getInstance().getViewFactory().showLogin();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

    }

}
