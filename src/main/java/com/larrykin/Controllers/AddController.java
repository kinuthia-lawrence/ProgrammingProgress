package com.larrykin.Controllers;

import com.larrykin.Utils.DatabaseConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    private AnchorPane addAnchorPane;

    @FXML
    private DatePicker datePicker;


    @FXML
    private TextArea futureImprovementTextArea;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> milestoneComboBox;

    @FXML
    private TextArea milestoneDescription;

    @FXML
    private TextArea projectDescriptionTextArea;

    @FXML
    private TextField projectNameTextField;

    @FXML
    private Button saveButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the comboboxes
        populateLanguageComboBox();
        populateMilestoneComboBox();
    }

    private void populateMilestoneComboBox() {
        ObservableList<String> milestones = FXCollections.observableArrayList(
                "TODO ", "UX/UI", "SETUP ENVIRONMENT", "IN PROGRESS", "DONE"
        );
        milestoneComboBox.setItems(milestones);
    }

    private void populateLanguageComboBox() {
        ObservableList<String> languages = FXCollections.observableArrayList(
                "Java", "C", "C++", "Python", "JavaScript"
        );
        languageComboBox.setItems(languages);
    }

    public  void saveButtonAction(){
        //create a new instance of the database connection
        DatabaseConn connectNow = new DatabaseConn();
        Connection connectDB = connectNow.getConnection();

    }


}
