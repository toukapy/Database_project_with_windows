package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;


/**
 * This class aims to deal with the window that displays the available queries
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
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
     */
    @Override
    public void initializeInformation(){

    }

    /**
     * Method to go to the window that returns the customers that have attended all cheapest trips in the database
     */
    @FXML
    void onClickAllCheapestTrips()  {
        queryPageWin.showAllCheapestTrips();
    }

    /**
     * Method to go to the window that retrieves all customers in trips with optional excursions
     */
    @FXML
    void onClickCustomerOptExcursion()  {
        queryPageWin.showCustomerAllOptExc();
    }

    /**
     * Method to go to the window that retrieves the customers that frequent only one restaurant in a city
     */
    @FXML
    void onClickFrequentsOneCity() {
        queryPageWin.showOneRestaurantCity();
    }

    /**
     * Method to go to the window that retrieves the guides that have gone to all trips in a given year
     */
    @FXML
    void onClickGuideAllTripYear() {
        queryPageWin.showGuideAllTripYear();
    }

    /**
     * Method to go to the window that retrieves the guides with each of the quantity of customers
     */
    @FXML
    void onClickGuidesCustomers() {
        queryPageWin.showQuantityCustomer();
    }

    /**
     * Method to go to the window that retrieves the restaurants that offer food liked by all managers
     */
    @FXML
    void onClickLikedAllManagers()  {
        queryPageWin.showRestaurantFoodManager();
    }

    /**
     * Method to go to the window that retrieves the trip with maximum gains
     */
    @FXML
    void onClickMaximumGains() {
        queryPageWin.showMaximumGainedTrip();
    }

    /**
     * Method to go to the window that retrieves guides that speak all languages registered in the trip database
     */
    @FXML
    void onClickSpeakAllLanguages()  {
        queryPageWin.showGuideAllLanguages();
    }

    /**
     * Method to go to the window that shows the employees who have gone to the same hotels as the CEO
     */
    @FXML
    void onClickHotelCEO() { queryPageWin.showHotelsCEO();}

    /**
     * Method to go to the window that shows the employees who have gone to the same hotels as the CEO
     */
    @FXML
    void onClickDates() { queryPageWin.showDates(); }

    /**
     * Method to go to the window that shows the speakers for each project
     */
    @FXML
    void onClickSpeakers() { queryPageWin.showSpeakers(); }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        queryPageWin.showMain();
    }

}
