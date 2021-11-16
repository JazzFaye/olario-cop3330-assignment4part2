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

public class AddListController implements Initializable {
    @FXML
    private TextField Title;
    @FXML
    private TextArea Description;
    @FXML
    private DatePicker Due_Date;
    @FXML
    private ComboBox<String> Status;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Due_Date.setValue(LocalDate.now());
        Status.getItems().add("Complete");
        Status.getItems().add( "Incomplete");
    }

    public TodoList processResult() {
        String title = Title.getText().trim();
        String desc = Description.getText().trim();
        LocalDate due_date = Due_Date.getValue();
        String status = Status.getValue();

        if (title.isEmpty() || desc.isEmpty()) {
            return null;
        } else {
            TodoList newItem = new TodoList(title, desc, due_date, status);
            Data.getInstance().addTodoItem(newItem);
            return newItem;
        }
    }
}