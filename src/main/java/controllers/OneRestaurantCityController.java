package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.util.Vector;

public class OneRestaurantCityController implements Controller {

    private MainGUI oneRestaurCityWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TextField cityField;
    @FXML
    private TableView<String> tblEmployee;
    @FXML
    private TableColumn<String,String> employeeColumn;


    @Override
    public void setMainApp(MainGUI main) {
        oneRestaurCityWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {

    }

    public void onClickEnter() {
        employeeColumn.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });

        Vector<String> rs = businessLogic.getEmployee1RestCity(cityField.getText());
        tblEmployee.getItems().clear();

        if(!rs.isEmpty()){
            tblEmployee.getItems().addAll(rs);
        }else{
            tblEmployee.getItems().add("There is no such employee");
        }

        

    }

    public void onClickBack() {
    }
}
