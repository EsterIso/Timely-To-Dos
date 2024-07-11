package gui;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class ToolbarController {
    private MainWindow mainWindow;

    public ToolbarController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void handleSearchAction(Button searchButton) {
        openSearchPopup(searchButton);
    }

    public void handleFilterAction(Button filterButton) {
        openFilterPopup(filterButton);
    }
    
    private void openSearchPopup(Button searchButton) {
        Popup popup = new Popup();

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Enter search term");

        Button searchButtonPopup = new Button("Search");
        searchButtonPopup.setOnAction(e -> {
            String searchTerm = searchTextField.getText();
            performSearch(searchTerm);
            popup.hide();
        });

        HBox searchBox = new HBox(10, searchTextField, searchButtonPopup);
        searchBox.setPadding(new Insets(10));

        VBox popupContent = new VBox(searchBox);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-border-width: 1;");

        popup.getContent().add(popupContent);
        popup.setAutoHide(true);

        // Calculate the position to show the popup
        Bounds buttonBounds = searchButton.localToScreen(searchButton.getBoundsInLocal());
        double x = buttonBounds.getMinX();
        double y = buttonBounds.getMaxY();

        popup.show(mainWindow.getPrimaryStage(), x, y);
    }

    private void performSearch(String searchTerm) {
        // This might involve iterating through tasks and highlighting matches
        for (javafx.scene.Node node : mainWindow.getTasksDisplayBox().getChildren()) {
            if (node instanceof HBox) {
                HBox taskPane = (HBox) node;
                // Assuming the second child of HBox is the TextArea containing the task text
                if (taskPane.getChildren().size() > 1 && taskPane.getChildren().get(1) instanceof javafx.scene.control.TextArea) {
                    javafx.scene.control.TextArea taskArea = (javafx.scene.control.TextArea) taskPane.getChildren().get(1);
                    if (taskArea.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
                        // Highlight the matching task
                        taskPane.setStyle("-fx-background-color: #fff68f;");
                    } else {
                        // Reset the style for non-matching tasks
                        taskPane.setStyle("");
                    }
                }
            }
        }
    }
    private void openFilterPopup(Button filterButton) {
        Popup popup = new Popup();

        CheckBox redFilter = new CheckBox("Red");
        CheckBox roseFilter = new CheckBox("Rose");
        CheckBox yellowFilter = new CheckBox("Yellow");
        CheckBox orangeFilter = new CheckBox("Orange");
        CheckBox emeraldFilter = new CheckBox("Emerald");
        CheckBox cyanFilter = new CheckBox("Cyan");
        CheckBox skyBlueFilter = new CheckBox("Sky Blue");
        CheckBox violetFilter = new CheckBox("Violet");
        CheckBox whiteFilter = new CheckBox("White (Default)");

        Button applyFilter = new Button("Apply Filter");
        applyFilter.setOnAction(e -> {
            performFilter(redFilter.isSelected(), roseFilter.isSelected(), 
                          yellowFilter.isSelected(), orangeFilter.isSelected(),
                          emeraldFilter.isSelected(), cyanFilter.isSelected(),
                          skyBlueFilter.isSelected(), violetFilter.isSelected(),
                          whiteFilter.isSelected());
            popup.hide();
        });

        VBox filterOptions = new VBox(10, redFilter, roseFilter, yellowFilter, orangeFilter,
                                      emeraldFilter, cyanFilter, skyBlueFilter, violetFilter,
                                      whiteFilter, applyFilter);
        filterOptions.setPadding(new Insets(10));
        filterOptions.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-border-width: 1;");

        popup.getContent().add(filterOptions);
        popup.setAutoHide(true);

        // Calculate the position to show the popup
        Bounds buttonBounds = filterButton.localToScreen(filterButton.getBoundsInLocal());
        double x = buttonBounds.getMinX();
        double y = buttonBounds.getMaxY();

        popup.show(mainWindow.getPrimaryStage(), x, y);
    }

    private void performFilter(boolean red, boolean rose, boolean yellow, boolean orange,
                               boolean emerald, boolean cyan, boolean skyBlue, boolean violet,
                               boolean white) {
        for (javafx.scene.Node node : mainWindow.getTasksDisplayBox().getChildren()) {
            if (node instanceof HBox) {
                HBox taskPane = (HBox) node;
                if (taskPane.getChildren().size() > 1 && taskPane.getChildren().get(1) instanceof TextArea) {
                    TextArea taskArea = (TextArea) taskPane.getChildren().get(1);
                    String style = taskArea.getStyle();
                    String backgroundColor = extractBackgroundColor(style);

                    boolean shouldShow = (red && backgroundColor.equals("#f44336")) ||
                                         (rose && backgroundColor.equals("#f288bc")) ||
                                         (yellow && backgroundColor.equals("#f1c232")) ||
                                         (orange && backgroundColor.equals("#f09b41")) ||
                                         (emerald && backgroundColor.equals("#7ada8e")) ||
                                         (cyan && backgroundColor.equals("#7adaca")) ||
                                         (skyBlue && backgroundColor.equals("#76bcec")) ||
                                         (violet && backgroundColor.equals("#baa0ff")) ||
                                         (white && (backgroundColor.equals("#ffffff") || backgroundColor.isEmpty())) ||
                                         (!red && !rose && !yellow && !orange && !emerald && !cyan && !skyBlue && !violet && !white);  // Show all if none selected

                    taskPane.setVisible(shouldShow);
                    taskPane.setManaged(shouldShow);
                }
            }
        }
    }

    private String extractBackgroundColor(String style) {
        String[] parts = style.split(";");
        for (String part : parts) {
            if (part.trim().startsWith("-fx-control-inner-background:")) {
                return part.trim().split(":")[1].trim();
            }
        }
        return "";
    }
}