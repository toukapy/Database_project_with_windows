package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;

public class QueriesPageController implements Controller {

    private MainGUI queryPageWin;

    @Override
    public void setMainApp(MainGUI main) {
        queryPageWin = main;
    }

    @FXML
    void onClickAllCheapestRestaurants() {

    }

    @FXML
    void onClickCustomerOptExcursion() {
        queryPageWin.showCustomerAllOptExc();
    }

    @FXML
    void onClickFrequentsOneCity() {

    }

    @FXML
    void onClickGuideAllTripYear() {

    }

    @FXML
    void onClickGuidesCustomers() {

    }

    @FXML
    void onClickLikedAllManagers() {

    }

    @FXML
    void onClickMaximumGains() {

    }

    @FXML
    void onClickSpeakAllLanguages() {

    }

}
