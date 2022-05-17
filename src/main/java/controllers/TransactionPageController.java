package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
/**
 * This class aims to deal with the window that displays the available transactions
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class TransactionPageController implements Controller {

    private MainGUI tranPageWin;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        tranPageWin = main;
    }

    /**
     * Method to initialize the information in the UI
     */
    @Override
    public void initializeInformation()  {

    }

    /**
     * Method to go to the window where a customer can be added (to the trip database)
     * @throws SQLException
     */
    @FXML
    void onClickAddCustomer() throws SQLException {
        tranPageWin.showAddCustomer();
    }
    /**
     * Method to go to the window where a customer can be deleted (from the trip database)
     * @throws SQLException
     */
    @FXML
    void onClickDeleteCustomer() throws SQLException {tranPageWin.showDeleteCustomer(); }
    /**
     * Method to go to the window where guides can be swapped between trips
     * @throws SQLException
     */
    @FXML
    void onClickSwapGuides() throws SQLException {
        tranPageWin.showSwapGuides();
    }
    /**
     * Method to go to the window where guides can be updated in a given interval of time
     * @throws SQLException
     */
    @FXML
    void onClickUpdateTourguide() throws SQLException {
        tranPageWin.showUpdateGuide();
    }
    /**
     * Method to go to the window where an order can be made
     * @throws SQLException
     */
    @FXML
    void onClickMakeOrder() throws SQLException {
        tranPageWin.showMakeOrder();
    }
    /**
     * Method to go to the window where a person can be deleted (from the restaurant database)
     * @throws SQLException
     */
    @FXML
    void onClickDeletePerson() throws SQLException {
        tranPageWin.showDeletePerson();
    }

    /**
     * Method to go to the window where a person can be added (to the restaurant database)
     * @throws SQLException
     */
    @FXML
    void onClickAddPerson() throws SQLException {
        tranPageWin.showAddPerson();
    }
    /**
     * Method to go to the window where a dish can be set to half its price
     * @throws SQLException
     */
    @FXML
    void onClickSales() throws SQLException {tranPageWin.showSales(); }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        tranPageWin.showMain();
    }

}
