package gui;

import database.DatabaseManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class TaskController {
    private MainWindow mainWindow;
    private String selectedColor;
    private boolean colorChanged = false;
    
    public TaskController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void addTask(String task, String backgroundColor) {
    	int id = DatabaseManager.addTask(task, backgroundColor);
        if (id != -1) {
            Platform.runLater(() -> {
                HBox taskPane = createTaskPane(id, task, backgroundColor, false);
                mainWindow.getTasksDisplayBox().getChildren().add(taskPane);
                mainWindow.getTasksDisplayBox().requestLayout();
            });
        } else {
            System.err.println("Failed to add task to database");
        }
    }

    public void addTask(int id, String task, String backgroundColor, boolean isCompleted) {
        HBox taskPane = createTaskPane(id, task, backgroundColor, isCompleted);
        mainWindow.getTasksDisplayBox().getChildren().add(taskPane);
    }

    private HBox createTaskPane(int id, String task, String backgroundColor, boolean isCompleted) {
        HBox taskPane = new HBox(10);  // 10 is the spacing between elements
        taskPane.setAlignment(Pos.CENTER_LEFT);
        taskPane.setPadding(new Insets(5));
        taskPane.setStyle("-fx-background-color: transparent;");
        taskPane.setUserData(id);
        
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(isCompleted);
        checkBox.setOnAction(event -> {
            int taskId = (int) taskPane.getUserData();
            boolean newState = checkBox.isSelected();
            DatabaseManager.updateTaskCompletion(taskId, newState);
        });
        
        TextArea taskDisplay = new TextArea(task);
        taskDisplay.setWrapText(true);
        taskDisplay.setEditable(false);
        taskDisplay.setPrefWidth(400);
        taskDisplay.setMaxWidth(Double.MAX_VALUE);
        taskDisplay.setPrefHeight(40);
        taskDisplay.setStyle("-fx-control-inner-background: " + backgroundColor + "; -fx-text-fill: black; -fx-font-size: 14px;"
        		+ "-fx-text-alignment: center;-fx-display: flex; -fx-justify-content: center;-fx-align-items: center;"); // Ensure the text color is set to black
        HBox.setHgrow(taskDisplay, Priority.ALWAYS);
        
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> openEditPopup(taskPane));
        
        taskPane.getChildren().addAll(checkBox, taskDisplay, editButton);

        return taskPane;
    }

    private void openEditPopup(HBox taskPane) {
    	Popup popup = new Popup();
    	
        TextArea currentTaskArea = (TextArea) taskPane.getChildren().get(1);
        String currentTask = currentTaskArea.getText();
        String currentColor = currentTaskArea.getStyle().split("-fx-control-inner-background: ")[1].split(";")[0];

        TextField editTaskField = new TextField(currentTask);
        editTaskField.setPromptText("Edit Task");
        editTaskField.setPrefWidth(300);

        selectedColor = currentColor;
        colorChanged = false;

        HBox colorButtons = createColorButtons();
        updateColorSelection(colorButtons, currentColor);
        
        HBox saveDel = new HBox();
        saveDel.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String newTask = editTaskField.getText();
            editTask(taskPane, newTask, colorChanged ? selectedColor : currentColor);
            popup.hide();
        });
        
        Button delButton = new Button("Delete");
        delButton.setOnAction(e -> {
        	removeTask(taskPane);
            popup.hide();
        });
        
        saveDel.getChildren().addAll(saveButton, delButton);
        
        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(10));
        popupContent.getChildren().addAll(editTaskField, colorButtons, saveDel);
        popupContent.setStyle("-fx-background-color: #eeeeee; -fx-border-color: gray; -fx-border-width: 1;");

        popup.getContent().add(popupContent);

        popup.setAutoHide(true);
        popup.show(taskPane, taskPane.localToScreen(taskPane.getBoundsInLocal()).getMinX(),
                taskPane.localToScreen(taskPane.getBoundsInLocal()).getMaxY());
        taskPane.getScene().getRoot().requestFocus();
    }

    private HBox createColorButtons() {
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.CENTER);
        String[] colors = {"#f44336", "#f288bc", "#f1c232", "#f09b41", "#7ada8e", "#7adaca", "#76bcec", "#baa0ff", "#ffffff"};
        
        for (String color : colors) {
            Button colorButton = new Button();
            colorButton.setStyle("-fx-background-color: " + color + "; -fx-min-width: 20px; -fx-min-height: 20px; -fx-max-width: 20px; -fx-max-height: 20px;");
            colorButton.setOnAction(e -> {
                selectedColor = color;
                colorChanged = true;
                updateColorSelection(hbox, color);
            });
            hbox.getChildren().add(colorButton);
        }
        return hbox;
    }

    private void updateColorSelection(HBox hbox, String selectedColor) {
        for (javafx.scene.Node node : hbox.getChildren()) {
            if (node instanceof Button) {
                Button colorButton = (Button) node;
                String buttonColor = colorButton.getStyle().split("background-color: ")[1].split(";")[0];
                if (buttonColor.equals(selectedColor)) {
                    colorButton.setStyle(colorButton.getStyle() + "-fx-border-color: black; -fx-border-width: 2px;");
                } else {
                    colorButton.setStyle(colorButton.getStyle().replace("-fx-border-color: black; -fx-border-width: 2px;", ""));
                }
            }
        }
    }

    public void editTask(HBox taskPane, String newTask, String newColor) {
        int id = (int) taskPane.getUserData();
        TextArea taskDisplay = (TextArea) taskPane.getChildren().get(1);
        CheckBox checkBox = (CheckBox) taskPane.getChildren().get(0);
        
        taskDisplay.setText(newTask);
        taskDisplay.setStyle("-fx-control-inner-background: " + newColor + "; -fx-text-fill: black; -fx-font-size: 14px;"
        		+ "-fx-text-alignment: center;-fx-display: flex; -fx-justify-content: center;-fx-align-items: center;");
        
        DatabaseManager.updateTask(id, newTask, newColor, checkBox.isSelected());
    }
    
    public void removeTask(HBox taskPane) {
        int id = (int) taskPane.getUserData();
        DatabaseManager.deleteTask(id);
        
        VBox parentContainer = (VBox) taskPane.getParent();
        if (parentContainer != null) {
            parentContainer.getChildren().remove(taskPane);
        }
    }
}