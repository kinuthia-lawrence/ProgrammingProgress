package com.larrykin.Controllers;

import com.larrykin.Models.Project;
import com.larrykin.Utils.DatabaseConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @FXML
    private Button filterButton;

    @FXML
    private HBox filterbox;

    @FXML
    private TableView<Project> projectTableView;

    @FXML
    private AnchorPane viewAnchorPane;

    @FXML
    private ComboBox<String> language;

    @FXML
    private ComboBox<String> milestone;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //initialize columns and an "Edit" button column
        initializeTableColumns();
        populateTable();
    }

    private void initializeTableColumns() {
        TableColumn<Project, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new
                PropertyValueFactory<>("projectID"));

        TableColumn<Project, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new
                PropertyValueFactory<>("date"));

        TableColumn<Project, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new
                PropertyValueFactory<>("projectName"));

        TableColumn<Project, String> projectDescriptionColumn = new TableColumn<>("Project Desc.");
        projectDescriptionColumn.setCellValueFactory(new
                PropertyValueFactory<>("projectDescription"));

        TableColumn<Project, String> futureImprovementColumn = new TableColumn<>("Future Imp.");
        futureImprovementColumn.setCellValueFactory(new
                PropertyValueFactory<>("futureImprovements"));

        TableColumn<Project, String> milestoneColumn = new TableColumn<>("Milestone");
        milestoneColumn.setCellValueFactory(new
                PropertyValueFactory<>("milestone"));

        TableColumn<Project, String> milestoneDescriptionColumn = new TableColumn<>("Milestone Desc.");
        milestoneDescriptionColumn.setCellValueFactory(new
                PropertyValueFactory<>("milestoneDescription"));

        TableColumn<Project, String> languageColumn = new TableColumn<>("Language");
        languageColumn.setCellValueFactory(new
                PropertyValueFactory<>("language"));

        TableColumn<Project, String> editColumn = new TableColumn<>("Edit");
        editColumn.setCellFactory(param -> new TableCell<Project, String>() {

            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Project project = getTableView().getItems().get(getIndex());
                    openEditProjectDialog(project);
                    System.out.println("Edit button clicked for: " + project.getProjectName());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
        projectTableView.getColumns().addAll(idColumn, dateColumn, nameColumn, projectDescriptionColumn, futureImprovementColumn, milestoneColumn, milestoneDescriptionColumn, languageColumn, editColumn);
    }

    public void openEditProjectDialog(Project project) {
        /*TODO navigate to edit*/
        System.out.println("Edit button clicked for: " + project.getProjectName());
    }

    private void populateTable() {
        //fetch data from the database and add to the table
        ObservableList<Project> projects = FXCollections.observableArrayList();

        projects.addAll(getProjectsFromDatabase());
        projectTableView.setItems(projects);

    }

    private List<Project> getProjectsFromDatabase() {
        /*TODO fetch from the database, return a list of projects*/
        DatabaseConn connectNow = new DatabaseConn();
        Connection connectDB = connectNow.getConnection();

        List<Project> projects = new ArrayList<>();

        try {
            Statement stmt = connectDB.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM projects");

            while (resultSet.next()) {
                Project project = new Project(resultSet.getObject("id"), resultSet.getString("date"), resultSet.getString("project_name"), resultSet.getString("project_description"), resultSet.getString("future_improvements"), resultSet.getString("milestone"), resultSet.getString("milestone_description"), resultSet.getString("language"));

                project.setProjectID(resultSet.getString("id"));
                project.setDate(resultSet.getString("date"));
                project.setProjectName(resultSet.getString("project_name"));
                project.setProjectDescription(resultSet.getString("project_description"));
                project.setFutureImprovements(resultSet.getString("future_improvements"));
                project.setMilestone(resultSet.getString("milestone"));
                project.setMilestoneDescription(resultSet.getString("milestone_description"));
                project.setLanguage(resultSet.getString("language"));

                projects.add(project);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
        }

        return projects;
    }
}
