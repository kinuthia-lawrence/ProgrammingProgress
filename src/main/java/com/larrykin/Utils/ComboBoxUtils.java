package com.larrykin.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxUtils {

    public static void populateLanguageComboBox(ComboBox<String> comboBox) {
        ObservableList<String> languages = FXCollections.observableArrayList(
                 "Java", "React", "PHP", "Kotlin", "Flutter", "Python", "JavaScript"
        );
        comboBox.setItems(languages);
    }

    public static void populateMilestoneComboBox(ComboBox<String> comboBox) {
        ObservableList<String> milestones = FXCollections.observableArrayList(
                "TODO", "UX/UI", "SETUP ENVIRONMENT", "IN PROGRESS", "DONE"
        );
        comboBox.setItems(milestones);
    }
}