/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Jazz Faye Olario
 */

package ucf.assignments;

import javafx.scene.image.Image;
import ucf.assignments.AppModel.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage.getIcons().add(new Image("Icons/todo.png"));
        stage.setTitle("2Do List");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() {
        try {
            Data.getInstance().saveTodoItems();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() {
        try {
            Data.getInstance().loadTodoItems();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}