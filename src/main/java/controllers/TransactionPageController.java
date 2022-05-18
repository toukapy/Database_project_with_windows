package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;


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
     */
    @FXML
    void onClickAddCustomer() {
        tranPageWin.showAddCustomer();
    }
    /**
     * Method to go to the window where a customer can be deleted (from the trip database)
     */
    @FXML
    void onClickDeleteCustomer() {tranPageWin.showDeleteCustomer(); }
    /**
     * Method to go to the window where guides can be swapped between trips
     */
    @FXML
    void onClickSwapGuides()  {
        tranPageWin.showSwapGuides();
    }
    /**
     * Method to go to the window where guides can be updated in a given interval of time
     */
    @FXML
    void onClickUpdateTourguide(){
        tranPageWin.showUpdateGuide();
    }
    /**
     * Method to go to the window where an order can be made
     */
    @FXML
    void onClickMakeOrder() {
        tranPageWin.showMakeOrder();
    }
    /**
     * Method to go to the window where a person can be deleted (from the restaurant database)
     */
    @FXML
    void onClickDeletePerson()  {
        tranPageWin.showDeletePerson();
    }

    /**
     * Method to go to the window where a person can be added (to the restaurant database)
     */
    @FXML
    void onClickAddPerson()  {
        tranPageWin.showAddPerson();
    }
    /**
     * Method to go to the window where a dish can be set to half its price
     */
    @FXML
    void onClickSales() {tranPageWin.showSales(); }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        tranPageWin.showMain();
    }

}
