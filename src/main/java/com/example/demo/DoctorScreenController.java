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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DoctorScreenController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    TableView<PatientOrDoc> tabelView;

    @FXML
    TableColumn<PatientOrDoc, String> nameColumn;
    @FXML
    TableColumn<PatientOrDoc, String> surnameColumn;
    @FXML
    TableColumn<PatientOrDoc, String> perCodeColumn;
    @FXML
    TableColumn<PatientOrDoc, String> phoneNumColumn;
    @FXML
    TableColumn<PatientOrDoc, String> emailColumn;

    @FXML
    TextField nameTextField;
    @FXML
    TextField surnameTextField;
    @FXML
    TextField personCodeTextField;
    @FXML
    TextField phoneNumTextField;
    @FXML
    TextField emailTextField;
    @FXML
    AnchorPane doctorAnchorPane;

    public ArrayList<PatientOrDoc> doctors = new ArrayList<>();
    public ArrayList<PatientOrDoc> tempDoctors = new ArrayList<>();

    private ObservableList<PatientOrDoc> observableList = FXCollections.observableArrayList();

    public void initialize(){

        loadPersonDataFromFile();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        perCodeColumn.setCellValueFactory(new PropertyValueFactory<>("PersonCode"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNum"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));

        tabelView.setItems(observableList);

        for (PatientOrDoc x : doctors){
            tabelView.getItems().add(x);
        }

        tabelView.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        perCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }


    public void backToMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void buttonAdd(ActionEvent event) throws JAXBException, FileNotFoundException {
        PatientOrDoc patDoc = new PatientOrDoc(nameTextField.getText(),
                surnameTextField.getText(), personCodeTextField.getText(),
                phoneNumTextField.getText(), emailTextField.getText());
        boolean dupeCheck = false;
        for (PatientOrDoc x : doctors)
            if (x.getPersonCode().contains(personCodeTextField.getText())) {
                dupeCheck = true;
            }
        if (dupeCheck) {
            new Alert(Alert.AlertType.ERROR, "A doctor with this Personal Code already exists").show();
        }
        else {
            tabelView.getItems().add(patDoc);
            tempGetData();
            nameTextField.clear();
            surnameTextField.clear();
            personCodeTextField.clear();
            phoneNumTextField.clear();
            emailTextField.clear();
        }
    }

    public void loadPersonDataFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(PatientOrDoc.class, PatientOrDocList.class);

            File file = new File("Doctors.xml");

            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

            PatientOrDocList o = (PatientOrDocList) jaxbUnmarshaller.unmarshal(file);


            tempDoctors = o.getPatientOrDocList();

            if (tempDoctors == null) {

                file.delete();
            } else {

                doctors = tempDoctors;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No file found");
            alert.setHeaderText("The Doctors.xml file was not found or corrupt");
            alert.setContentText("A new file will be created");
            alert.showAndWait();
        }
    }


    public void savePersonDataToFile() throws JAXBException, FileNotFoundException {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PatientOrDoc.class, PatientOrDocList.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            PatientOrDocList o = new PatientOrDocList();
            o.setPatientOrDocList(doctors);

            m.marshal(o, new File("Doctors.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void tempGetData() throws JAXBException, FileNotFoundException {

        PatientOrDoc patDoc = new PatientOrDoc();
        doctors.clear();
        for (int i = 0; i < tabelView.getItems().size(); i++) {
            patDoc = tabelView.getItems().get(i);
            PatientOrDoc patient = new PatientOrDoc();
            patient.setName(patDoc.name.get());
            patient.setSurname(patDoc.surname.get());
            patient.setPersonCode(patDoc.personCode.get());
            patient.setPhoneNum(patDoc.phoneNum.get());
            patient.setEmail(patDoc.email.get());
            doctors.add(patient);
        }
        savePersonDataToFile();
    }

    public void delRecord(ActionEvent actionEvent) throws JAXBException, FileNotFoundException {
        try {
            ObservableList<PatientOrDoc> allPatients, selectedPatient;
            allPatients = tabelView.getItems();
            selectedPatient = tabelView.getSelectionModel().getSelectedItems();
            selectedPatient.forEach(allPatients::remove);
            tabelView.setItems(observableList);
            tempGetData();
        } catch (Exception e) {

        }
    }

    public void nameChanged(TableColumn.CellEditEvent<PatientOrDoc, String> patientOrDocStringCellEditEvent)
            throws JAXBException, FileNotFoundException {
        PatientOrDoc patOrDoc = tabelView.getSelectionModel().getSelectedItem();
        patOrDoc.setName(patientOrDocStringCellEditEvent.getNewValue());
        tempGetData();
    }

    public void surnameChanged(TableColumn.CellEditEvent<PatientOrDoc, String> patientOrDocStringCellEditEvent)
            throws JAXBException, FileNotFoundException {
        PatientOrDoc patOrDoc = tabelView.getSelectionModel().getSelectedItem();
        patOrDoc.setSurname(patientOrDocStringCellEditEvent.getNewValue());
        tempGetData();
    }

    public void perCodeChanged(TableColumn.CellEditEvent<PatientOrDoc, String> patientOrDocStringCellEditEvent)
            throws JAXBException, FileNotFoundException {
        PatientOrDoc patOrDoc = tabelView.getSelectionModel().getSelectedItem();
        patOrDoc.setPersonCode(patientOrDocStringCellEditEvent.getNewValue());
        tempGetData();
    }

    public void phoneNumChanged(TableColumn.CellEditEvent<PatientOrDoc, String> patientOrDocStringCellEditEvent)
            throws JAXBException, FileNotFoundException {
        PatientOrDoc patOrDoc = tabelView.getSelectionModel().getSelectedItem();
        patOrDoc.setPhoneNum(patientOrDocStringCellEditEvent.getNewValue());
        tempGetData();
    }

    public void emailChanged(TableColumn.CellEditEvent<PatientOrDoc, String> patientOrDocStringCellEditEvent)
            throws JAXBException, FileNotFoundException {
        PatientOrDoc patOrDoc = tabelView.getSelectionModel().getSelectedItem();
        patOrDoc.setEmail(patientOrDocStringCellEditEvent.getNewValue());
        tempGetData();
    }
    public void keyPressed(KeyEvent event) throws IOException, JAXBException {
        if (event.getCode() == KeyCode.ESCAPE) {
            root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if (event.getCode() == KeyCode.ENTER) {
            PatientOrDoc patDoc = new PatientOrDoc(nameTextField.getText(),
                    surnameTextField.getText(), personCodeTextField.getText(),
                    phoneNumTextField.getText(), emailTextField.getText());
            boolean dupeCheck = false;
            for (PatientOrDoc x : doctors)
                if (x.getPersonCode().contains(personCodeTextField.getText())) {
                    dupeCheck = true;
                }
            if (dupeCheck) {
                new Alert(Alert.AlertType.ERROR, "A doctor with this Personal Code already exists").show();
            }
            else {
                tabelView.getItems().add(patDoc);
                tempGetData();
                nameTextField.clear();
                surnameTextField.clear();
                personCodeTextField.clear();
                phoneNumTextField.clear();
                emailTextField.clear();
            }
        }
        }
    }

