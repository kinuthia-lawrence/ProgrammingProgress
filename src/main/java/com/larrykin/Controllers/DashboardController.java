package com.larrykin.Controllers;

import com.larrykin.Models.Model;
import com.larrykin.Utils.ComboBoxUtils;
import com.larrykin.Views.DashboardOptions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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

import java.io.IOException;
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
    private ComboBox<String> searchComboBox;

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

    private ViewController viewController;

    public DashboardController() {

        //? Instantiate the view controller using FXMLLoader
        try {
            // Step 1: Create an FXMLLoader instance and set the location of the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/View.fxml"));

            // Step 2: Load the FXML file to initialize the ViewController and its components
            Parent view = loader.load(); // This line automatically creates an instance of ViewController and initializes it

            // Step 3: Get the ViewController instance from the loader if you need to use it
            viewController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception or log it
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ComboBoxUtils.populateLanguageComboBox(searchComboBox);
        searchComboBox.setValue("Java");

        addListeners();

        BorderPane.setCenter(Model.getInstance().getViewFactory().getViewAnchorPane());
        Model.getInstance().getViewFactory().getDashboardSelectedItem().addListener((observable, oldVal, newVal) -> {
            switch (newVal) {
                case ADD -> BorderPane.setCenter(Model.getInstance().getViewFactory().getAddAnchorPane());
                case EDIT -> BorderPane.setCenter(Model.getInstance().getViewFactory().getEditAnchorPane());
                case VIEW -> BorderPane.setCenter(Model.getInstance().getViewFactory().getViewAnchorPane());
                case PROJECT -> BorderPane.setCenter(Model.getInstance().getViewFactory().getProjectAnchorPane());
                case TODO -> BorderPane.setCenter(Model.getInstance().getViewFactory().getTodoAnchorPane());

            }
        });
    }

    //? adding listeners to the dashboard buttons
    private void addListeners() {
        viewDashboardButton.setOnAction(event -> viewDashboardButtonClicked());
        todoDashboardButton.setOnAction(event -> todoDashboardButtonClicked());
        addDashboardButton.setOnAction(actionEvent -> addDashboardButtonClicked());
        editDashboardButton.setOnAction(event -> editDashboardButtonClicked());
        projectsDashboardButton.setOnAction(event -> projectsDashboardButtonClicked());
        searchButton.setOnAction(event -> performSearch());
        logoutButton.setOnAction(event -> logoutButtonClicked());
    }

    private void performSearch() {

        //? Get the search text and the search type
        String searchText = searchTextField.getText();
        String selectedLanguage = searchComboBox.getValue();

        Model.getInstance().getViewFactory().getDashboardSelectedItem().set(DashboardOptions.VIEW);
        viewController.manualInitialize();
        viewController.populateSearchedData(selectedLanguage, searchText);
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
    public void logoutButtonClicked() {
        Model.getInstance().getViewFactory().showLogin();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

    }

}

