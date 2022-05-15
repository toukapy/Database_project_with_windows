package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;

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
     * @throws SQLException
     */
    @Override
    public void initializeInformation() throws SQLException {

    }


    @FXML
    void onClickAddCustomer() throws SQLException {
        tranPageWin.showAddCustomer();
    }
    @FXML
    void onClickDeleteCustomer() throws SQLException {tranPageWin.showDeleteCustomer(); }
    @FXML
    void onClickSwapGuides() throws SQLException {
        tranPageWin.showSwapGuides();
    }
    @FXML
    void onClickUpdateTourguide() throws SQLException {
        tranPageWin.showUpdateGuide();
    }
    @FXML
    void onClickMakeOrder() throws SQLException {
        tranPageWin.showMakeOrder();
    }

    @FXML
    void onClickDeletePerson() throws SQLException {
        tranPageWin.showDeletePerson();
    }
    @FXML
    void onClickAddPerson() throws SQLException {
        tranPageWin.showAddPerson();
    }

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
