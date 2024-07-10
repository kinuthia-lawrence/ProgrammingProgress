package com.larrykin.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class EditController {

    @FXML
    private AnchorPane addAnchorPane;

    @FXML
    private ComboBox<?> chooseProjectComboBox;

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
    private Button updateButton;

}
