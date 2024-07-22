package com.larrykin.Controllers;

import com.larrykin.Models.Model;
import com.larrykin.Views.AccountSettingOptions;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class AccountSettings implements Initializable {

    @FXML
    private Button HelpButton;

    @FXML
    private BorderPane accountSettingBorderPane;

    @FXML
    private Button contactDeveloperButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button resetPasswordButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            addListeners();

            accountSettingBorderPane.setCenter(Model.getInstance().getViewFactory().getResetPasswordAnchorPane());
            Model.getInstance().getViewFactory().getAccountSettingsSelectedItem().addListener((observableValue, oldVal, newVal) -> {
                switch (newVal) {
                    case CREATE_ACCOUNT:
                        accountSettingBorderPane.setCenter(Model.getInstance().getViewFactory().getCreateAccountAnchorPane());
                        break;
                    case PASSWORD_RESET:
                        accountSettingBorderPane.setCenter(Model.getInstance().getViewFactory().getResetPasswordAnchorPane());
                        break;
                    case CONTACT_DEVELOPER:
                        accountSettingBorderPane.setCenter(Model.getInstance().getViewFactory().getContactDeveloperAnchorPane());
                        break;
                    case HELP:
                        accountSettingBorderPane.setCenter(Model.getInstance().getViewFactory().getHelpAnchorPane());
                        break;
                }
            });
    }

    //? add listeners to the buttons
    private void addListeners() {
        createAccountButton.setOnAction(event -> Model.getInstance().getViewFactory().getAccountSettingsSelectedItem().set(AccountSettingOptions.CREATE_ACCOUNT));
        resetPasswordButton.setOnAction(event -> Model.getInstance().getViewFactory().getAccountSettingsSelectedItem().set(AccountSettingOptions.PASSWORD_RESET));
        contactDeveloperButton.setOnAction(event -> contactDeveloperButtonClicked());
        HelpButton.setOnAction(event -> helpButtonClicked());
    }

    private void contactDeveloperButtonClicked() {
       Model.getInstance().getViewFactory().getAccountSettingsSelectedItem().set(AccountSettingOptions.CONTACT_DEVELOPER);
    }

    private void helpButtonClicked() {
       Model.getInstance().getViewFactory().getAccountSettingsSelectedItem().set(AccountSettingOptions.HELP);
    }
}
