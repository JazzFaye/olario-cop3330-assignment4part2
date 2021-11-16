package ucf.assignments.AppModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Data {

    private static final Data instance = new Data();
    private static final String File_Name = "2DoList.txt";
    private ObservableList<TodoList> todoLists;
    private final DateTimeFormatter Date_Format;

    public static Data getInstance() {
        return instance;
    }

    public Data() {
        Date_Format = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    }

    public ObservableList<TodoList> getTodoItems() {
        return todoLists;
    }

    public void addTodoItem(TodoList item) {
        todoLists.add(item);
    }

    public void deleteTodoItem(TodoList item) {
        todoLists.remove(item);
    }
    public void updateTodoItem(TodoList oldItem, TodoList updatedItem) {
        int oldItemIndex = todoLists.indexOf(oldItem);
        todoLists.set(oldItemIndex, updatedItem);
    }

    public void loadTodoItems() throws IOException {

        todoLists = FXCollections.observableArrayList();
        Path path = Paths.get(File_Name);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String input;
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String title = itemPieces[0];
                String description = itemPieces[1];
                String dateString = itemPieces[2];
                String status = itemPieces[3];

                LocalDate date = LocalDate.parse(dateString, Date_Format);
                TodoList todoList = new TodoList(title, description, date,status);
                todoLists.add(todoList);
            }
        }
    }

    public void saveTodoItems() throws IOException {

        Path path = Paths.get(File_Name);
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (TodoList item : todoLists) {
                bw.write(String.format("%s\t%s\t%s\t%s",
                        item.getTitle(),
                        item.getDescription(),
                        item.getDue_Date().format(Date_Format),
                        item.getComplete()));
                bw.newLine();
            }
        }
    }
}