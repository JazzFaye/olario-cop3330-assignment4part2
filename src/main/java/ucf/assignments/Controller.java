/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Jazz Faye Olario
 */

package ucf.assignments;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import ucf.assignments.AppModel.Data;
import ucf.assignments.AppModel.TodoList;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller{
    @FXML
    private ListView<TodoList> todoListView;
    @FXML
    private TextArea itemDetailTextArea;
    @FXML
    private Label DateAndStatus;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private Button Delete_List;
    @FXML
    private Button Edit_List;
    @FXML
    private ToggleButton Filter_List;

    private FilteredList<TodoList> Filtered_List;
    private Predicate<TodoList> AllItems;
    private Predicate<TodoList> IncompleteList;
    private Predicate<TodoList> CompleteList;

    public void initialize() {

        ContextMenu();
        Setup_EachButton();
        Setup_FullView();
        Setup_Predicates();

        Filtered_List = new FilteredList<>(Data.getInstance().getTodoItems(), AllItems);
        Filtered_List = new FilteredList<>(Data.getInstance().getTodoItems(), IncompleteList);
        Filtered_List = new FilteredList<>(Data.getInstance().getTodoItems(), CompleteList);

        SortedList<TodoList> sortedList = new SortedList<>(Filtered_List, Comparator.comparing(TodoList::getComplete));

        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<TodoList> call(ListView<TodoList> todoItemListView) {
                ListCell<TodoList> Cell = new ListCell<>() {
                    @Override
                    protected void updateItem(TodoList item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                        } else {
                            this.setText(item.getTitle());
                            if (item.getComplete().equals("Incomplete")) {
                                this.setTextFill(Color.RED);
                            } else {
                                this.setTextFill(Color.FORESTGREEN);
                            }
                        }
                    }
                };
                Cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                Cell.setContextMenu(null);
                            } else {
                                Cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return Cell;
            }
        });
    }

    @FXML
    public void AddNewList() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Todo Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddNewList.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Dialog could not load.");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        AddListController controller = fxmlLoader.getController();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TodoList newItem = controller.processResult();
            if (newItem == null) {
                Error("Empty fields", "Title and Description cannot be empty.");
            } else {
                todoListView.getSelectionModel().select(newItem);
            }
        }
    }

    public void ContextMenu() {

        listContextMenu = new ContextMenu();
        MenuItem DeleteMenuItem = new MenuItem("Delete");
        MenuItem EditMenuItem = new MenuItem("Edit");

        DeleteMenuItem.setOnAction(actionEvent -> {
            TodoList item = todoListView.getSelectionModel().getSelectedItem();
            Delete_Item(item);
        });

        EditMenuItem.setOnAction(actionEvent -> {
            TodoList item = todoListView.getSelectionModel().getSelectedItem();
            EditList(item);
        });
        listContextMenu.getItems().addAll(EditMenuItem, DeleteMenuItem);
    }
    public void Delete_Item(TodoList item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Data.getInstance().deleteTodoItem(item);
            itemDetailTextArea.clear();
            todoListView.refresh();
        }
    }

    @FXML
    public void EditList(TodoList oldItem) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Todo Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("EditList.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Dialog Could not load.");
            e.printStackTrace();
            return;
        }
        EditListController controller = fxmlLoader.getController();
        controller.setFields(oldItem);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TodoList updatedItem = controller.UpdateTodoItem();
            controller.updateItem(oldItem, updatedItem);
        }
        todoListView.refresh();
    }

    public void Setup_EachButton() {
        Delete_List.setOnAction(actionEvent -> {
            TodoList item = todoListView.getSelectionModel().getSelectedItem();
            if (item == null) {
                Error("Error", "No List selected. Please select a list to Delete.");
            } else {
                Delete_Item(item);
            }
        });
        Edit_List.setOnAction(actionEvent -> {
            TodoList item = todoListView.getSelectionModel().getSelectedItem();
            if (item == null) {
                Error("Error", "No List selected. Please select a list to Edit.");
            } else {
                EditList(item);
            }
        });
    }

    public void Setup_FullView() {
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoList>() {
            @Override
            public void changed(ObservableValue<? extends TodoList> observable, TodoList oldValue, TodoList newValue) {
                if (newValue != null) {
                    TodoList item = todoListView.getSelectionModel().getSelectedItem();
                    itemDetailTextArea.setText(item.getDescription());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
                    DateAndStatus.setText("\t\t\t\tDue: " + df.format(item.getDue_Date()) + "\tStatus: " + (item.getComplete()));
                }else{
                    itemDetailTextArea.setText("");
                    DateAndStatus.setText("\t\t\t\tDue: " + "\tStatus: ");
                }
            }
        });
    }

    public void Setup_Predicates() {
        AllItems = new Predicate<TodoList>() {
            @Override
            public boolean test(TodoList todoList) {
                return true;
            }
        };
        IncompleteList = item -> item.getComplete().equals("Incomplete");
        CompleteList = item -> item.getComplete().equals("Complete");
    }

    public void Error(String error, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    public void Close() {
        Platform.exit();
    }

    @FXML
    public void Handle_Key(KeyEvent keyEvent) {
        TodoList selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
               Delete_Item(selectedItem);
            }
        }
    }

    @FXML
    public void Filter_List_Pressed() {
        TodoList selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (Filter_List.isSelected()) {
            Filtered_List.setPredicate(IncompleteList);
            if (Filtered_List.isEmpty()) {
                itemDetailTextArea.clear();
                DateAndStatus.setText("");
            } else if (Filtered_List.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            }
        } else {
            Filtered_List.setPredicate(CompleteList);
            todoListView.getSelectionModel().select(selectedItem);
        }
    }
    @FXML
    public void Show_All_Pressed() {
        TodoList selectedItem = todoListView.getSelectionModel().getSelectedItem();
        Filtered_List.setPredicate(AllItems);
        todoListView.getSelectionModel().select(selectedItem);
    }


}
