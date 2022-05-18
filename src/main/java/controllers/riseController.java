package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.util.Vector;

/**
 * This class aims to deal with the window that handles setting a dish to half its price
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class riseController implements Controller {

    private MainGUI mainWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> empTable;
    @FXML
    private TableColumn<String, String> col;



    @FXML
    private Label errorLbl;
    @FXML
    private Label correctLbl;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        mainWin = main;
    }

    /**
     * Method to initialize the information in the UI
     */
    @Override
    public void initializeInformation() {
        fillTable();
        resetFields();
    }

    /**
     * Method to reset the different fields in the UI
     */
    private void resetFields(){
        errorLbl.setText("");
        correctLbl.setText("");
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }

    /**
     * This method updates the price of a given dish to its half
     */
    @FXML
    void onClickExecute() {
        errorLbl.setText("");
        correctLbl.setText("");

        try {

            businessLogic.risesForEmployees();
            correctLbl.setText("Transaction executed!!");
            fillTable();

        } catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }
    }


    /**
     * Method to fill the table with the initial information
     */
    private void fillTable() {
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        // clear table
        empTable.getItems().clear();

        try {
            // fill table with current employees in the database
            Vector<String> rs = businessLogic.getAllSalaries();
            if (!rs.isEmpty()) {
                empTable.getItems().addAll(rs);
            } else {
                empTable.getItems().add("No employees in the database");
            }
        } catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }
    }
}
