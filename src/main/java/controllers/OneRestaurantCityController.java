package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    @FXML
    private Label errorLbl;


    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        oneRestaurCityWin = main;
    }

    /**
     * Method to initialize the information in the UI
     * @throws SQLException
     */
    @Override
    public void initializeInformation() throws SQLException {
        cityField.setText("");
        tblEmployee.getItems().clear();
    }

    /**
     *
     */
    @FXML
    public void onClickEnter() {
        employeeColumn.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });

        if(cityField.getText().isEmpty()) {
            errorLbl.setText("Please, fill all the fields");
        }else if(!check(cityField.getText())){
            errorLbl.setText("Please, enter a city, not a number");
        }else{

            Vector<String> rs = businessLogic.getEmployee1RestCity(cityField.getText());
            tblEmployee.getItems().clear();

            if(!rs.isEmpty()){
                tblEmployee.getItems().addAll(rs);
            }else{
                tblEmployee.getItems().add("There is no such employee");
            }
        }

    }

    /**
     * Check whether the text inserted is not a number
     * @param text String - the city
     * @return boolean - whether or not is valid
     */
    private boolean check(String text) {
        for(int i = 0; i<text.length();i++){
            if(text.charAt(i) >= '0' && text.charAt(i)<='9'){
                return false;
            }
        }

        return true;
    }

    /**
     * Method to return to the parent window
     */
    public void onClickBack() {
        oneRestaurCityWin.showQuery();
    }
}
