package businessLogic;

import dataAccess.DataManager;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;

/**
 * This interface represents the business logic
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public interface BlFacade {


/* CUSTOMERS-RELATED */

    /**
     * Trasaction 1 -> Delete a customer by phone and name from a trip
     * @param name String that represents the name
     * @param phoneNum String that represents the phone number
     * @param TripTo String that represents to where the trip is
     * @param DepartureDate String that represents the departure
     * @throws SQLException if rollback fails
     * @throws ParseException if the date is not valid
     */
    public void deleteCustomerFromTrip(String name, String phoneNum, String TripTo, String DepartureDate) throws SQLException, ParseException;

    /**
     * Query 1 -> Retrieves the trip that has obtained the highest amount of gains
     * @return the trip that has obtained the highest amount of gains
     * @throws SQLException if rollback fails
     */
    public Vector<String> getMaximumGainedTrip() throws SQLException;


    /**
     * Query 2 -> Retrieves the customers that have gone to every trip with optional excursion
     * @return the customers that have gone to every trip with optional excursion
     * @throws SQLException if rollback fails
     */
    public Vector<String> retrieveCustomerEveryTripExc() throws SQLException;

    /**
     * This method gets the customers who have attended at least all cheapest trips attended by customers
     * @return the customers who have attended at least all cheapest trips attended by customers
     */
    public Vector<String> getCustomersAllCheapestTrips();

    /**
     * Method that gets all customers from a trip, given the destination and the departure date
     *
     * @param trip String - The destination
     * @param departure String - The departure date
     * @return Vector<String> - The customers information
     * @throws SQLException if rollback fails
     * @throws ParseException if the provided date is not valid
     */
    public Vector<String> getCustomerTrip(String trip, String departure) throws SQLException, ParseException;

    /**
     * Method that gets a customer that satisfies all the restrictions given
     *
     * @param custname String - Name of the customer
     * @param custphone String - Phone number of the customer
     * @param hotelname String - Name of the hotel
     * @param hotelcity String - City where the hotel is
     * @param TripTo String - Destination of the trip
     * @param DepartureDate String - Departure date of the trip
     * @return Vector<String> Vector containing the customer (if it exists)
     * @throws SQLException if rollback fails
     */
    public Vector<String> getCustomerTripHotel(String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate) throws SQLException ;
    /**
     * Method to get the information about all customers in a trip and a hotel
     *
     * @return Vector<String> - A vector containing strings with such information
     */
    public Vector<String> getAllCustomers() ;

    /**
     * Method to get the information about a customer in a trip
     *
     * @return Vector<String> - Vector containing strings with such information
     */
    public Vector<String> getAllCustomersJustTrip();

    /**
     * Transaction 2 -> Method that adds a customer to a trip
     *
     *  @param choice String - The choice of creating or not creating a hotel, customer or trip if it does not exist
     * @param custname String - Name of the customer
     * @param custphone String - Phone number of the customer
     * @param hotelname String - Name of the hotel
     * @param hotelcity String - City where the hotel is
     * @param TripTo String - Destination of the trip
     * @param DepartureDate String - Departure date of the trip
     */
    public void addCustomerToTrip(String choice, String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate);


    /* TOUR-GUIDES RELATED */

    /**
     * This method provides the tour-guides who speak all languages registered in the database
     * @return the tour-guides who speak all languages registered in the database
     */
    public Vector<String> getTourguidesAllLanguages();

    /**
     * This method provides the tour-guides who have attended all trips of a given year.
     * @param year provided year
     */
    public Vector<String> getTourguidesAllTripsYear(String year);

    /**
     * Method to get all guides
     *
     * @return Vector<String> - A vector containing strings with that information
     */
    public Vector<String> getAllTourguideTrips();
    /**
     * Method to get the information of guides that are in trips (guides who do not have a trip are not going to appear here)
     *
     * @return Vector<String> - A vector containing strings with that information
     */
    public Vector<String> getAllTourguideTripsNotNull();

    /**
     * This method updates the tour-guide of the trips between two given dates.
     * @param tgprev previous tour-guide
     * @param tgnew new tour-guide to be set
     * @param date1 first date of the interval
     * @param date2 second date of the interval
     */
    public void updateTourguide(String tgprev, String tgnew, String date1, String date2);

    /**
     * Transition 3 -> Method that changes the guides between two trips
     *
     * @param guidename1 String - Name of first guide
     * @param guidephone1 String - Phone number of the first guide
     * @param TripTo1 String - Destination of the first trip
     * @param DepartureDate1 String - Departure date of the first trip
     * @param guidename2 String - Name of the second guide
     * @param guidephone2 String - Phone number of the second guide
     * @param TripTo2 String - Destination of the second trip
     * @param DepartureDate2 String - Departure date of the second trip
     */
    public void changeGuidesBetweenTrips(String guidename1, String guidephone1, String TripTo1, String DepartureDate1, String guidename2, String guidephone2, String TripTo2, String DepartureDate2) ;
    /**
     * Retrieve the number of customer each guide is responsible of
     * @return the number of customer each guide is responsible of
     */
    public Vector<String> retrieveNumCustomerGuideResponsible();

/* MENU AND MENU-ORDERS RELATED */

    /**
     * This method aims to provide all menu orders
     * @return all menu orders
     */
    public Vector<String> getAllMenuOrders();

    /**
     * This method adds a menu-order (to the restaurant database)
     * @param choice String that represents whether additional objects should be created (y) or not (!=y)
     * @param menu_mtype String that represents the type of menu
     * @param menu_id String that represents the menu identifier
     * @param name String that represents the name of the customer
     * @param customer_id String that represents the id of the customer
     */
    public void insertMenuOrder(String choice, String menu_mtype, String menu_id,  String name, String customer_id);



    /* PEOPLE RELATED (restaurant db) */

    /**
     * Method to insert a person (in the restaurant database)
     *
     * @param choice - Whether we want to create the objects if they do not exist
     * @param name - The name of the person
     * @param age - The age of the person
     * @param id - The id of the person
     * @param food - The dish the person likes
     * @param restaurant - The restaurant the person attends
     * @throws SQLException if rollback could not be done.
     */
    public void insertPerson(String choice, String name, String age, String id, String food, String restaurant) throws SQLException ;

    /**
     * This method deletes a person from the restaurants database
     * @param name String that represents the name of the person
     * @param id String that represents the id of the person
     * @throws SQLException if rollback could not be done
     */
    public void deletePerson(String name, String id) throws SQLException;

    /**
     * This method provides all the people that belong to the restaurants database
     * @return all the people that belong to the restaurants database
     */
    public Vector<String> getAllPeople();



    /* DISHES RELATED */

    /**
     * This method updates a given dishes' price to its half
     * @param dish provided dish
     */
    public void updateDishPrice(String dish);

    /**
     * Method to retrieve the information about dishes
     *
     * @return Vector<String> - A vector containing that information
     */
    public Vector<String> getAllDishes();



    /* RESTAURANT RELATED */

    /**
     * This method gets the restaurants that provide food liked by all managers
     * @return the restaurants that provide food liked by all managers
     */
    public Vector<String> getRestaurantLikedManagers();

    /* EMPLOYEES-RELATED*/

    /**
     * This method gets the employees who have attended a single restaurant of a given city
     * @param city provided city
     * @return the employees who have attended a single restaurant of a given city
     */
    public Vector<String> getEmployee1RestCity(String city);


}
