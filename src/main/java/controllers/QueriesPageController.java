package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;

public class QueriesPageController implements Controller {

    private MainGUI queryPageWin;

    @Override
    public void setMainApp(MainGUI main) {
        queryPageWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {

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
    void onClickMaximumGains() throws SQLException {
        queryPageWin.showMaximumGainedTrip();
    }

    @FXML
    void onClickSpeakAllLanguages() {

    }

}
