/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Jazz Faye Olario
 */

package ucf.assignments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ucf.assignments.AppModel.Data;
import ucf.assignments.AppModel.TodoList;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditListController implements Initializable {
    @FXML
    private TextField Title;
    @FXML
    private TextArea Description;
    @FXML
    private DatePicker Due_Date;
    @FXML
    private ComboBox<String> Status;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Status.getItems().add("Complete");
        Status.getItems().add("Incomplete");
    }

    public void setFields(TodoList item) {
        Title.setText(item.getTitle());
        Description.setText(item.getDescription());
        Due_Date.setValue(item.getDue_Date());
        Status.setValue(item.getComplete());
    }

    public TodoList UpdateTodoItem() {
        String title = Title.getText();
        String desc= Description.getText();
        LocalDate due_date = Due_Date.getValue();
        String status = Status.getValue();
        return new TodoList(title, desc, due_date, status);
    }

    public void updateItem(TodoList oldItem, TodoList updatedItem) {
        Data.getInstance().updateTodoItem(oldItem, updatedItem);
    }

}