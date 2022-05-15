package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;

public class QueriesPageController implements Controller {

    private MainGUI queryPageWin;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        queryPageWin = main;
    }

    /**
     * Method to initialize the information in the UI
     * @throws SQLException
     */
    @Override
    public void initializeInformation() throws SQLException {

    }

    /**
     * Method to go to the window that returns all cheapest trips in the database
     * @throws SQLException
     */
    @FXML
    void onClickAllCheapestTrips() throws SQLException {
        queryPageWin.showAllCheapestTrips();
    }

    /**
     * MEthod to go to the window that retrieves all customers in trips with optional excursions
     * @throws SQLException
     */
    @FXML
    void onClickCustomerOptExcursion() throws SQLException {
        queryPageWin.showCustomerAllOptExc();
    }

    /**
     * Method to go to the window that retrieves the customers that frequent one restaurant in a city
     * @throws SQLException
     */
    @FXML
    void onClickFrequentsOneCity() throws SQLException {
        queryPageWin.showOneRestaurantCity();
    }

    /**
     * Method to go to the window that retrieves the guides that have gone to all trips in a year
     * @throws SQLException
     */
    @FXML
    void onClickGuideAllTripYear() throws SQLException {
        queryPageWin.showGuideAllTripYear();
    }

    /**
     * Method to go to the window that retrieves the guides with each of the quantity of customers
     * @throws SQLException
     */
    @FXML
    void onClickGuidesCustomers() throws SQLException {
        queryPageWin.showQuantityCustomer();
    }

    /**
     * Method to go to the window that retrieves all customers that have gone to a restaurant liked by all managers
     * @throws SQLException
     */
    @FXML
    void onClickLikedAllManagers() throws SQLException {
        queryPageWin.showRestaurantFoodManager();
    }

    /**
     * Method to go to the window that retrieves the trip with maximum gains
     * @throws SQLException
     */
    @FXML
    void onClickMaximumGains() throws SQLException {
        queryPageWin.showMaximumGainedTrip();
    }

    /**
     * Method to go to the window that retrieves guides that speak all languages
     * @throws SQLException
     */
    @FXML
    void onClickSpeakAllLanguages() throws SQLException {
        queryPageWin.showGuideAllLanguages();
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        queryPageWin.showMain();
    }

}
