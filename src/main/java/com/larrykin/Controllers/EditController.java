package com.larrykin.Controllers;

import com.larrykin.Models.Project;
import com.larrykin.Utils.ComboBoxUtils;
import com.larrykin.Utils.DatabaseConn;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            updateProject();
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
    private void searchProject() {
    }

    //? Method to perfonm the update
    public void updateProject() {
    }
}
