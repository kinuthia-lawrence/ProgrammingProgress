package com.larrykin.Models;

import com.larrykin.Views.ViewFactory;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;

    public Model() {
        this.databaseDriver = new DatabaseDriver();
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance(){
        if (model == null){
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    public DatabaseDriver getDatabaseDriver(){
        return databaseDriver;
    }
}
