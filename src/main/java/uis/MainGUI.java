package uis;

import businessLogic.BlFacade;
import controllers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

/**
 * This class manages the GUI
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class MainGUI {

    private Window mainPageWin, queryPageWin,tranPageWin, allOptExcTripWin,maximumGainedWin,quantityCustomerWin;
    private Window restaurantFoodWin, addCustomerWin, deleteCustomerWin, swapGuidesWin, updateGuideWin, makeOrderWin, salesWin;
    private Window oneRestaurantCityWin,allCheapestTripsWin, allTripYear,guideAllLang, addPersonWin, deletePersonWin;
    private Window hotelCEOWin, riseWin, datesWin, tripDepWin, speakerWin;

    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;
    private String locale = "";

    /**
     * Constructor of the class
     */
    public MainGUI() {
        Platform.startup(() -> {
            try {
                init(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method sets the visibility of the window
     * @param b Boolean - sets the visibility
     */
    public void setVisibility(boolean b) {
        stage.close();
    }

    /**
     * A class that represents a window
     */
    class Window {
        Controller c;
        Parent ui;
    }

    /**
     * A method that loads the fxml file that corresponds to the current window to be shown
     *
     * @param fxmlfile String - the path to the fxml file
     * @return Window - the window that corresponds to the already loaded fxml file
     * @throws IOException
     */
    private Window load(String fxmlfile) throws IOException {
        Window window = new Window();
        loader = new FXMLLoader(MainGUI.class.getResource(fxmlfile));
        loader.setControllerFactory(controllerClass -> {

            if (controllerClass == MainPageController.class) {
                return new MainPageController();
            } else {
                // default behavior for controllerFactory:
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc); // fatal, just bail...
                }
            }
        });
        window.ui = loader.load();
        window.c = loader.getController();
        (window.c).setMainApp(this);

        return window;
    }

    /**
     * A method that initializes all windows using the load method
     *
     * @param stage Stage - Value of the current stage
     * @throws IOException
     */
    public void init(Stage stage) throws IOException {

        this.stage = stage;

        mainPageWin = load("/uis/mainPage.fxml");
        queryPageWin = load("/uis/queriesPage.fxml");
        tranPageWin = load("/uis/transactionsPage.fxml");
        allOptExcTripWin = load("/uis/customerAllOptExcTrip.fxml");
        maximumGainedWin = load("/uis/maximumGainsTrip.fxml");
        quantityCustomerWin = load("/uis/quantityCustomerGuide.fxml");
        restaurantFoodWin = load("/uis/restaurantFoodManager.fxml");
        oneRestaurantCityWin = load("/uis/oneRestaurantCity.fxml");
        allCheapestTripsWin = load("/uis/customerAllCheapestTrips.fxml");
        allTripYear = load("/uis/guideAllTripYear.fxml");
        guideAllLang = load("/uis/guideAllLanguages.fxml");
        addCustomerWin = load("/uis/addCustomer.fxml");
        deleteCustomerWin= load("/uis/deleteCustomer.fxml");
        swapGuidesWin= load("/uis/swapGuides.fxml");
        updateGuideWin= load("/uis/updateGuide.fxml");
        makeOrderWin= load("/uis/makeOrder.fxml");
        salesWin= load("/uis/sales.fxml");
        addPersonWin = load("/uis/addPerson.fxml");
        deletePersonWin = load("/uis/deletePerson.fxml");
        hotelCEOWin = load("/uis/hotelCEO.fxml");
        riseWin = load("/uis/rise.fxml");
        datesWin = load("/uis/restDates.fxml");
        tripDepWin = load("/uis/tripToDepartment.fxml");
        speakerWin = load("/uis/projectSpeakers.fxml");
        showMain();
    }
    /**
     * Method to show the main window
     */
    public void showMain() {
        setupScene(mainPageWin.ui, "Main", 655,398);
    }

    /**
     * Method to show the query selection window
     */
    public void showQuery(){ setupScene(queryPageWin.ui, "Queries",964,595);}

    /**
     * Method to show the transaction selection window
     */
    public void showTransaction(){ setupScene(tranPageWin.ui, "Transactions",964,653);}

    /**
     * Method to show the window that displays the customers that have been in all trips with optional excursions
     */
    public void showCustomerAllOptExc(){
        setupScene(allOptExcTripWin.ui, "Customers in all trips with optional excursions",814,595);
        allOptExcTripWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the trip that gained the maximum amount of money
     */
    public void showMaximumGainedTrip() {
        setupScene(maximumGainedWin.ui, "Maximum gained trip", 814, 595);
        maximumGainedWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the quantity of customers each guide has
     */
    public void showQuantityCustomer() {
        setupScene(quantityCustomerWin.ui, "Quantity of customers with guide", 814, 595);
        quantityCustomerWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the restaurants that offer food liked by all managers
     * and that have enough capacity for all of them
     */
    public void showRestaurantFoodManager()  {
        setupScene(restaurantFoodWin.ui, "Restaurants for managers", 814, 595);
        restaurantFoodWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the employees that frequent a single restaurant in a given city
     */
    public void showOneRestaurantCity() {
        setupScene(oneRestaurantCityWin.ui, "Employees that frequent a single restaurant in a city", 814, 575);
        oneRestaurantCityWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the customers that attended all cheapest trips
     */
    public void showAllCheapestTrips() {
        setupScene(allCheapestTripsWin.ui, "Customers in all cheapest trips", 814, 575);
        allCheapestTripsWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the guides that attended all trips of a given year
     */
    public void showGuideAllTripYear() {
        setupScene(allTripYear.ui, "Guides in all trips in a year", 814, 555);
        allTripYear.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the guides that speak all languages registered
     */
    public void showGuideAllLanguages()  {
        setupScene(guideAllLang.ui, "Guides speaking all languages", 814, 575);
        guideAllLang.c.initializeInformation();
    }

    /**
     * Method to show the window that allows us to add a customer to a trip with a hotel
     */
    public void showAddCustomer() {
        setupScene(addCustomerWin.ui, "Add customer", 952, 630);
        addCustomerWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows us to delete a customer from a trip
     */
    public void showDeleteCustomer() {
        setupScene(deleteCustomerWin.ui, "Delete customer", 814, 657);
        deleteCustomerWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows us to swap two guides between trips
     */
    public void showSwapGuides() {
        setupScene(swapGuidesWin.ui, "Swap guides", 814, 730);
        swapGuidesWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows us to update the guide of the trips in a given interval of time
     */
    public void showUpdateGuide()  {
            setupScene(updateGuideWin.ui, "Update guide", 814, 575);
        updateGuideWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows the user to add a person to the restaurants database
     */
    public void showAddPerson()  {
        setupScene(addPersonWin.ui, "Add Person", 814, 585);
        addPersonWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows the user to delete a person from the restaurants database
     */
    public void showDeletePerson() {
        setupScene(deletePersonWin.ui, "Delete Person", 814, 585);
        deletePersonWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows the user to make an order
     */
    public void showMakeOrder() {
        setupScene(makeOrderWin.ui, "Make order", 814, 585);
        makeOrderWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows the user to update a dish's price to its half
     */
    public void showSales() {
        setupScene(salesWin.ui, "Sales", 814, 575);
        salesWin.c.initializeInformation();
    }

    /**
     * Method to show the window that shows the employees who have gone to the same hotels as the CEO
     */
    public void showHotelsCEO() {
        setupScene(hotelCEOWin.ui, "CEO hotels", 814, 575);
        hotelCEOWin.c.initializeInformation();
    }

    /**
     * Method to show the window that shows the speakers for each project
     */
    public void showSpeakers() {
        setupScene(speakerWin.ui, "Project speakers", 814, 575);
        speakerWin.c.initializeInformation();
    }

    /**
     * Method to show the window that will execute the rise
     */
    public void showRise() {
        setupScene(riseWin.ui, "Employee raise", 814, 575);
        riseWin.c.initializeInformation();
    }

    /**
     * Method to show the window with the restaurant dates
     */
    public void showDates() {
        setupScene(datesWin.ui, "Restaurant dates", 814, 575);
        datesWin.c.initializeInformation();
    }

    /**
     * Method to execute the trip to department transaction
     */
    public void showTripToDep() {
        setupScene(tripDepWin.ui, "Trip to department", 952, 630);
        tripDepWin.c.initializeInformation();
    }

    /**
     * Method that sets the specified window and scene, and sets it visible for interaction
     *
     * @param ui     Parent - The window to show
     * @param title  String - The title to show in the head of the window
     * @param width  int - The width value of the window
     * @param height int - the height value of the window
     */
    private void setupScene(Parent ui, String title, int width, int height) {
        if (scene == null) {
            scene = new Scene(ui, width, height);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
        }
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle(title);
        scene.setRoot(ui);
        stage.show();
        ;
    }
}

