package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button exitButton;
    @FXML
    private AnchorPane scenePane;

    public void switchToPatients(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("PatientScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDoctors(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("DoctorScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAppointments(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AppointmentScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToOperations(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("OperationScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void logout(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You are about to close the scheduler!");
        alert.setContentText("Are you sure that you want to do that?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }
}