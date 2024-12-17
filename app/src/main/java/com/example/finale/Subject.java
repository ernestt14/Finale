package com.example.finale;

public class Subject {
    private String name;
    private int credits;
    private String className;
    private String schedule;
    private boolean isEnrolled;

    // Constructor
    public Subject(String name, int credits, String className, String schedule) {
        this.name = name;
        this.credits = credits;
        this.className = className;
        this.schedule = schedule;
        this.isEnrolled = false; // Default value
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getClassName() {
        return className;
    }

    public String getSchedule() {
        return schedule;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.isEnrolled = enrolled;
    }
}
