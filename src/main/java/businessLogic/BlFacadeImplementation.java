package businessLogic;

import dataAccess.DataManager;
import exceptions.ObjectNotCreated;
import exceptions.UncompletedRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;

/**
 * This class represents the implementation of the business logic interface
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class BlFacadeImplementation implements BlFacade{

    private DataManager dbManager = new DataManager();

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
    @Override
    public void deleteCustomerFromTrip(String name, String phoneNum, String TripTo, String DepartureDate) throws SQLException, ParseException, UncompletedRequest {

        dbManager.open();

        ResultSet customer = dbManager.getCustomer(name,phoneNum);

        if(customer == null){
            System.out.println("There is no such person in the database!!");
        }else{
            while(customer.next()) {
                if(dbManager.customerExistsInTripWithoutHotel(customer.getString("CustomerId"),TripTo,DepartureDate)){
                    dbManager.deleteCustomerFromTrip(customer.getString("CustomerId"), TripTo, DepartureDate);
                }else{
                    System.out.println("Customer does not exist in trip");
                }
            }
        }

        dbManager.close();
    }


    /**
     * Query 1 -> Retrieves the trip that has obtained the highest amount of gains
     * @return the trip that has obtained the highest amount of gains
     * @throws SQLException if rollback fails
     */
    @Override
    public Vector<String> getMaximumGainedTrip() throws SQLException, UncompletedRequest {
        Vector<String> answer = new Vector<>();
        dbManager.open();

        ResultSet trip = dbManager.getMaximumGainedTrip();
        if(trip.next()){
            answer.add("Destination: "+ trip.getString("TripTo") +", Departure date: "+ trip.getString("DepartureDate"));
            System.out.println("Destination: "+ trip.getString("TripTo") +", Departure date: "+ trip.getString("DepartureDate"));
        }
        dbManager.close();
        return answer;

    }


    /**
     * Query 2 -> Retrieves the customers that have gone to every trip with optional excursion
     * @return the customers that have gone to every trip with optional excursion
     * @throws SQLException if rollback fails
     */
    @Override
    public Vector<String> retrieveCustomerEveryTripExc() throws SQLException, UncompletedRequest {

        Vector<String> answer = new Vector<String>();
        dbManager.open();
        ResultSet customers = dbManager.retrieveCustomerEveryTripExc();

        while(customers.next()){
            answer.add("CustomerId: "+ customers.getString("CustomerId") + ", name: " + customers.getString("custname") + ", phone: "+ customers.getString("custphone"));
        }
        dbManager.close();
        return answer;
    }
    /**
     * This method gets the customers who have attended at least all cheapest trips attended by customers
     * @return the customers who have attended at least all cheapest trips attended by customers
     */
    @Override
    public Vector<String> getCustomersAllCheapestTrips() throws UncompletedRequest, SQLException {

        Vector<String> answer = new Vector<>();

        dbManager.open();

        ResultSet customers = dbManager.getCustomersAllCheapestTrips();
        while (customers.next()) {
            System.out.println("Customerid: " + customers.getString("id") + ", Name: " + customers.getString("name"));
            answer.add("Customerid: " + customers.getString("id") + ", Name: " + customers.getString("name"));
        }

        dbManager.close();


        return answer;
    }


    /**
     * Method that gets all customers from a trip, given the destination and the departure date
     *
     * @param trip String - The destination
     * @param departure String - The departure date
     * @return Vector<String> - The customers information
     * @throws SQLException if rollback fails
     * @throws ParseException if the provided date is not valid
     */
    @Override
    public Vector<String> getCustomerTrip(String trip, String departure) throws SQLException, ParseException {
        Vector<String> answer = new Vector<>();
        dbManager.open();
        ResultSet rs = dbManager.getCustomerTrip(trip,departure);

        while(rs.next()){
            answer.add("Customer: "+rs.getString("custname")+ ", Phone: " + rs.getString("custphone"));
        }

        dbManager.close();

        return answer;
    }

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
    @Override
    public Vector<String> getCustomerTripHotel(String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate) throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();
        ResultSet rs = dbManager.getCustomerTripHotel(custname,custphone,hotelname,hotelcity,TripTo,DepartureDate);
        if(rs == null){
            return null;
        }
        while(rs.next()){
            answer.add("Customer: "+rs.getString("CustomerId")+", HotelId: "+rs.getString("HotelId")+", Destination: "+rs.getString("TripTo")+", Departure date: "+rs.getString("DepartureDate"));
        }
        dbManager.close();

        return answer;
    }


    /**
     * Method to get the information about all customers in a trip and a hotel
     *
     * @return Vector<String> - A vector containing strings with such information
     */
    @Override
    public Vector<String> getAllCustomers() throws SQLException {

        Vector<String> answer = new Vector<>();

        dbManager.open();
        ResultSet rs = dbManager.getAllCustomers();
        if (rs == null) {
            return null;
        }
        while (rs.next()) {
            System.out.println("Destination: " + rs.getString("TripTo") + ",  Departure date: " + rs.getString("DepartureDate") + ",  Hotel name: " + rs.getString("hotelname") + ",  Hotel city:" + rs.getString("hotelcity") + ",  Customer name: " + rs.getString("custname") + ",  Customer phone: " + rs.getString("custphone"));
            answer.add("Destination: " + rs.getString("TripTo") + ",  Departure date: " + rs.getString("DepartureDate") + ",  Hotel name: " + rs.getString("hotelname") + ",  Hotel city:" + rs.getString("hotelcity") + ",  Customer name: " + rs.getString("custname") + ",  Customer phone: " + rs.getString("custphone"));
        }
        dbManager.close();

        return answer;

    }

    /**
     * Method to get the information about a customer in a trip
     *
     * @return Vector<String> - Vector containing strings with such information
     */
    @Override
    public Vector<String> getAllCustomersJustTrip() throws SQLException {

        Vector<String> answer = new Vector<>();

        dbManager.open();
        ResultSet rs = dbManager.getAllCustomersJustTrip();
        if (rs == null) {
            return null;
        }
        while (rs.next()) {
            System.out.println("Destination: " + rs.getString("TripTo") + ",  Departure date: " + rs.getString("DepartureDate") );
            answer.add("Destination: " + rs.getString("TripTo") + ",  Departure date: " + rs.getString("DepartureDate"));
        }
        dbManager.close();

        return answer;

    }

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
    @Override
    public void addCustomerToTrip(String choice, String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate) throws ObjectNotCreated, UncompletedRequest, SQLException {

        dbManager.open();


        // check if customer exists -> create if must
        ResultSet customer = dbManager.getCustomer(custname, custphone);
        if(!customer.next()){
            System.out.println("The customer does not exist");
            if(choice.equals("y")){
                System.out.println("Creating a new customer with that data");
                dbManager.insertCustomer(custname, custphone);
                customer = dbManager.getCustomer(custname, custphone);
                customer.next();
            }else {
                throw new ObjectNotCreated();
            }
        }

        // check if trip exist ->  create if must
        ResultSet trip = dbManager.getTrip(TripTo, DepartureDate);
        if(!trip.next()) {
            System.out.println("the trip does not exist");
            if(choice.equals("y")){
                System.out.println("Creating a new trip with the data");
                dbManager.insertTrip(TripTo, DepartureDate);
            }else{
                throw new ObjectNotCreated();
            }
        }

        // check if hotel exists -> create if must
        ResultSet hotel = dbManager.getHotel(hotelname, hotelcity);
        if(!hotel.next()){
            System.out.println("The hotel does not exist");

            if(choice.equals("y")){
                System.out.println("Creating a new hotel with the data");
                dbManager.insertHotel(hotelname, hotelcity);
                hotel = dbManager.getHotel(hotelname, hotelcity);
                hotel.next();
            }else{
                throw new ObjectNotCreated();
            }
        }

        // check if hotel trip exists -> create if must
        ResultSet hotelTrip = dbManager.getHotelTrip(TripTo, DepartureDate, hotel.getString("HotelId"));
        if(!hotelTrip.next())
            dbManager.createHotelTrip(TripTo, DepartureDate, hotel.getString("HotelId"));

        dbManager.addCustomerToTrip(customer.getString("CustomerId"),TripTo,DepartureDate,hotel.getString("HotelId"));

        dbManager.close();


    }



    /* TOUR-GUIDES RELATED */

    /**
     * This method provides the tour-guides who speak all languages registered in the database
     * @return the tour-guides who speak all languages registered in the database
     */
    @Override
    public Vector<String> getTourguidesAllLanguages() throws UncompletedRequest, SQLException {

        Vector<String> answer = new Vector<>();

        dbManager.open();

        ResultSet tourguides = dbManager.getTourguidesAllLanguages();
        while(tourguides.next()) {
            System.out.println("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Language amount: " + tourguides.getString("LangCount"));
            answer.add("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Language amount: " + tourguides.getString("LangCount"));
        }
        dbManager.close();


        return answer;
    }

    /**
     * This method provides the tour-guides who have attended all trips of a given year.
     * @param year provided year
     */
    @Override
    public Vector<String> getTourguidesAllTripsYear(String year) throws UncompletedRequest, SQLException {

        Vector<String> answer = new Vector<>();

        dbManager.open();

        ResultSet tourguides = dbManager.getTourguidesAllTripsYear(year);
        while (tourguides.next()) {
            System.out.println("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name"));
            answer.add("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name"));
        }
        dbManager.close();

        return answer;
    }


    /**
     * Method to get all guides
     *
     * @return Vector<String> - A vector containing strings with that information
     */
    @Override
    public Vector<String> getAllTourguideTrips() throws SQLException {

        Vector<String> answer = new Vector<>();

        dbManager.open();

        ResultSet tourguides = dbManager.getAllTourguideTrips();
        while (tourguides.next()) {
            System.out.println("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Phone:" + tourguides.getString("phone") + ", Trip to:" + tourguides.getString("TripTo") + " Departure date:" + tourguides.getString("DepartureDate"));
            answer.add("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name")+ ", Phone:" + tourguides.getString("phone") +", Trip to:" + tourguides.getString("TripTo") + " Departure date:" + tourguides.getString("DepartureDate"));
        }
        dbManager.close();

        return answer;
    }

    /**
     * Method to get the information of guides that are in trips (guides who do not have a trip are not going to appear here)
     *
     * @return Vector<String> - A vector containing strings with that information
     */
    @Override
    public Vector<String> getAllTourguideTripsNotNull() throws SQLException {

        Vector<String> answer = new Vector<>();

        dbManager.open();

        ResultSet tourguides = dbManager.getAllTourguideTripsNotNull();
        while (tourguides.next()) {
            System.out.println("Name: " + tourguides.getString("name") + ", Phone:" + tourguides.getString("phone") + ", Trip to:" + tourguides.getString("TripTo") + " Departure date:" + tourguides.getString("DepartureDate"));
            answer.add("Name: " + tourguides.getString("name")+ ", Phone:" + tourguides.getString("phone") +", Trip to:" + tourguides.getString("TripTo") + " Departure date:" + tourguides.getString("DepartureDate"));
        }
        dbManager.close();

        return answer;
    }


    /**
     * This method updates the tour-guide of the trips between two given dates.
     * @param tgprev previous tour-guide
     * @param tgnew new tour-guide to be set
     * @param date1 first date of the interval
     * @param date2 second date of the interval
     */
    @Override
    public void updateTourguide(String tgprev, String tgnew, String date1, String date2) throws UncompletedRequest, SQLException {

        dbManager.open();
        dbManager.updateTourguide(tgprev, tgnew, date1, date2);
        dbManager.close();
    }

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
    @Override
    public void changeGuidesBetweenTrips(String guidename1, String guidephone1, String TripTo1, String DepartureDate1, String guidename2, String guidephone2, String TripTo2, String DepartureDate2) throws UncompletedRequest, SQLException {

            dbManager.open();

            System.out.println("Finding the first tourguide");
            ResultSet guide1 = dbManager.getGuide(guidename1,guidephone1);
            if(!guide1.next()){
                System.out.println("System creating a guide");
                dbManager.createGuide(guidename1, guidephone1);
            }

            System.out.println("Finding the first trip");
            ResultSet trip1 = dbManager.getTrip(TripTo1,DepartureDate1);
            if(!trip1.next()){
                System.out.println("Trip does not exist in the database");
                System.out.println("Try again the transaction");
                dbManager.close();
                return;
            }

            if(!dbManager.existGuideInTrip(guide1.getString("GuideId"),TripTo1,DepartureDate1)){
                System.out.println("This guide is not in this trip!!!");
                return;
            }

            System.out.println("Finding the second tourguide");
            ResultSet guide2 = dbManager.getGuide(guidename2,guidephone2);
            if(!guide2.next()){
                System.out.println("System creating a guide");
                dbManager.createGuide(guidename2, guidephone2);
            }

            System.out.println("Finding the second trip");
            ResultSet trip2 = dbManager.getTrip(TripTo2,DepartureDate2);
            if(!trip2.next()){
                System.out.println("Trip does not exist in the database");
                System.out.println("Try again the transaction");
                dbManager.close();
                return;
            }

            if(!dbManager.existGuideInTrip(guide2.getString("GuideId"),TripTo2,DepartureDate2)){
                System.out.println("This guide is not in this trip!!!");
                return;
            }

            dbManager.swapGuidesBetweenTrips(guide1.getString("GuideId"),guide2.getString("GuideId"),TripTo1,TripTo2,DepartureDate1,DepartureDate2);
            System.out.println("Update done correctly!");

            dbManager.close();

    }

    /**
     * Retrieve the number of customer each guide is responsible for
     * @return the number of customer each guide is responsible for
     */
    @Override
    public Vector<String> retrieveNumCustomerGuideResponsible() throws UncompletedRequest, SQLException {


        Vector<String> answer = new Vector<>();

        dbManager.open();

        ResultSet numCustomers = dbManager.retrieveNumCustomerGuideResponsible();
        while(numCustomers.next()){
            System.out.println("GuideId: "+ numCustomers.getString("GuideId") + ", Number of customers: "+ numCustomers.getString("num"));
            answer.add("GuideId: "+ numCustomers.getString("GuideId") + ", Number of customers: "+ numCustomers.getString("num"));
        }

        dbManager.close();


        return answer;
    }


