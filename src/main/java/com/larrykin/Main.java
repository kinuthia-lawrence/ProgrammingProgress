package com.larrykin;

import com.larrykin.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Model.getInstance().getViewFactory().showLogin();
    }
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

}