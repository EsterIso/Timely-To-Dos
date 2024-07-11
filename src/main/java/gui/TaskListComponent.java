package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TaskListComponent {
    private VBox tasksDisplayBox;
    private TaskController taskController;

    public TaskListComponent() {
        initializeTaskList();
    }

    private void initializeTaskList() {
        tasksDisplayBox = new VBox(10); // 10 is the spacing between tasks
        tasksDisplayBox.setAlignment(Pos.TOP_CENTER);
    }

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }

    public void addTask(StackPane taskPane) {
        tasksDisplayBox.getChildren().add(taskPane);
    }

    public VBox getTasksDisplayBox() {
        return tasksDisplayBox;
    }
}