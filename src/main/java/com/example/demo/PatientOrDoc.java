package com.example.demo;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
@XmlRootElement(name = "Patients")
class PatientOrDocList {
    private ArrayList<PatientOrDoc> perList;

    @XmlElement(name = "Person")
    public ArrayList<PatientOrDoc> getPatientOrDocList() {
        return perList;
    }

    public void setPatientOrDocList(ArrayList<PatientOrDoc> perList) {
        this.perList = perList;
    }
}
public class PatientOrDoc {

   public PatientOrDoc(String name, String surname, String personCode, String phoneNum, String email) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.personCode = new SimpleStringProperty(personCode);
        this.phoneNum = new SimpleStringProperty(phoneNum);
        this.email = new SimpleStringProperty(email);
    }
    public PatientOrDoc() {

    }

    protected SimpleStringProperty name;
    protected SimpleStringProperty surname;
    protected SimpleStringProperty personCode;
    protected SimpleStringProperty phoneNum;
    protected SimpleStringProperty email;

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String surname) {
        this.surname = new SimpleStringProperty(surname);
    }

    public String getPersonCode() {
        return personCode.get();
    }

    public void setPersonCode(String personCode) {
        this.personCode = new SimpleStringProperty(personCode);
    }

    public String getPhoneNum() {
        return phoneNum.get();
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = new SimpleStringProperty(phoneNum);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email = new SimpleStringProperty(email);
    }

    @Override
    public String toString() {
        return getName() +
                ", " + getSurname() +
                ", " + getPersonCode() +
                ", " + getPhoneNum() +
                ", " + getEmail();
        }

}



