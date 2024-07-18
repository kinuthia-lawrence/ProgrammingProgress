package com.larrykin.Controllers;

import com.larrykin.Utils.ComboBoxUtils;
import com.larrykin.Utils.DatabaseConn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
        // Populate the comboBoxes using the utility class
        ComboBoxUtils.populateLanguageComboBox(languageComboBox);
        ComboBoxUtils.populateMilestoneComboBox(milestoneComboBox);

        // Set default selections
        languageComboBox.setValue("Java");
        milestoneComboBox.setValue("TODO");

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

        //get the values from the fields
        String projectName = projectNameTextField.getText();
        String projectDescription = projectDescriptionTextArea.getText();
        String milestone = milestoneComboBox.getValue();
        String milestoneDesc = milestoneDescription.getText();
        String futureImprovement = futureImprovementTextArea.getText();
        String language = languageComboBox.getValue();
        String date = datePicker.getValue().toString();

        //query to insert the values into the database
        String insertProject = "INSERT INTO projects (project_name, project_description, milestone, milestone_description, future_improvements, language, date) VALUES (?,?,?,?,?,?,?)";

        try{
            //? create a prepared statement(used to execute the query)
            PreparedStatement preparedStatement = connectDB.prepareStatement(insertProject);

            //? set the values to the prepared statement(used to execute the query by replacing the ? in the query with the values)
            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, projectDescription);
            preparedStatement.setString(3, milestone);
            preparedStatement.setString(4, milestoneDesc);
            preparedStatement.setString(5, futureImprovement);
            preparedStatement.setString(6, language);
            preparedStatement.setString(7, date);

            int rowsAffected = preparedStatement.executeUpdate(); //? this will execute the query and return the number of rows affected
            if(rowsAffected >0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Project Added");
                alert.setContentText( projectName + " added successfully!");

                //? Timeline to close the alert after 2 seconds
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), ev -> alert.close()));
                timeline.setCycleCount(1);
                timeline.play();
                alert.showAndWait();

                //? clear fields after adding project
                projectNameTextField.clear();
                projectDescriptionTextArea.clear();
                milestoneDescription.clear();
                futureImprovementTextArea.clear();
                datePicker.getEditor().clear();
                languageComboBox.getSelectionModel().clearSelection();
                milestoneComboBox.getSelectionModel().clearSelection();

                /*TODO update all the view lists*/

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding " + projectName);
                alert.setContentText("An error occurred while adding the "+ projectName +". Please try again.");
                alert.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
        }

    }


}
