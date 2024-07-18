package com.larrykin.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddController {

    @FXML
    private AnchorPane addAnchorPane;

    @FXML
    private DatePicker datePicker;


    @FXML
    private TextArea futureImprovementTextArea;

    @FXML
    private ComboBox<?> languageComboBox;

    @FXML
    private ComboBox<?> milestoneComboBox;

    @FXML
    private TextArea milestoneDescription;

    @FXML
    private TextArea projectDescriptionTextArea;

    @FXML
    private TextField projectNameTextField;

    @FXML
    private Button saveButton;



}
