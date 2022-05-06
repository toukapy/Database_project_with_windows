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
    void onClickAddCustomer(){
        tranPageWin.showAddCustomer();
    }
    @FXML
    void onClickDeleteCustomer(){
        tranPageWin.showDeleteCustomer();
    }
    @FXML
    void onClickSwapGuides(){
        tranPageWin.showSwapGuides();
    }
    @FXML
    void onClickUpdateTourguide(){
        tranPageWin.showUpdateGuide();
    }
    @FXML
    void onClickMakeOrder(){
        tranPageWin.showMakeOrder();
    }

    @FXML
    void onClickSales(){tranPageWin.showSales(); }
    @FXML
    void onClickBack(){
        tranPageWin.showMain();
    }

}
