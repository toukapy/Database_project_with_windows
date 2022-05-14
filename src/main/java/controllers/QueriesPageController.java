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
    void onClickAllCheapestTrips() throws SQLException {
        queryPageWin.showAllCheapestTrips();
    }

    @FXML
    void onClickCustomerOptExcursion() throws SQLException {
        queryPageWin.showCustomerAllOptExc();
    }

    @FXML
    void onClickFrequentsOneCity(){
        queryPageWin.showOneRestaurantCity();
    }

    @FXML
    void onClickGuideAllTripYear() throws SQLException {
        queryPageWin.showGuideAllTripYear();
    }

    @FXML
    void onClickGuidesCustomers() throws SQLException {
        queryPageWin.showQuantityCustomer();
    }

    @FXML
    void onClickLikedAllManagers() throws SQLException {
        queryPageWin.showRestaurantFoodManager();
    }

    @FXML
    void onClickMaximumGains() throws SQLException {
        queryPageWin.showMaximumGainedTrip();
    }

    @FXML
    void onClickSpeakAllLanguages() throws SQLException {
        queryPageWin.showGuideAllLanguages();
    }

    @FXML
    void onClickBack(){
        queryPageWin.showMain();
    }

}
