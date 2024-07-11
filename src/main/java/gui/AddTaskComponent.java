package gui;

import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AddTaskComponent {
    private VBox addTaskArea;
    private TextField taskNameField;
    private String selectedColor = "#FFFFFF";
    private TaskController taskController;

    public AddTaskComponent(TaskController taskController) {
        this.taskController = taskController;
        initializeAddTaskArea();
    }

    private void initializeAddTaskArea() {
        addTaskArea = new VBox(10);
        addTaskArea.setPadding(new Insets(10, 0, 0, 0));  // 10 pixels of top padding
        taskNameField = new TextField();
        
        taskNameField.setPromptText("Enter Task");
        taskNameField.setPrefHeight(40);
        taskNameField.setMaxSize(550, Double.MAX_VALUE);
        HBox colorButtons = createColorButtons();
        Button addTaskButton = createAddTaskButton();
        addTaskArea.getChildren().addAll(taskNameField, colorButtons, addTaskButton);
        addTaskArea.setAlignment(Pos.TOP_CENTER);
    }

    private HBox createColorButtons() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        String[] colors = {"#f44336", "#f288bc", "#f1c232", "#f09b41", "#7ada8e", "#7adaca", "#76bcec", "#baa0ff", "#ffffff"};
        for (String color : colors) {
            Button colorButton = new Button();
            colorButton.setStyle("-fx-background-color: " + color + "; -fx-min-width: 20px; -fx-min-height: 20px;");
            colorButton.setOnAction(e -> selectedColor = color);
            hbox.getChildren().add(colorButton);
        }
        return hbox;
    }

    private Button createAddTaskButton() {
        Button addTaskButton = new Button("Add Task");
        addTaskButton.setOnAction(event -> {
            String taskText = taskNameField.getText().trim();
            if (!taskText.isEmpty()) {
                // Add task to database
                int taskId = DatabaseManager.addTask(taskText, selectedColor);
                
                // Add task to UI
                if (taskId != -1) {
                    taskController.addTask(taskId, taskText, selectedColor, false);
                    taskNameField.clear();
                } else {
                    // Handle database insertion error
                    System.out.println("Error adding task to database");
                }
            }
        });
        return addTaskButton;
    }

    public VBox getAddTaskArea() {
        return addTaskArea;
    }
}