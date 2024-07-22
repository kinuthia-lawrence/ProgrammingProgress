package com.larrykin.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewFactory {

    private final ObjectProperty dashboardSelectedItem;
    private final ObjectProperty accountSettingsSelectedItem;
    //? dashboard anchor panes
    private AnchorPane addAnchorPane, editAnchorPane, viewAnchorPane, projectAnchorPane, todoAnchorPane;
    //? account settings anchor panes
    private AnchorPane helpAnchorPane, contactDeveloperAnchorPane, createAccountAnchorPane, resetPasswordAnchorPane;



    //?constructor


    public ViewFactory() {
        this.dashboardSelectedItem = new SimpleObjectProperty();
        this.accountSettingsSelectedItem = new SimpleObjectProperty();
    }

    //!  DASHBOARD SECTION
    //?getting the DASHBOARD selected item
    public ObjectProperty<DashboardOptions> getDashboardSelectedItem() {
        return dashboardSelectedItem;
    }

    //! getting DASHBOARD resources
    public AnchorPane getAddAnchorPane() {
        if (addAnchorPane == null) {
            try {
                addAnchorPane = new FXMLLoader(getClass().getResource("/FXML/Add.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return addAnchorPane;
    }

    public AnchorPane getEditAnchorPane() {
        if (editAnchorPane == null) {
            try {
                editAnchorPane = new FXMLLoader(getClass().getResource("/FXML/Edit.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return editAnchorPane;
    }

    public AnchorPane getViewAnchorPane() {
        if (viewAnchorPane == null) {
            try {
                viewAnchorPane = new FXMLLoader(getClass().getResource("/FXML/View.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return viewAnchorPane;
    }

    public AnchorPane getProjectAnchorPane() {
        if (projectAnchorPane == null) {
            try {
                projectAnchorPane = new FXMLLoader(getClass().getResource("/FXML/project.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return projectAnchorPane;
    }

    public AnchorPane getTodoAnchorPane() {
        if (todoAnchorPane == null) {
            try {
                todoAnchorPane = new FXMLLoader(getClass().getResource("/FXML/Todo.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return todoAnchorPane;
    }

    //? Show login
    public void showLogin() {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
        Scene loginScene = null;
        try {
            loginScene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
        }
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Programming Progress Tracker");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    //! ACCOUNT SETTINGS SECTION
    //?getting the ACCOUNT SETTINGS selected item
    public ObjectProperty<AccountSettingOptions> getAccountSettingsSelectedItem() {
        return accountSettingsSelectedItem;
    }

    //! getting ACCOUNT SETTINGS resources
    public AnchorPane getHelpAnchorPane() {
        if (helpAnchorPane == null) {
            try {
                helpAnchorPane = new FXMLLoader(getClass().getResource("/FXML/help.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return helpAnchorPane;
    }
    public AnchorPane getContactDeveloperAnchorPane() {
        if (contactDeveloperAnchorPane == null) {
            try {
                contactDeveloperAnchorPane = new FXMLLoader(getClass().getResource("/FXML/contactDeveloper.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return contactDeveloperAnchorPane;
    }
    public AnchorPane getCreateAccountAnchorPane() {
        if (createAccountAnchorPane == null) {
            try {
                createAccountAnchorPane = new FXMLLoader(getClass().getResource("/FXML/createAccount.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return createAccountAnchorPane;
    }
    public AnchorPane getResetPasswordAnchorPane() {
        if (resetPasswordAnchorPane == null) {
            try {
                resetPasswordAnchorPane = new FXMLLoader(getClass().getResource("/FXML/passwordReset.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getCause().getMessage());
            }
        }
        return resetPasswordAnchorPane;
    }
}
