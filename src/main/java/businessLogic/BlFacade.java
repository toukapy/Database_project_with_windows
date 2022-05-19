package businessLogic;


import exceptions.*;


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
     * Transaction 1 -> Delete a customer by phone and name from a trip
     * @param name String that represents the name
     * @param phoneNum String that represents the phone number
     * @param TripTo String that represents to where the trip is
     * @param DepartureDate String that represents the departure
     * @throws SQLException if database management fails
     * @throws ParseException if the date is not valid
     * @throws UncompletedRequest if transaction fails
     * @throws NotBelong if the person does not belong to the trip
     */
    void deleteCustomerFromTrip(String name, String phoneNum, String TripTo, String DepartureDate)
            throws SQLException, ParseException, UncompletedRequest, NotBelong;


    /**
     * Query 1 -> Retrieves the trip that has obtained the highest amount of gains
     * @return the trip that has obtained the highest amount of gains
     * @throws SQLException if database management fails
     */
    Vector<String> getMaximumGainedTrip() throws SQLException;



    /**
     * Query 2 -> Retrieves the customers that have gone to every trip with optional excursion
     * @return the customers that have gone to every trip with optional excursion
     * @throws SQLException if database management fails
     */
    Vector<String> retrieveCustomerEveryTripExc() throws SQLException;


    /**
     * This method gets the customers who have attended at least all cheapest trips attended by customers
     * @return the customers who have attended at least all cheapest trips attended by customers
     * @throws SQLException if database management fails
     */
    Vector<String> getCustomersAllCheapestTrips() throws  SQLException;


    /**
     * Method that gets all customers from a trip, given the destination and the departure date
     *
     * @param trip String - The destination
     * @param departure String - The departure date
     * @return Vector<String> - The customers information
     * @throws SQLException if rollback fails
     * @throws ParseException if the provided date is not valid
     */
    Vector<String> getCustomerTrip(String trip, String departure) throws SQLException, ParseException;

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
     * @throws SQLException if database management fails
     * @throws ParseException if the date is not valid
     */
    Vector<String> getCustomerTripHotel(String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate)
            throws SQLException, ParseException;
    /**
     * Method to get the information about all customers in a trip and a hotel
     *
     * @return Vector<String> - A vector containing strings with such information
     * @throws SQLException if database management fails
     */
    Vector<String> getAllCustomers() throws SQLException;


    /**
     * Method to get the information about a customer in a trip
     *
     * @return Vector<String> - Vector containing strings with such information
     * @throws SQLException if database management fails
     */
    Vector<String> getAllCustomersJustTrip() throws SQLException;


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
     * @throws ObjectNotCreated if transaction cannot be completed because of non-created objects
     * @throws UncompletedRequest if transaction is not successful
     * @throws SQLException if database management fails
     * @throws ParseException if the date is not valid
     */
    void addCustomerToTrip(String choice, String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate)
            throws ObjectNotCreated, UncompletedRequest, SQLException, ParseException;



    /* TOUR-GUIDES RELATED */

    /**
     * This method provides the tour-guides who speak all languages registered in the database
     * @return the tour-guides who speak all languages registered in the database
     * @throws SQLException if database management fails
     */
    Vector<String> getTourguidesAllLanguages() throws SQLException;


    /**
     * This method provides the tour-guides who have attended all trips of a given year.
     * @param year provided year
     * @throws SQLException if database management fails
     */
    Vector<String> getTourguidesAllTripsYear(String year) throws SQLException;


    /**
     * Method to get all guides
     *
     * @return Vector<String> - A vector containing strings with that information
     * @throws SQLException if database management fails
     */
    Vector<String> getAllTourguideTrips() throws SQLException;

    /**
     * Method to get the information of guides that are in trips (guides who do not have a trip are not going to appear here)
     *
     * @return Vector<String> - A vector containing strings with that information
     * @throws SQLException if database management fails
     */
     Vector<String> getAllTourguideTripsNotNull() throws SQLException;


    /**
     * This method updates the tour-guide of the trips between two given dates.
     * @param tgprev previous tour-guide
     * @param tgnew new tour-guide to be set
     * @param date1 first date of the interval
     * @param date2 second date of the interval
     * @throws UncompletedRequest if transaction fails
     * @throws SQLException if database management fails
     * @throws NoChange if no rows are updated
     * @throws NotBelong if tour-guides don't belong to the database
     * @throws ParseException if the date is not valid
     */
    void updateTourguide(String tgprev, String tgnew, String date1, String date2)
            throws UncompletedRequest, SQLException, NoChange, NotBelong, ParseException;

    /**
     * Transition 3 -> Method that changes the guides between two trips
     *
     *
     * @param choice
     * @param guidename1 String - Name of first guide
     * @param guidephone1 String - Phone number of the first guide
     * @param TripTo1 String - Destination of the first trip
     * @param DepartureDate1 String - Departure date of the first trip
     * @param guidename2 String - Name of the second guide
     * @param guidephone2 String - Phone number of the second guide
     * @param TripTo2 String - Destination of the second trip
     * @param DepartureDate2 String - Departure date of the second trip
     * @throws UncompletedRequest if the transaction was not successful
     * @throws SQLException if database management fails
     * @throws ParseException if the date is not valid
     */
    void changeGuidesBetweenTrips(String choice, String guidename1, String guidephone1, String TripTo1, String DepartureDate1, String guidename2, String guidephone2, String TripTo2, String DepartureDate2)
            throws UncompletedRequest, SQLException, ParseException;

    /**
     * Retrieve the number of customer each guide is responsible for
     * @return the number of customer each guide is responsible for
     * @throws SQLException if database management fails
     */
    Vector<String> retrieveNumCustomerGuideResponsible() throws SQLException;


