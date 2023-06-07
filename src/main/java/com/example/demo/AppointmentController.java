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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private DatePicker appoDatePicker;
    @FXML
    private Label tempLabel;
    @FXML
    private ChoiceBox<PatientOrDoc> doctorChoiceBox;
    @FXML
    private ChoiceBox<PatientOrDoc> patientChoiceBox;
    @FXML
    private ChoiceBox<Operatio> operationChoiceBox;
    @FXML
    private Spinner<Integer> hourSpinner;
    @FXML
    private Spinner<Integer> minuteSpinner;

    @FXML
    TableView<Appointment> tabelView;

    @FXML
    TableColumn<Appointment, String> doctorColumn;
    @FXML
    TableColumn<Appointment, String> patientColumn;
    @FXML
    TableColumn<Appointment, String> timeColumn;
    @FXML
    TableColumn<Appointment, String> dateColumn;
    @FXML
    TableColumn<Appointment, String> operationColumn;




    public ArrayList<PatientOrDoc> patients = new ArrayList<>();
    public ArrayList<PatientOrDoc> tempPatients = new ArrayList<>();
    public ArrayList<PatientOrDoc> doctors = new ArrayList<>();
    public ArrayList<PatientOrDoc> tempDoctors = new ArrayList<>();
    public ArrayList<Operatio> opers = new ArrayList<>();
    public ArrayList<Operatio> tempOpers = new ArrayList<>();


    public ArrayList<Appointment> appos = new ArrayList<>();
    public ArrayList<Appointment> tempAppos = new ArrayList<>();
    private ObservableList<Appointment> observableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadPatientDataFromFile();
        loadDoctorDataFromFile();
        loadOperDataFromFile();
        loadAppoDataFromFile();


        patientChoiceBox.getItems().addAll(patients);
        doctorChoiceBox.getItems().addAll(doctors);
        operationChoiceBox.getItems().addAll(opers);

        SpinnerValueFactory<Integer> valueFactoryHour =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        valueFactoryHour.setValue(12);
        SpinnerValueFactory<Integer> valueFactoryMin =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        valueFactoryMin.setValue(30);

        hourSpinner.setValueFactory(valueFactoryHour);
        minuteSpinner.setValueFactory(valueFactoryMin);

        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("Doctero"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("Patiento"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("AppoTime"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("AppoDate"));
        operationColumn.setCellValueFactory(new PropertyValueFactory<>("Opero"));



        tabelView.setItems(observableList);

        for (Appointment x : appos){
            tabelView.getItems().add(x);
        }

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
            LocalDate theDate = appoDatePicker.getValue();
            String formattedDate = theDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String time = hourSpinner.getValue().toString() + ":" + minuteSpinner.getValue().toString();
            String doc = doctorChoiceBox.getValue().getName() + " " + doctorChoiceBox.getValue().getSurname() + " " +
                    doctorChoiceBox.getValue().getPersonCode();
            String pat = patientChoiceBox.getValue().getName() + " " + patientChoiceBox.getValue().getSurname() + " " +
                    patientChoiceBox.getValue().getPersonCode();
            String op = operationChoiceBox.getValue().toString();

            Appointment appero = new Appointment(doc, pat, op, formattedDate, time);

                tabelView.getItems().add(appero);
                tempGetData();


        } catch (Exception e) {
            //new Alert(Alert.AlertType.ERROR, "Please provide Price as a decimal value.").show();
        }
    }

    public void delRecord(ActionEvent actionEvent) throws JAXBException, FileNotFoundException {
        try {
            ObservableList<Appointment> allAppos, selectedAppo;
            allAppos = tabelView.getItems();
            selectedAppo = tabelView.getSelectionModel().getSelectedItems();
            selectedAppo.forEach(allAppos::remove);
            tabelView.setItems(observableList);
            tempGetData();
        } catch (Exception e) {

        }
    }

    public void loadAppoDataFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(Appointment.class, AppointmentList.class);

            File file = new File("Appointments.xml");

            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

            AppointmentList o = (AppointmentList) jaxbUnmarshaller.unmarshal(file);

            tempAppos = o.getAppointmentList();

            if (tempAppos == null) {
                file.delete();
            } else {
                appos = tempAppos;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No file found");
            alert.setHeaderText("The Appointments.xml file was not found or corrupt");
            alert.setContentText("A new file will be created");
            alert.showAndWait();
        }
    }

    public void saveAppoDataToFile() throws JAXBException, FileNotFoundException {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(Appointment.class, AppointmentList.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            AppointmentList o = new AppointmentList();
            o.setAppointmentList(appos);

            m.marshal(o, new File("Appointments.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void tempGetData() throws JAXBException, FileNotFoundException {

        Appointment appero = new Appointment();
        appos.clear();
        for (int i = 0; i < tabelView.getItems().size(); i++) {
            appero = tabelView.getItems().get(i);
            Appointment appont = new Appointment();
            appont.setDoctero(appero.doctero.get());
            appont.setPatiento(appero.patiento.get());
            appont.setAppoTime(appero.appoTime.get());
            appont.setAppoDate(appero.appoDate.get());
            appont.setOpero(appero.opero.get());

            appos.add(appont);
        }
        saveAppoDataToFile();
    }
    public void keyPressed(KeyEvent event) throws IOException, JAXBException {
       if (event.getCode() == KeyCode.ESCAPE) {
            root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }

    public void loadPatientDataFromFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(PatientOrDoc.class, PatientOrDocList.class);

            File file = new File("Patients.xml");

            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

            PatientOrDocList o = (PatientOrDocList) jaxbUnmarshaller.unmarshal(file);


            tempPatients = o.getPatientOrDocList();

            if (tempPatients == null) {

                file.delete();
            } else {

                patients = tempPatients;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No file found");
            alert.setHeaderText("The Patients.xml file was not found or corrupt");
            alert.setContentText("A new file will be created");
            alert.showAndWait();
        }
    }
    public void loadDoctorDataFromFile() {
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
}
