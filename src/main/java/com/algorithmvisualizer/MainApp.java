package com.algorithmvisualizer;

import com.algorithmvisualizer.utils.AppConstants;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/algorithmvisualizer/fxml/main_view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/com/algorithmvisualizer/css/style.css").toExternalForm());
        
        primaryStage.setTitle(AppConstants.WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
