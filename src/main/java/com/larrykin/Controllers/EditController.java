package com.larrykin.Controllers;

import com.larrykin.Models.Project;
import com.larrykin.Utils.ComboBoxUtils;
import com.larrykin.Utils.DatabaseConn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditController implements Initializable {

    @FXML
    private Button Search;

    @FXML
    private AnchorPane addAnchorPane;

    @FXML
    private ComboBox<String> chooseProjectComboBox;

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
    private Button updateButton;

    //? Database connection
    DatabaseConn connectNow = new DatabaseConn();

    //? Keep track of the current project
    private Project currentProject = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ComboBoxUtils.populateLanguageComboBox(languageComboBox);
        ComboBoxUtils.populateLanguageComboBox(milestoneComboBox);
        List<String> projectNames = fetchAllProjectNames();
        chooseProjectComboBox.setItems(FXCollections.observableArrayList(projectNames));

        Search.setOnAction(event -> {
            searchProject();
        });
        updateButton.setOnAction(event -> {
            getProject();
        });

    }


    //? Fetch the project names.
    public List<String> fetchAllProjectNames() {
        List<String> projectNames = new ArrayList<>();
        String query = "SELECT project_name FROM projects";
        try (Connection conn = connectNow.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                projectNames.add(rs.getString("project_name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching project names: " + e.getMessage());
            e.printStackTrace();
        }
        return projectNames;
    }

    //? Method to search for a project
    private Project searchProject() {
        String selectedProjectName = chooseProjectComboBox.getValue();
        if (selectedProjectName == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a project");
            alert.showAndWait();
            return null;
        }

        Project foundProject = null;
        String query = "SELECT * FROM projects WHERE project_name = ?";
        try (Connection conn = connectNow.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedProjectName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    foundProject = new Project();
                }
            }
            //? Enable the update button
            updateButton.setDisable(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundProject;
    }

    private Project getProject() {
        //? get the return value or the searchProject method
        Project project = searchProject();
        updateProject(project);
        return null;
    }

    //? Method to perfonm the update
    public void updateProject(Project project) {
        if (project != null) {
            try{

            //?populate the fields with the project details
            datePicker.setValue(LocalDate.parse(project.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            chooseProjectComboBox.setValue(project.getProjectName());
            languageComboBox.setValue(project.getLanguage());
            projectDescriptionTextArea.setText(project.getProjectDescription());
            futureImprovementTextArea.setText(project.getFutureImprovements());
            milestoneComboBox.setValue(project.getMilestone());
            milestoneComboBox.setValue(project.getMilestoneDescription());
            milestoneDescription.setText(project.getMilestoneDescription());

            //? Keep track of the current project
            currentProject = project;

            //? save the updated project to the database
            updateProjectInDatabase();

            }catch (Exception e){
                System.out.println("Error updating nodes values: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void updateProjectInDatabase() {
        if (currentProject == null) {
            return;
        }
        try {
            Connection connectDB = connectNow.getConnection(); //! try with resources closes the connection automatically
            String query = "UPDATE projects SET project_name = ?, date = ?, language = ?, project_description = ?, future_improvements = ?, milestone = ?, milestone_description = ? WHERE project_name = ?";

            PreparedStatement pstmt = connectDB.prepareStatement(query);

            pstmt.setString(1, chooseProjectComboBox.getValue());
            pstmt.setString(2, datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            pstmt.setString(3, languageComboBox.getValue());
            pstmt.setString(4, projectDescriptionTextArea.getText());
            pstmt.setString(5, futureImprovementTextArea.getText());
            pstmt.setString(6, milestoneComboBox.getValue());
            pstmt.setString(7, milestoneDescription.getText());
            pstmt.setString(8, currentProject.getProjectName());

            int rowAffected = pstmt.executeUpdate();
            pstmt.close();
            if(rowAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Project updated successfully");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event-> alert.close()));
                timeline.setCycleCount(1);
                timeline.play();

                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error updating project");
                alert.showAndWait();

            }
        } catch (Exception e) {
            System.out.println("Error in updating project: " + e.getMessage());
            e.printStackTrace();
        } finally {
            currentProject = null;
        }
    }

}
