package com.larrykin.Models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Todos {
    private final SimpleObjectProperty<Object> todoID; // Keep as Object if ID can vary in type
    private final SimpleStringProperty date;
    private final SimpleStringProperty title;
    private final SimpleStringProperty description;

    public Todos(SimpleObjectProperty<Object> todoID, SimpleStringProperty date, SimpleStringProperty title, SimpleStringProperty description) {
        this.todoID = todoID;
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public Object getTodoID() {
        return todoID.get();
    }

    public SimpleObjectProperty<Object> todoIDProperty() {
        return todoID;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    // Setters
    public void setTodoID(Object projectID) {
        this.todoID.set(projectID);
    }
    public void setDate(String date) {
        this.date.set(date);
    }
    public void setTitle(String title) {
        this.title.set(title);
    }
    public void setDescription(String description) {
        this.description.set(description);
    }
}
