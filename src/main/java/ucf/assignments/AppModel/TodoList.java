/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Jazz Faye Olario
 */

package ucf.assignments.AppModel;


import java.time.LocalDate;

public class TodoList {

    private String title;
    private String description;
    private LocalDate Due_Date;
    private String complete;

    public TodoList(String title, String details, LocalDate Due_Date, String complete) {
        this.title = title;
        this.description = details;
        this.Due_Date = Due_Date;
        this.complete = complete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String details) {
        this.description = details;
    }

    public LocalDate getDue_Date() {
        return Due_Date;
    }

    public void setDue_Date(LocalDate due_Date) {
        Due_Date = due_Date;
    }


    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return title;
    }
}