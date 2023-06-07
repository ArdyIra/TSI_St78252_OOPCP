package com.example.demo;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

@XmlRootElement(name = "Operations")
class OperatioList {
    private ArrayList<Operatio> operatioList;

    @XmlElement(name = "Operation")
    public ArrayList<Operatio> getOperatioList() {
        return operatioList;
    }
    public void setOperatioList(ArrayList<Operatio> operatioList) {
        this.operatioList = operatioList;
    }
}

public class Operatio {
    public Operatio(String operatioName, double operatioPrice) {
        this.operatioName = new SimpleStringProperty(operatioName);
        this.operatioPrice = new SimpleDoubleProperty(operatioPrice);
    }

    public Operatio() {

    }

    protected SimpleStringProperty operatioName;
    protected SimpleDoubleProperty operatioPrice;
    public String getOperatioName() {
        return operatioName.get();
    }

    public void setOperatioName(String operatioName) {
        this.operatioName = new SimpleStringProperty(operatioName);
    }

    public double getOperatioPrice() {
        return operatioPrice.get();
    }

    public void setOperatioPrice(double operatioPrice) {
        this.operatioPrice = new SimpleDoubleProperty(operatioPrice);
    }

    @Override
    public String toString() {
        return getOperatioName() +
                ", Price: " + getOperatioPrice();
    }
}
