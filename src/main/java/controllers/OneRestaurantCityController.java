package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import exceptions.UncompletedRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.util.Vector;
/**
 * This class aims to deal with the window that handles getting the employees that attend a single restaurant in a given city
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class OneRestaurantCityController implements Controller {

    private MainGUI oneRestaurCityWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

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
     * Method to initialize the information in the UIç
     */
    @Override
    public void initializeInformation()  {
        cityField.setText("");
        errorLbl.setText("");
        tblEmployee.getItems().clear();
    }

    /**
     * This method handles getting the employees that attend a single restaurant in the given city
     */
    @FXML
    public void onClickEnter() {
        try {
            employeeColumn.setCellValueFactory(data -> {
                return new SimpleStringProperty(data.getValue());
            });
            // check if empty fields -> inform user
            if (cityField.getText().isEmpty())
                errorLbl.setText("Please, fill all the fields");
            //check if data is valid -> inform user
            else if (!check(cityField.getText()))
                errorLbl.setText("Please, enter a city, not a number");

            // get employees who attend a single restaurant
            else {

                Vector<String> rs = businessLogic.getEmployee1RestCity(cityField.getText());
                tblEmployee.getItems().clear();

                if (!rs.isEmpty()) {
                    tblEmployee.getItems().addAll(rs);
                } else {
                    tblEmployee.getItems().add("There is no such employee");
                }

            }
        } catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        } catch (UncompletedRequest e) {
            errorLbl.setText("Transaction could not be done. Please change the fields' information.");
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
