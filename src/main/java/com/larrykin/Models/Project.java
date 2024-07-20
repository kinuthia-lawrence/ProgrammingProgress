package com.larrykin.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Project {
    private final SimpleObjectProperty<Object> projectID; // Keep as Object if ID can vary in type
    private final SimpleStringProperty date;
    private final SimpleStringProperty projectName;
    private final SimpleStringProperty projectDescription;
    private final SimpleStringProperty futureImprovements;
    private final SimpleStringProperty milestone;
    private final SimpleStringProperty milestoneDescription;
    private final SimpleStringProperty language;

    public Project(Object projectID, String date, String projectName, String projectDescription, String futureImprovements, String milestone, String milestoneDescription, String language) {
        this.projectID = new SimpleObjectProperty<>(projectID);
        this.date = new SimpleStringProperty(date);
        this.projectName = new SimpleStringProperty(projectName);
        this.projectDescription = new SimpleStringProperty(projectDescription);
        this.futureImprovements = new SimpleStringProperty(futureImprovements);
        this.milestone = new SimpleStringProperty(milestone);
        this.milestoneDescription = new SimpleStringProperty(milestoneDescription);
        this.language = new SimpleStringProperty(language);
    }

    // Getters and Property Accessors

    public Object getProjectID() {
        return projectID.get();
    }

    public SimpleObjectProperty<Object> projectIDProperty() {
        return projectID;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getProjectName() {
        return projectName.get();
    }

    public SimpleStringProperty projectNameProperty() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription.get();
    }

    public SimpleStringProperty projectDescriptionProperty() {
        return projectDescription;
    }

    public String getFutureImprovements() {
        return futureImprovements.get();
    }

    public SimpleStringProperty futureImprovementsProperty() {
        return futureImprovements;
    }

    public String getMilestone() {
        return milestone.get();
    }

    public SimpleStringProperty milestoneProperty() {
        return milestone;
    }

    public String getMilestoneDescription() {
        return milestoneDescription.get();
    }

    public SimpleStringProperty milestoneDescriptionProperty() {
        return milestoneDescription;
    }

    public String getLanguage() {
        return language.get();
    }

    public SimpleStringProperty languageProperty() {
        return language;
    }

    //? Setters
    public void setProjectID(Object projectID) {
        this.projectID.set(projectID);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setProjectName(String projectName) {
        this.projectName.set(projectName);
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription.set(projectDescription);
    }

    public void setFutureImprovements(String futureImprovements) {
        this.futureImprovements.set(futureImprovements);
    }

    public void setMilestone(String milestone) {
        this.milestone.set(milestone);
    }

    public void setMilestoneDescription(String milestoneDescription) {
        this.milestoneDescription.set(milestoneDescription);
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }
}