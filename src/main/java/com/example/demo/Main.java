package com.example.demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

        Scene scene = new Scene(root, Color.DARKSEAGREEN);
        Image icon = new Image("WindowIcon.png");

        stage.getIcons().add(icon);
        stage.setTitle("Doctor Appointment Scheduler");
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();;
            logout(stage);
        });
    }

    public void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You are about to close the scheduler!");
        alert.setContentText("Are you sure that you want to do that?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}