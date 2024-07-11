package gui;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseManager;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {
    private ToolbarComponent toolbarComponent;
    private AddTaskComponent addTaskComponent;
    private TaskController taskController;
    private ToolbarController toolbarController;
    private VBox tasksDisplayBox;
    private Stage primaryStage;  
    
    public MainWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupCloseHandler(primaryStage);
        DatabaseManager.initializeDatabase();
        taskController = new TaskController(this);
        toolbarController = new ToolbarController(this);
        toolbarComponent = new ToolbarComponent(toolbarController);
        addTaskComponent = new AddTaskComponent(taskController);
        initializeTasksDisplayBox();
        loadTasksFromDatabase();
    }
    
    private void loadTasksFromDatabase() {
        ResultSet tasks = DatabaseManager.getAllTasks();
        try {
            while (tasks.next()) {
                int id = tasks.getInt("id");
                String text = tasks.getString("text");
                String color = tasks.getString("color");
                boolean isCompleted = tasks.getBoolean("is_completed");
                taskController.addTask(id, text, color, isCompleted);
            }
        } catch (SQLException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public VBox createMainLayout() {
        VBox mainLayout = new VBox(20);
        mainLayout.getChildren().addAll(
            addTaskComponent.getAddTaskArea(),
            createScrollPane(tasksDisplayBox)
        );
        return mainLayout;
    }

    public HBox ToolBar() {
        return toolbarComponent.getToolbar();
    }

    private ScrollPane createScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(600);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    private void initializeTasksDisplayBox() {
        tasksDisplayBox = new VBox(10); // 10 is the spacing between tasks
        tasksDisplayBox.setAlignment(Pos.TOP_CENTER);
    }

    public VBox getTasksDisplayBox() {
        return tasksDisplayBox;
    }
    public void clearSearchHighlights() {
        for (javafx.scene.Node node : tasksDisplayBox.getChildren()) {
            if (node instanceof HBox) {
                node.setStyle("");
            }
        }
    }
    public void setupCloseHandler(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}