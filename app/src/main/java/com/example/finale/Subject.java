package com.example.finale;

public class Subject {

    private String name;
    private int credits;
    private String className;
    private String schedule;
    private boolean isSelected;  // To track the selection state

    public Subject() {
        // Default constructor required for Firebase
    }

    public Subject(String name, int credits, String className, String schedule) {
        this.name = name;
        this.credits = credits;
        this.className = className;
        this.schedule = schedule;
        this.isSelected = false;  // By default, not selected
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    // Getter and setter for the selection state
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
