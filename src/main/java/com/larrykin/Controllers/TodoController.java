package com.larrykin.Controllers;

import com.larrykin.Models.Project;
import com.larrykin.Models.Todos;
import com.larrykin.Utils.DatabaseConn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    //? Keep track of the todos being edited
    private Todos currentEditingTodo = null;

    //? Database connection
    DatabaseConn connectNow = new DatabaseConn();
    Connection connectDB = connectNow.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();

        saveButton.setOnAction(event -> {
            saveTodo();
        });
    }

    private void saveTodo() {
        if (datePicker.getValue() == null || titleTextField.getText().isEmpty() || descriptionTextArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("All fields are required.");
            alert.setContentText("Please fill in all fields.");

            alert.showAndWait();
            return;
        }
        // Save the todos to the database
        try {
            String insertQuery = "INSERT INTO todos (date, title, description) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connectDB.prepareStatement(insertQuery);

            // Assuming the date is stored in a 'yyyy-MM-dd' format in the database
            pstmt.setString(1, datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            pstmt.setString(2, titleTextField.getText());
            pstmt.setString(3, descriptionTextArea.getText());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Todo Saved");
                alert.setHeaderText("Todo saved successfully.");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> alert.close()));
                timeline.setCycleCount(1);
                timeline.play();

                alert.showAndWait();

                populateTable(); // Refresh the table view
            } else {
                System.out.println("Todo could not be saved.");
            }
        } catch (Exception e) {
            System.out.println("Error saving todo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            clearForm();
        }

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


        todoTableView.getColumns().addAll(idColumn, dateColumn, titleColumn, descriptionColumn, editColumn, doneColumn);

        //? Populate the table with data
        populateTable();
    }

    private void editTodo(Todos todo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit Todo");
        alert.setHeaderText("Are you sure you want to edit this todo?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                performEdit(todo);
            } else if (response == ButtonType.CANCEL) {
                alert.close();
            }
        });
    }



    private void performEdit(Todos todo) {
        // Set the UI components with the values from the todo
        datePicker.setValue(LocalDate.parse(todo.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        titleTextField.setText(todo.getTitle());
        descriptionTextArea.setText(todo.getDescription());

        // Keep track of the currently editing todo
        currentEditingTodo = todo;

        saveButton.setText("Update");
        // Change the saveButton action to update the todo
        saveButton.setOnAction(event -> updateTodoInDatabase());
    }

    private void updateTodoInDatabase() {
        if (currentEditingTodo == null) {
            return; // or handle error
        }

        try {
            String updateQuery = "UPDATE todos SET date = ?, title = ?, description = ? WHERE id = ?";
            PreparedStatement pstmt = connectDB.prepareStatement(updateQuery);

            // Assuming the date is stored in a 'yyyy-MM-dd' format in the database
            pstmt.setString(1, datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            pstmt.setString(2, titleTextField.getText());
            pstmt.setString(3, descriptionTextArea.getText());
            pstmt.setObject(4, currentEditingTodo.getTodoID()); // Ensure getTodoID() returns the correct type or is cast appropriately

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Todo Updated");
                alert.setHeaderText("Todo updated successfully.");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> alert.close()));
                timeline.setCycleCount(1);
                timeline.play();

                alert.showAndWait();

                populateTable(); // Refresh the table view
            } else {
                System.out.println("Todo could not be updated.");
            }
        } catch (Exception e) {
            System.out.println("Error updating todo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Reset the saveButton action to its default behavior after updating
            saveButton.setOnAction(event -> {
                saveButton.setText("Save");
                saveTodo();
            });
            // Clear the form and reset currentEditingTodo
            clearForm();
            currentEditingTodo = null;
        }
    }

    private void clearForm() {
        datePicker.setValue(null);
        titleTextField.setText("");
        descriptionTextArea.setText("");
    }

    private void markAsDone(Todos todo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mark Todo as Done");
        alert.setHeaderText("Are you sure you want to mark this todo as done? and Delete it?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteTodoFromDatabase(todo);
            } else if (response == ButtonType.CANCEL) {
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
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Todo Deleted");
                alert.setHeaderText("Todo deleted successfully.");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> alert.close()));
                timeline.setCycleCount(1);
                timeline.play();

                alert.showAndWait();
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
        //fetch data from the database and add to the table
        ObservableList<Todos> todos = FXCollections.observableArrayList();

        todos.addAll(getTodosFromDatabase());
        todoTableView.setItems(todos);

    }

    private List<Todos> getTodosFromDatabase() {
        List<Todos> todos = new ArrayList<>();

        try {
            Statement statement = connectDB.createStatement();
            String query = "SELECT * FROM todos";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

        return todos;
    }
}
