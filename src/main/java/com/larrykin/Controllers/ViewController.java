package com.larrykin.Controllers;

import com.larrykin.Models.Model;
import com.larrykin.Models.Project;
import com.larrykin.Utils.ComboBoxUtils;
import com.larrykin.Utils.DatabaseConn;
import com.larrykin.Views.DashboardOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    @FXML
    private Button refreshButton;

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

    //? Database connection
    DatabaseConn connectNow = new DatabaseConn();

    private EditController editController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //initialize columns and an "Edit" button column
        initializeTableColumns();


        filterButton.setOnAction(event -> {
            if (language.getValue() == null && milestone.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please select a language or a milestone");
                alert.showAndWait();
                return;
            }
            populateFilteredTable();
        });
        refreshButton.setOnAction(event -> {
            refreshTable();
        });
    }

    public void manualInitialize() {
        initializeTableColumns();
        // Any other initialization logic you want to manually trigger
    }

    private void initializeTableColumns() {


        //? Populate the comboBoxes using the utility class
        ComboBoxUtils.populateLanguageComboBox(language);
        ComboBoxUtils.populateMilestoneComboBox(milestone);

        //? Initialize the columns of the table
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

        //? Populate the table with data
        populateTable();
    }


    public void openEditProjectDialog(Project project) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit Project");
        alert.setHeaderText("Are you sure you want to edit " + project.getProjectName() + " in Edit Page?");
        alert.setContentText("You will be redirected to the Edit Page");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                //! Create EditController instance using FXMLLoader
                try {
                    // Step 1: Create an FXMLLoader instance and set the location of the FXML file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Edit.fxml"));
                    // Step 2: Load the FXML file to initialize the ViewController and its components
                    Parent root = loader.load(); // This line automatically creates an instance of ViewController , initializes it and injects @FXML fields
                    // Step 3: Get the editController instance from the loader if you need to use it
                    editController = loader.getController();
                } catch (IOException e) {
                    System.out.println("Error in loading Edit.fxml in ViewController: " + e.getMessage());
                    e.printStackTrace();
                }
                //? Set the selected item in the dashboard to EDIT
                Model.getInstance().getViewFactory().getDashboardSelectedItem().set(DashboardOptions.EDIT);
                editController.updateProject(project);
            } else if (response == ButtonType.CANCEL) {
                alert.close();
            }
        });

    }

    private void populateTable() {
        //fetch data from the database and add to the table
        ObservableList<Project> projects = FXCollections.observableArrayList();

        projects.addAll(getProjectsFromDatabase());
        projectTableView.setItems(projects);

    }

    private List<Project> getProjectsFromDatabase() {
        List<Project> projects = new ArrayList<>();

        try {
            Connection connectDB = connectNow.getConnection(); //! try with resources closes the connection automatically

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

    public void refreshTable() {
        language.setValue(null);
        milestone.setValue(null);

        projectTableView.getItems().clear();
        projectTableView.getItems().addAll(getProjectsFromDatabase());
    }

    //? Filter the table based on the selected language and milestone
    public void populateFilteredTable() {
        projectTableView.getItems().clear();
        projectTableView.getItems().addAll(filteredProjects());
    }

    private List<Project> filteredProjects() {
        String selectedLanguage = language.getValue();
        String selectedMilestone = milestone.getValue();


        List<Project> projects = new ArrayList<>();

        try {
            Connection connectDB = connectNow.getConnection(); //! try with resources closes the connection automatically

            Statement stmt = connectDB.createStatement();
            ResultSet resultSet = null;

            if (selectedLanguage != null && selectedMilestone != null) {
                resultSet = stmt.executeQuery("SELECT * FROM projects WHERE language = '" + selectedLanguage + "' AND milestone = '" + selectedMilestone + "'");
            } else if (selectedLanguage != null) {
                resultSet = stmt.executeQuery("SELECT * FROM projects WHERE language = '" + selectedLanguage + "'");
            } else if (selectedMilestone != null) {
                resultSet = stmt.executeQuery("SELECT * FROM projects WHERE milestone = '" + selectedMilestone + "'");
            }

            //clear the table
            projectTableView.getItems().clear();

            //add the filtered projects to the table
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

    private List<Project> searchInProjects(String searchLanguage, String searchText) {

        List<Project> projects = new ArrayList<>();

        //? If the search text is empty, show all projects, if not, show the projects that match the search text
        try {
            Connection connectDB = connectNow.getConnection(); //! try with resources closes the connection automatically

            Statement stmt = connectDB.createStatement();
            ResultSet resultSet = null;

            if (searchText.isBlank() && searchLanguage == null) {
                System.out.println("Search text is empty");
                populateTable();
            } else if (searchLanguage != null) {
                resultSet = stmt.executeQuery("SELECT * FROM projects WHERE language = '" + searchLanguage + "'");
            } else if (searchText != null) {
                try {
                    String query = "SELECT * FROM projects WHERE project_name LIKE ? OR project_description LIKE ?";
                    PreparedStatement pstmt = connectDB.prepareStatement(query);
                    pstmt.setString(1, "%" + searchText + "%");
                    pstmt.setString(2, "%" + searchText + "%");
                    resultSet = pstmt.executeQuery();
                } catch (Exception e) {
                    System.out.println("Error in searchText != null try: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (searchLanguage != null && searchText != null) {
                StringBuilder queryBuilder = new StringBuilder("SELECT * FROM projects WHERE ");
                ArrayList<String> conditions = new ArrayList<>();

                if (searchLanguage != null && !searchLanguage.isEmpty()) {
                    conditions.add("language = ?");
                }

                if (searchText != null && !searchText.isBlank()) {
                    conditions.add("(project_name LIKE ? OR project_description LIKE ?)");
                }

                queryBuilder.append(String.join(" AND ", conditions));

                try (PreparedStatement pstmt = connectDB.prepareStatement(queryBuilder.toString())) {
                    int index = 1;
                    if (searchLanguage != null && !searchLanguage.isEmpty()) {
                        pstmt.setString(index++, searchLanguage);
                    }
                    if (searchText != null && !searchText.isEmpty()) {
                        pstmt.setString(index++, "%" + searchText + "%");
                        pstmt.setString(index, "%" + searchText + "%");
                    }

                    resultSet = pstmt.executeQuery();
                } catch (Exception e) {
                    System.out.println("Error in searchLanguage != null && searchText != null try: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            //clear the table
            projectTableView.getItems().clear();

            //add the filtered projects to the table
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
            System.out.println("Error in searchInProjects try: " + e.getMessage());
            e.printStackTrace();
        }
        return projects;
    }

    //? Filter the table based on the selected language and text
    public void populateSearchedData(String languageSelected, String textSelected) {
        projectTableView.getItems().clear();
        projectTableView.getItems().addAll(searchInProjects(languageSelected, textSelected));
    }
}
