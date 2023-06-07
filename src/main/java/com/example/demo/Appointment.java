package com.example.demo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

@XmlRootElement(name = "Appointments")
class AppointmentList {
    private ArrayList<Appointment> appoList;

    @XmlElement(name = "Appointment")
    public ArrayList<Appointment> getAppointmentList() {
        return appoList;
    }

    public void setAppointmentList(ArrayList<Appointment> appoList) {
        this.appoList = appoList;
    }
}

public class Appointment {

    public Appointment(String doctero, String patiento,
                       String opero, String appoDate, String appoTime) {
        this.doctero = new SimpleStringProperty(doctero);
        this.patiento = new SimpleStringProperty(patiento);
        this.opero = new SimpleStringProperty(opero);
        this.appoDate = new SimpleStringProperty(appoDate);
        this.appoTime = new SimpleStringProperty(appoTime);
    }
    public Appointment() {

    }

    protected SimpleStringProperty doctero;
    protected SimpleStringProperty patiento;
    protected SimpleStringProperty opero;
    protected SimpleStringProperty appoDate;
    protected SimpleStringProperty appoTime;

    public String getDoctero() {
        return doctero.get();
    }

    public void setDoctero(String doctero) {
        this.doctero = new SimpleStringProperty(doctero);
    }

    public String getPatiento() {
        return patiento.get();
    }

    public void setPatiento(String patiento) {
        this.patiento = new SimpleStringProperty(patiento);
    }

    public String getOpero() {
        return opero.get();
    }

    public void setOpero(String opero) {
        this.opero = new SimpleStringProperty(opero);
    }

    public String getAppoDate() {
        return appoDate.get();
    }

    public void setAppoDate(String appoDate) {
        this.appoDate = new SimpleStringProperty(appoDate);
    }

    public String getAppoTime() {
        return appoTime.get();
    }

    public void setAppoTime(String appoTime) {
        this.appoTime = new SimpleStringProperty(appoTime);
    }

    @Override
    public String toString() {
        return getDoctero() +
                ", " + getPatiento() +
                ", " + getOpero() +
                ", " + getAppoDate() +
                ", " + getAppoTime();
    }
}
