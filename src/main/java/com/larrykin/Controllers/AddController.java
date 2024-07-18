package com.larrykin.Controllers;

import com.larrykin.Utils.ComboBoxUtils;
import com.larrykin.Utils.DatabaseConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
        // Populate the comboboxes using the utility class
        ComboBoxUtils.populateLanguageComboBox(languageComboBox);
        ComboBoxUtils.populateMilestoneComboBox(milestoneComboBox);
        saveButton.setOnAction(actionEvent -> saveButtonAction());
    }


    public void saveButtonAction() {
        //check if fields are empty
        if (!projectNameTextField.getText().isBlank() && !projectDescriptionTextArea.getText().isBlank() && !milestoneDescription.getText().isBlank() && !futureImprovementTextArea.getText().isBlank() && datePicker.getValue() != null && languageComboBox.getValue() != null && milestoneComboBox.getValue() != null) {
            addProject();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
        }
    }

    private void addProject() {
        //create a new instance of the database connection
        DatabaseConn connectNow = new DatabaseConn();
        Connection connectDB = connectNow.getConnection();
    }


}
