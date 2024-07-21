package com.larrykin.Controllers;

import com.larrykin.Models.Project;
import com.larrykin.Models.Todos;
import com.larrykin.Utils.DatabaseConn;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TodoController implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button saveButton;

    @FXML
    private TextField titleTextField;

    @FXML
    private AnchorPane todoAnchorPane;

    @FXML
    private TableView<Todos> todoTableView;

    //? Database connection
    DatabaseConn connectNow = new DatabaseConn();
    Connection connectDB = connectNow.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();

        saveButton.setOnAction(event -> {

        });
    }

    private void initializeTableColumns() {
        //? Create the columns
        TableColumn<Todos, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new
                PropertyValueFactory<>("todoID"));

        TableColumn<Todos, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new
                PropertyValueFactory<>("date"));

        TableColumn<Todos, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new
                PropertyValueFactory<>("title"));

        TableColumn<Todos, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new
                PropertyValueFactory<>("description"));


        TableColumn<Todos, String> editColumn = new TableColumn<>("Edit");
        editColumn.setCellFactory(param -> new TableCell<Todos, String>() {

            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Todos todo = getTableView().getItems().get(getIndex());
                    editTodo(todo);
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

        TableColumn<Todos, String> doneColumn = new TableColumn<>("Done");
        doneColumn.setCellFactory(param -> new TableCell<Todos, String>() {

            private final Button markDone = new Button("Done");

            {
                markDone.setOnAction(event -> {
                    Todos todo = getTableView().getItems().get(getIndex());
                    markAsDone(todo);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(markDone);
                }
            }
        });


        todoTableView.getColumns().addAll(idColumn, dateColumn, titleColumn, descriptionColumn, editColumn , doneColumn);

        //? Populate the table with data
        populateTable();
    }

    private void editTodo(Todos todo) {
        /*TODO edit todo*/
        System.out.println("Edit todo: " + todo);
    }
    private void markAsDone(Todos todo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mark Todo as Done");
        alert.setHeaderText("Are you sure you want to mark this todo as done? and Delete it?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteTodoFromDatabase(todo);
            }else if (response == ButtonType.CANCEL){
                alert.close();
            }
        });
    }
  private void deleteTodoFromDatabase(Todos todo) {
    try {
        // Assuming todoID is of type Object and needs to be cast to the appropriate type, e.g., Integer or String
        String deleteQuery = "DELETE FROM todos WHERE id = ?";
        PreparedStatement pstmt = connectDB.prepareStatement(deleteQuery);

        // Set the todoID parameter; casting may vary based on your ID type
        pstmt.setObject(1, todo.getTodoID()); // Ensure getTodoID() returns the correct type or is cast appropriately

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Todo marked as done and deleted successfully.");
            // Optionally, refresh the table view here to reflect the deletion
            populateTable();
        } else {
            System.out.println("Todo could not be found or deleted.");
        }
    } catch (Exception e) {
        System.out.println("Error deleting todo: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void populateTable() {
        /*TODO populate table*/
        //fetch data from the database and add to the table
        ObservableList<Todos> todos = FXCollections.observableArrayList();

        todos.addAll(getTodosFromDatabase());
        todoTableView.setItems(todos);

    }

    private List<Todos> getTodosFromDatabase() {
        List<Todos> todos = new ArrayList<>();

        try{
            Statement statement = connectDB.createStatement();
            String query = "SELECT * FROM todos";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                while (resultSet.next()) {
                    Todos todo = new Todos(
                            new SimpleObjectProperty<>(resultSet.getObject("id")),
                            new SimpleStringProperty(resultSet.getString("date")),
                            new SimpleStringProperty(resultSet.getString("title")),
                            new SimpleStringProperty(resultSet.getString("description"))
                    );

                    todos.add(todo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

        return todos;
    }
}
