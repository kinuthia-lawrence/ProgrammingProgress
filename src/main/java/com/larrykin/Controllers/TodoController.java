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
        /*TODO mark done*/
        System.out.println("Mark as done: " + todo);
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
