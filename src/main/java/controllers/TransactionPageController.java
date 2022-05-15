package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;

public class TransactionPageController implements Controller {

    private MainGUI tranPageWin;

    @Override
    public void setMainApp(MainGUI main) {
        tranPageWin = main;
    }

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
    @FXML
    void onClickBack(){
        tranPageWin.showMain();
    }

}
