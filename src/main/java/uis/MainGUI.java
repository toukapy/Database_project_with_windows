package uis;

import businessLogic.BlFacadeImplementation;
import controllers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.SQLException;

public class MainGUI {

    private Window mainPageWin, queryPageWin,tranPageWin, allOptExcTripWin,maximumGainedWin,quantityCustomerWin;
    private Window restaurantFoodWin, addCustomerWin, deleteCustomerWin, swapGuidesWin, updateGuideWin, makeOrderWin, salesWin;
    private Window oneRestaurantCityWin,allCheapestTripsWin, allTripYear,guideAllLang, addPersonWin, deletePersonWin;

    private BlFacadeImplementation businessLogic;
    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;
    private String locale = "";

    /**
     * Contructor of the class
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
     * A method that loads the fxmlfile that corresponds to the current window to be shown
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
        showMain();
    }
    /**
     * Method to show the main window
     */
    public void showMain() {
        setupScene(mainPageWin.ui, "Main", 814,555);
    }

    /**
     * Method to show the query selection window
     */
    public void showQuery(){ setupScene(queryPageWin.ui, "Queries",814,555);}

    /**
     * Method to show the transaction selection window
     */
    public void showTransaction(){ setupScene(tranPageWin.ui, "Transactions",814,555);}

    /**
     * Method to show the window that displays the customers that have been in all optional excursions
     */
    public void showCustomerAllOptExc() throws SQLException {
        setupScene(allOptExcTripWin.ui, "All optional excursion trip",814,555);
        allOptExcTripWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the trip that gained the maximum amount of money
     */
    public void showMaximumGainedTrip() throws SQLException {
        setupScene(maximumGainedWin.ui, "Maximum gained trip", 814, 555);
        maximumGainedWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the quantity of customers each guide has
     */
    public void showQuantityCustomer() throws SQLException {
        setupScene(quantityCustomerWin.ui, "Quantity of customers with guide", 814, 555);
        quantityCustomerWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the restaurants that offer food liked by all managers
     */
    public void showRestaurantFoodManager() throws SQLException {
        setupScene(restaurantFoodWin.ui, "Restaurants' food like by manager", 814, 555);
        restaurantFoodWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the employees that frequent a single restaurant in a given city
     */
    public void showOneRestaurantCity() throws SQLException {
        setupScene(oneRestaurantCityWin.ui, "Employees that frequent just one restaurant in a city", 814, 555);
        oneRestaurantCityWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the customers that attended all cheapest trips
     */
    public void showAllCheapestTrips() throws SQLException {
        setupScene(allCheapestTripsWin.ui, "Customers in all cheapest trips", 814, 555);
        allCheapestTripsWin.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the guides that attended all trips of a given year
     */
    public void showGuideAllTripYear() throws SQLException {
        setupScene(allTripYear.ui, "Guides in all trips in a year", 814, 555);
        allTripYear.c.initializeInformation();
    }

    /**
     * Method to show the window that displays the guides that speak all languages registered
     */
    public void showGuideAllLanguages() throws SQLException {
        setupScene(guideAllLang.ui, "Guides speaks all languages", 814, 555);
        guideAllLang.c.initializeInformation();
    }

    /**
     * Method to show the window that allows us to add a customer to a trip with a hotel
     */
    public void showAddCustomer() throws SQLException {
        setupScene(addCustomerWin.ui, "Add customer", 814, 615);
        addCustomerWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows us to delete a customer from a trip
     */
    public void showDeleteCustomer() throws SQLException {
        setupScene(deleteCustomerWin.ui, "Delete customer", 814, 555);
        deleteCustomerWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows us to swap two guides between trips
     */
    public void showSwapGuides() throws SQLException {
        setupScene(swapGuidesWin.ui, "Swap guides", 814, 555);
        swapGuidesWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows us to update the guide of the trips in a given interval of time
     */
    public void showUpdateGuide() throws SQLException {
            setupScene(updateGuideWin.ui, "Update guide", 814, 555);
        updateGuideWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows the user to add a person to the restaurant database
     */
    public void showAddPerson() throws SQLException {
        setupScene(addPersonWin.ui, "Add Person", 814, 555);
        addPersonWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows the user to delete a person from the restaurant database
     */
    public void showDeletePerson() throws SQLException {
        setupScene(deletePersonWin.ui, "Delete Person", 814, 555);
        deletePersonWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows the user to make an order
     */
    public void showMakeOrder() throws SQLException {
        setupScene(makeOrderWin.ui, "Make order", 814, 555);
        makeOrderWin.c.initializeInformation();
    }

    /**
     * Method to show the window that allows the user to update a dish's price to its half
     */
    public void showSales() throws SQLException {
        setupScene(salesWin.ui, "Sales", 814, 555);
        salesWin.c.initializeInformation();
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

