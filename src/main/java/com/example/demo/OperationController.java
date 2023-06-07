package com.example.demo;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OperationController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TableView<Operatio> tabelView;

    @FXML
    TableColumn<Operatio, String> nameColumn;
    @FXML
    TableColumn<Operatio, String> priceColumn;


    @FXML
    TextField operNameTextField;
    @FXML
    TextField operPriceTextField;

    @FXML
    AnchorPane operAnchorPane;

    public ArrayList<Operatio> opers = new ArrayList<>();
    public ArrayList<Operatio> tempOpers = new ArrayList<>();

    private ObservableList<Operatio> observableList = FXCollections.observableArrayList();


    public void initialize(){

        loadOperDataFromFile();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("OperatioName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("OperatioPrice"));

        tabelView.setItems(observableList);

        for (Operatio x : opers){
            tabelView.getItems().add(x);
        }

        tabelView.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void backToMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void buttonAdd(ActionEvent event) throws JAXBException, FileNotFoundException {
        try {
            Operatio opero = new Operatio(operNameTextField.getText(),
                    Double.parseDouble(operPriceTextField.getText()));
            boolean dupeCheck = false;
            for (Operatio x : opers)
                if (x.getOperatioName().contains(operNameTextField.getText())) {
                    dupeCheck = true;
                }
            if (dupeCheck) {
                new Alert(Alert.AlertType.ERROR, "An operation with this name is already in the records").show();
            } else {
                tabelView.getItems().add(opero);
                tempGetData();
                operNameTextField.clear();
                operPriceTextField.clear();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Please provide Price as a decimal value.").show();
        }
    }

    public void loadOperDataFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(Operatio.class, OperatioList.class);

            File file = new File("Operations.xml");

            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

            OperatioList o = (OperatioList) jaxbUnmarshaller.unmarshal(file);

            tempOpers = o.getOperatioList();

            if (tempOpers == null) {
                file.delete();
            } else {
                opers = tempOpers;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No file found");
            alert.setHeaderText("The Operations.xml file was not found or corrupt");
            alert.setContentText("A new file will be created");
            alert.showAndWait();
        }
    }

    public void saveOperDataToFile() throws JAXBException, FileNotFoundException {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(Operatio.class, OperatioList.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            OperatioList o = new OperatioList();
            o.setOperatioList(opers);

            m.marshal(o, new File("Operations.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void tempGetData() throws JAXBException, FileNotFoundException {

        Operatio opero = new Operatio();
        opers.clear();
        for (int i = 0; i < tabelView.getItems().size(); i++) {
            opero = tabelView.getItems().get(i);
            Operatio onper = new Operatio();
            onper.setOperatioName(opero.operatioName.get());
            onper.setOperatioPrice(opero.operatioPrice.get());

            opers.add(onper);
        }
        saveOperDataToFile();
    }






    public void nameChanged(TableColumn.CellEditEvent<Operatio, String> operatioStringCellEditEvent)
            throws JAXBException, FileNotFoundException {
        Operatio oper = tabelView.getSelectionModel().getSelectedItem();
        oper.setOperatioName(operatioStringCellEditEvent.getNewValue());
        tempGetData();
    }



    public void delRecord(ActionEvent actionEvent) throws JAXBException, FileNotFoundException {
        try {
            ObservableList<Operatio> allOpers, selectedOper;
            allOpers = tabelView.getItems();
            selectedOper = tabelView.getSelectionModel().getSelectedItems();
            selectedOper.forEach(allOpers::remove);
            tabelView.setItems(observableList);
            tempGetData();
        } catch (Exception e) {

        }
    }
    public void keyPressed(KeyEvent event) throws IOException, JAXBException {
        if (event.getCode() == KeyCode.ESCAPE) {
            root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else if (event.getCode() == KeyCode.ENTER) {
            try {
                Operatio opero = new Operatio(operNameTextField.getText(),
                        Double.parseDouble(operPriceTextField.getText()));
                boolean dupeCheck = false;
                for (Operatio x : opers)
                    if (x.getOperatioName().contains(operNameTextField.getText())) {
                        dupeCheck = true;
                    }
                if (dupeCheck) {
                    new Alert(Alert.AlertType.ERROR, "An operation with this name is already in the records").show();
                } else {
                    tabelView.getItems().add(opero);
                    tempGetData();
                    operNameTextField.clear();
                    operPriceTextField.clear();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Please provide Price as a decimal value.").show();
            }
        }
    }
}

