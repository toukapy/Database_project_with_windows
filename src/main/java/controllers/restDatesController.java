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
 * This class aims to deal with the window that handles getting the customers that attended all the cheapest trips
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class restDatesController implements Controller {

    private MainGUI datesWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblCustomer;
    @FXML
    private TableColumn<String,String> customerColumn;
    @FXML
    private Label errorLbl;
    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        datesWin = main;
    }

    /**
     * Method to initialize the information in the UI
     */
    @Override
    public void initializeInformation()  {
        try {
            customerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

            Vector<String> rs = businessLogic.restaurantDates();

            tblCustomer.getItems().clear();

            if (!rs.isEmpty()) {
                tblCustomer.getItems().addAll(rs);
            } else {
                tblCustomer.getItems().add("There is no such pairing");
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
        datesWin.showQuery();
    }
}