/* MENU AND MENU-ORDERS RELATED */

    /**
     * This method aims to provide all menu orders
     * @return all menu orders
     * @throws SQLException if database management fails
     */
    Vector<String> getAllMenuOrders() throws SQLException;


    /**
     * This method adds a menu-order (to the restaurant database)
     * @param choice String that represents whether additional objects should be created (y) or not (!=y)
     * @param menu_mtype String that represents the type of menu
     * @param menu_id String that represents the menu identifier
     * @param customer_id String that represents the id of the customer
     * @throws UncompletedRequest if transaction could not be completed
     * @throws SQLException if database management failed
     */
    void insertMenuOrder(String choice, String menu_mtype, String menu_id, String customer_id)
            throws UncompletedRequest, SQLException;




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
     * @throws UncompletedRequest if transaction could not be completed
     */
    void insertPerson(String choice, String name, String age, String id, String food, String restaurant)
            throws SQLException, UncompletedRequest;


    /**
     * This method deletes a person from the restaurants database
     * @param name String that represents the name of the person
     * @param id String that represents the id of the person
     * @throws SQLException if rollback could not be done
     * @throws UncompletedRequest if the transaction could not be completed
     * @throws NotBelong if the person does not belong to the database
     */
    void deletePerson(String name, String id) throws SQLException, UncompletedRequest, NotBelong;


    /**
     * This method provides all the people that belong to the restaurants database
     * @return all the people that belong to the restaurants database
     * @throws SQLException if database management fails
     */
    Vector<String> getAllPeople() throws SQLException;




    /* DISHES RELATED */

    /**
     * This method updates a given dishes' price to its half
     * @param dish provided dish
     * @throws SQLException if database management fails
     * @throws UncompletedRequest if the transaction was not successful
     * @throws NoChange if no changes were made (serves table)
     * @throws NotBelong if the dish does not belong to the database (dish table)
     */
    void updateDishPrice(String dish) throws SQLException, UncompletedRequest, NoChange, NotBelong;


    /**
     * Method to retrieve the information about dishes
     *
     * @return Vector<String> - A vector containing that information
     * @throws SQLException if database management fails
     */
    Vector<String> getAllDishes() throws SQLException;




    /* RESTAURANT RELATED */

    /**
     * This method gets the restaurants that provide food liked by all managers
     * @return the restaurants that provide food liked by all managers
     * @throws SQLException if database management fails
     */
    Vector<String> getRestaurantLikedManagers() throws SQLException;

    /**
     * This query retrieves couples of names and a restaurant of people who frequent the same
     * restaurant, have at least a liked dish in common and that restaurant serves it
     * @return the vestor with the pairings and restaurant
     */
    Vector<String> restaurantDates() throws SQLException;

    /* EMPLOYEES-RELATED*/

    /**
     * This method gets the employees who have attended a single restaurant of a given city
     * @param city provided city
     * @return the employees who have attended a single restaurant of a given city
     * @throws SQLException if an error occurred when managing the database
     */
    Vector<String> getEmployee1RestCity(String city) throws SQLException;

    /**
     * This method gives a raise of 1000 dollars to the employees who are paid less than
     * the average salary of the company. Also, if they have any dependent they get 100 dollars
     * per dependent
     */
    void risesForEmployees() throws SQLException;



    /**
     * For choosing from dept and locations for "bookTripToDepartment"
     * @return all the departments and locations
     */
    public Vector<String> getDepartmentsAndLocations();

    /**
     * This method retrieves the employees that have been to the same hotels as the CEO
     * @return the info about the employees
     * @throws SQLException
     */
    Vector<String> hotelsCEO() throws SQLException;

    /**
     * Method to get the ssn-s and salaries of all employees in the database
     * @return the ssn and salary of all employees
     * @throws SQLException
     */
    Vector<String> getAllSalaries() throws SQLException;

    /**
     * Get the employees who have worked the most hours in each project and the manager of the department responsible for the project
     * @return The employee name, last name, project name, the hours worked and the department managers full name
     * @throws SQLException
     */
    Vector<String> getSpeakers() throws SQLException;

    /**
     * Book a trip to the employees not in the given department to the location of that department
     * in the given date
     * @param Dno the department the trip is to
     * @param location where the trip is to
     * @param date what date the trip is
     * @return
     */
    void bookTripToDepartment(String Dno, String location, String date) throws SQLException, UncompletedRequest, NoHotel, ParseException;

    /**
     * Get all the hotel customer trips that are to and in the date recieved, and the people going are employees
     * @param location where the trip is to
     * @param date the trip date
     * @return the ssn and full name of the employee, the trip location and date and the hotel id
     * @throws SQLException
     */
    Vector<String> getTrips(String location, String date) throws SQLException;



}
