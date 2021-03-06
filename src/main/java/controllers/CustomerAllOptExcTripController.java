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
 * This class aims to deal with the window that handles getting the customers that attended all trips with optional excursions
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class CustomerAllOptExcTripController implements Controller {

    private MainGUI allOptExcWin;
    private BlFacade businessLogic = new BlFacadeImplementation();


    @FXML
    private TableView<String> customerTable;
    @FXML
    private TableColumn<String, String> idColumn;
    @FXML
    private Label errorLbl;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        allOptExcWin = main;
    }

    /**
     * Method to initialize the information in the UI
     */
    @Override
    public void initializeInformation() {
        try {
            idColumn.setCellValueFactory(data -> {
                return new SimpleStringProperty(data.getValue());
            });

            Vector<String> rs = businessLogic.retrieveCustomerEveryTripExc();

            customerTable.getItems().clear();

            if (rs.isEmpty()) {
                System.out.println("There is no such customer in the database!!");
                customerTable.getItems().add("There is no such customer!!");
            } else {
                customerTable.getItems().addAll(rs);
            }
        } catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }


    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        allOptExcWin.showQuery();
    }




}
