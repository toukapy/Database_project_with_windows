package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import exceptions.UncompletedRequest;
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
public class CustomerAllCheapestTripsController implements Controller {

    private MainGUI allCheapestTripWin;
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
        allCheapestTripWin = main;
    }

    /**
     * Method to initialize the information in the UI
     */
    @Override
    public void initializeInformation()  {
        try {
            customerColumn.setCellValueFactory(data -> {
                return new SimpleStringProperty(data.getValue());
            });

            Vector<String> rs = businessLogic.getCustomersAllCheapestTrips();

            tblCustomer.getItems().clear();

            if (!rs.isEmpty()) {
                tblCustomer.getItems().addAll(rs);
            } else {
                tblCustomer.getItems().add("There is no such customer");
            }
        } catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        } catch (UncompletedRequest e){
            errorLbl.setText("Transaction could not be done. Please change the fields' information.");
        }

    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        allCheapestTripWin.showQuery();
    }
}