/* MENU AND MENU-ORDERS RELATED */

    /**
     * This method aims to provide all menu orders
     * @return all menu orders
     */
    @Override
    public Vector<String> getAllMenuOrders() throws SQLException {

    Vector<String> allorders = new Vector<>();

    dbManager.open();
    ResultSet orders = dbManager.getAllMenuOrders();
    if (orders==null) System.out.println("No menu order was found.");
    else {
        while (orders.next()) {
            System.out.println("Order number: " + orders.getString("numord") + ", menu type: " + orders.getString("menu_mtype") + ", menu id:" + orders.getString("menu_id") + ", customer id:" + orders.getString("customer_id"));
            allorders.add("Order number: " + orders.getString("numord") + ", menu type: " + orders.getString("menu_mtype") + ", menu id:" + orders.getString("menu_id") + ", customer id:" + orders.getString("customer_id"));
        }
    }
    dbManager.close();

    return allorders;
}

    /**
     * This method adds a menu-order (to the restaurant database)
     * @param choice String that represents whether additional objects should be created (y) or not (!=y)
     * @param menu_mtype String that represents the type of menu
     * @param menu_id String that represents the menu identifier
     * @param name String that represents the name of the customer
     * @param customer_id String that represents the id of the customer
     */
    @Override
    public void insertMenuOrder(String choice, String menu_mtype, String menu_id,  String name, String customer_id) throws ObjectNotCreated, UncompletedRequest, SQLException {

        dbManager.open();

        //Check if person exists -> create if must
        if (!dbManager.personExists(name, customer_id)){
            System.out.println("The person does not exist");

            if (choice.equals("y")) {
                System.out.println("Creating new person...");
                dbManager.insertPerson(name, null, customer_id);
            } else throw new ObjectNotCreated();

        }

        //Check if menu exists -> create if must
        ResultSet menu = dbManager.getMenu(menu_id, customer_id);
        if(!menu.next()){
            System.out.println("The menu does not exist");

            if (choice.equals("y")) {
                System.out.println("Creating a new menu...");
                dbManager.insertMenu(menu_id, customer_id);
            } else throw new ObjectNotCreated();
        }

        //Add menu-order
        dbManager.addMenuOrder(menu_mtype, menu_id, customer_id);
        dbManager.close();


    }


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
    @Override
    public void insertPerson(String choice, String name, String age, String id, String food, String restaurant) throws SQLException, UncompletedRequest {


        dbManager.open();
        //check person exists -> create if must
        if (dbManager.personExists(name, id)){
            System.out.println("The person already exists!");
            return;
        }
        dbManager.insertPerson(name, age, id);

        // Check food exists -> create if must
        if(!dbManager.foodExists(food)){
            System.out.println("The dish does not exist");

            if (choice.equals("y")) {
                System.out.println("Creating a new dish...");
                dbManager.insertDish(food);

                // register food as eaten by the person
                dbManager.insertEats(name,food);
            }
        }else dbManager.insertEats(name,food);

        // Check restaurant exists -> create if must
        if(!dbManager.restaurantExists(restaurant)){
            System.out.println("The restaurant does not exist");

            if (choice.equals("y")) {
                System.out.println("Creating a new restaurant...");
                dbManager.insertRestaurant(restaurant);

                //  make person frequent the restaurant
                dbManager.addFrequents(name, restaurant);
            }
        } else dbManager.addFrequents(name, restaurant);

            dbManager.close();
    }


    /**
     * This method deletes a person from the restaurants database
     * @param name String that represents the name of the person
     * @param id String that represents the id of the person
     * @throws SQLException if rollback could not be done
     */
    @Override
    public void deletePerson(String name, String id) throws SQLException, UncompletedRequest {

        dbManager.open();

        ResultSet person = dbManager.getPerson(name,id);

        if(person == null){
            System.out.println("There is no such person in the database!!");
        }else{
            dbManager.deletePerson(name, id);
        }
        dbManager.close();
    }

    /**
     * This method provides all the people that belong to the restaurants database
     * @return all the people that belong to the restaurants database
     */
    @Override
    public Vector<String> getAllPeople() throws SQLException {


        Vector<String> answer = new Vector<>();

        dbManager.open();
        ResultSet people = dbManager.getAllPeople();
        if (people==null) System.out.println("No person matching the requirements was found.");
        else {
            while(people.next()){
                System.out.println("Name: " + people.getString("p.nameid") + ", id: " + people.getString("p.id") + ", eats:" + people.getString("e.dish") + ", frequented restaurant:" + people.getString("f.restaurname"));
                answer.add("Name: " + people.getString("p.nameid") + ", id: " + people.getString("p.id") + ", eats:" + people.getString("e.dish") + ", frequented restaurant:" + people.getString("f.restaurname"));
            }
        }
        dbManager.close();
        return answer;
    }





    /* DISHES RELATED */

    /**
     * This method updates a given dishes' price to its half
     * @param dish provided dish
     */
    @Override
    public void updateDishPrice(String dish) throws SQLException, UncompletedRequest {
        dbManager.open();
        dbManager.updateDishPrice(dish);
        dbManager.close();
    }


    /**
     * Method to retrieve the information about dishes
     *
     * @return Vector<String> - A vector containing that information
     */
    @Override
    public Vector<String> getAllDishes() throws SQLException {

        Vector<String> answer = new Vector<>();

        dbManager.open();
        ResultSet dishes = dbManager.getAllDishes();
        if (dishes==null) System.out.println("No dish matching the requirements was found.");
        else {
            while(dishes.next()){
                System.out.println("Dish: " + dishes.getString("dish") + ",    restaurant:" + dishes.getString("restaurname")+ ",    price: " + dishes.getString("price") );
                answer.add("Dish: " + dishes.getString("dish") + ",    restaurant:" + dishes.getString("restaurname")+ ",    price: " + dishes.getString("price") );
            }
        }
        dbManager.close();
        return answer;

    }





    /* RESTAURANT RELATED */

    /**
     * This method gets the restaurants that provide food liked by all managers
     * @return the restaurants that provide food liked by all managers
     */
    @Override
    public Vector<String> getRestaurantLikedManagers() throws SQLException, UncompletedRequest {

        Vector<String> answer = new Vector<String>();

        dbManager.open();

        ResultSet restaurants = dbManager.getRestaurantLikedManagers();
        if (restaurants==null) System.out.println("No restaurants matching the requirements were found.");
        else
            while (restaurants.next()) {
                System.out.println("Restaurname: " + restaurants.getString("restaurant") + ", dish: " + restaurants.getString("dish"));
                answer.add("Restaurname: " + restaurants.getString("restaurant") + ", dish: " + restaurants.getString("dish"));
            }
        dbManager.close();

        return answer;
    }

    /* EMPLOYEES-RELATED*/

    /**
     * This method gets the employees who have attended a single restaurant of a given city
     * @param city provided city
     * @return the employees who have attended a single restaurant of a given city
     * @throws SQLException if an error occurred when managing the database
     * @throws UncompletedRequest if the query could not be carried out
     */
    @Override
    public Vector<String> getEmployee1RestCity(String city) throws SQLException, UncompletedRequest {
        Vector<String> answer = new Vector<>();
        dbManager.open();

        //obtain the due employees
        ResultSet employees = dbManager.getEmployee1RestCity(city);
            while (employees.next()) {
                //display and store the information
                System.out.println("Id: " + employees.getString("id") + ", First name: " + employees.getString("fname") + ", Last name: "+  employees.getString("lname"));
                answer.add("Id: " + employees.getString("id") + ", First name: " + employees.getString("fname") + ", Last name: "+  employees.getString("lname"));
            }

        dbManager.close();
        return answer;
    }

}
