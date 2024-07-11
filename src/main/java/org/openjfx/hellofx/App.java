package org.openjfx.hellofx;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import database.DatabaseManager;
import gui.MainWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	

    @Override
    public void start(Stage primaryStage) {
        try {
            MainWindow mainWindow = new MainWindow(primaryStage);  
            BorderPane root = new BorderPane();

            // Create the toolbar from MainWindow
            HBox toolbar = mainWindow.ToolBar();
            
            // Create the main layout from MainWindow
            VBox mainLayout = mainWindow.createMainLayout();
            mainLayout.setStyle("-fx-background-color: #eeeeee;");
            
            // Add the toolbar to the top of BorderPane
            root.setTop(toolbar);
            // Add the main layout to the center of BorderPane
            root.setCenter(mainLayout);

            Scene scene = new Scene(root, 600, 800);
            primaryStage.setTitle("Timely To Dos");
            Image icon = new Image(getClass().getResourceAsStream("/icons/TimelyToDos.png"));
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }
    
    @Override
	public void stop() throws Exception {
	    // Perform any cleanup operations here
    	DatabaseManager.closeConnection();
	    super.stop();
	}
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
}

