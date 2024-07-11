package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ToolbarComponent {
    private HBox toolbar;
    private ToolbarController controller;
    private Button searchButton;  

    public ToolbarComponent(ToolbarController controller) {
        this.controller = controller;
        initializeToolbar();
    }

    private void initializeToolbar() {
        toolbar = new HBox();
        
        searchButton = createIconButton("/icons/search-icon.png", "Search");
        searchButton.setOnAction(e -> controller.handleSearchAction(searchButton));  

        Button filterButton = createIconButton("/icons/filter.png", "Filter");
        filterButton.setOnAction(e -> controller.handleFilterAction(filterButton));
         
        toolbar.getChildren().addAll(searchButton, filterButton);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setSpacing(10);
        toolbar.setPrefHeight(30);
        toolbar.setStyle("-fx-background-color: #E0E0E0;");
    }

    private Button createIconButton(String iconPath, String tooltip) {
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);

        Button button = new Button();
        button.setGraphic(imageView);
        button.setTooltip(new Tooltip(tooltip));
        
        // Create a glow effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.DODGERBLUE);
        glow.setWidth(20);
        glow.setHeight(20);
        glow.setRadius(10);
        
        // Apply styles and hover effect
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-padding: 5px;" +
            "-fx-border-color: transparent;" +
            "-fx-border-radius: 5px;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setEffect(glow);
            button.setStyle(
                "-fx-background-color: rgba(0,0,0,0.1);" +
                "-fx-padding: 5px;" +
                "-fx-border-color: rgba(0,0,0,0.2);" +
                "-fx-border-radius: 5px;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setEffect(null);
            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-padding: 5px;" +
                "-fx-border-color: transparent;" +
                "-fx-border-radius: 5px;"
            );
        });

        return button;
    }

    public HBox getToolbar() {
        return toolbar;
    }
}