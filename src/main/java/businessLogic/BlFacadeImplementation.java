package businessLogic;

import dataAccess.DataManager;

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
public class BlFacadeImplementation {

    private DataManager dbManager = new DataManager();

/* CUSTOMERS-RELATED */
    /**
     * Trasaction 1 -> Delete a customer by phone and name from a trip
     * @param name
     * @param phoneNum
     */
    public void deleteCustomerFromTrip(String name, String phoneNum, String TripTo, String DepartureDate) throws SQLException, ParseException {
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
     *
     * @throws SQLException
     */
    public Vector<String> getMaximumGainedTrip() throws SQLException {
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
     *
     * @throws SQLException
     */
    public Vector<String> retrieveCustomerEveryTripExc() throws SQLException {
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
    public Vector<String> getCustomersAllCheapestTrips(){
        Vector<String> answer = new Vector<>();
        try {
            dbManager.open();

            ResultSet customers = dbManager.getCustomersAllCheapestTrips();
            while (customers.next()) {
                System.out.println("Customerid: " + customers.getString("id") + ", Name: " + customers.getString("name"));
                answer.add("Customerid: " + customers.getString("id") + ", Name: " + customers.getString("name"));
            }

            dbManager.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return answer;
    }


    /**
     * Method that gets all customers from a trip, given the destination and the departure date
     *
     * @param trip String - The destination
     * @param departure String - The departure date
     * @return Vector<String> - The customers information
     * @throws SQLException
     * @throws ParseException
     */
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
     * @throws SQLException
     */
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
     *
     * @return
     */
    public Vector<String> getAllCustomers()  {
        Vector<String> answer = new Vector<>();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }

    /**
     *
     * @return
     */
    public Vector<String> getAllCustomersJustTrip()  {
        Vector<String> answer = new Vector<>();
        try {
            dbManager.open();
            ResultSet rs = dbManager.getAllCustomers();
            if (rs == null) {
                return null;
            }
            while (rs.next()) {
                System.out.println("Destination: " + rs.getString("TripTo") + ",  Departure date: " + rs.getString("DepartureDate") );
                answer.add("Destination: " + rs.getString("TripTo") + ",  Departure date: " + rs.getString("DepartureDate"));
            }
            dbManager.close();

            return answer;
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public void addCustomerToTrip(String choice, String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate){
        dbManager.open();
        try {

            ResultSet customer = dbManager.getCustomer(custname, custphone);

            if(!customer.next()){
                System.out.println("The customer does not exist");
                if(choice.equals("y")){
                    System.out.println("Creating a new customer with that data");
                    dbManager.insertCustomer(custname, custphone);
                    customer = dbManager.getCustomer(custname, custphone);
                    customer.next();
                }else {
                    return;
                }
            }

            ResultSet trip = dbManager.getTrip(TripTo, DepartureDate);
            if(!trip.next()) {
                System.out.println("the trip does not exist");
                if(choice.equals("y")){
                    System.out.println("Creating a new trip with the data");
                    dbManager.insertTrip(TripTo, DepartureDate);
                }else{
                    return;
                }
            }

            ResultSet hotel = dbManager.getHotel(hotelname, hotelcity);
            if(!hotel.next()){
                System.out.println("The hotel does not exist");

                if(choice.equals("y")){
                    System.out.println("Creating a new hotel with the data");
                    dbManager.insertHotel(hotelname, hotelcity);
                    hotel = dbManager.getHotel(hotelname, hotelcity);
                    hotel.next();
                }else{
                    return;
                }
            }

            ResultSet hotelTrip = dbManager.getHotelTrip(TripTo, DepartureDate, hotel.getString("HotelId"));
            if(!hotelTrip.next())
                dbManager.createHotelTrip(TripTo, DepartureDate, hotel.getString("HotelId"));

            dbManager.addCustomerToTrip(customer.getString("CustomerId"),TripTo,DepartureDate,hotel.getString("HotelId"));

            dbManager.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    /* TOUR-GUIDES RELATED */

    /**
     * This method provides the tour-guides who speak all languages registered in the database
     * @return the tour-guides who speak all languages registered in the database
     */
    public Vector<String> getTourguidesAllLanguages(){
        Vector<String> answer = new Vector<>();
        try {
            dbManager.open();

            ResultSet tourguides = dbManager.getTourguidesAllLanguages();
            while(tourguides.next()) {
                System.out.println("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Language amount: " + tourguides.getString("LangCount"));
                answer.add("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Language amount: " + tourguides.getString("LangCount"));
            }
            dbManager.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return answer;
    }

    /**
     * This method provides the tour-guides who have attended all trips of a given year.
     * @param year provided year
     */
    public Vector<String> getTourguidesAllTripsYear(String year){
        Vector<String> answer = new Vector<>();
        try {
            dbManager.open();

            ResultSet tourguides = dbManager.getTourguidesAllTripsYear(year);
            while (tourguides.next()) {
                System.out.println("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name"));
                answer.add("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name"));
            }
            dbManager.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }


    /**
     *
     * @return
     */
    public Vector<String> getAllTourguideTrips() {
        Vector<String> answer = new Vector<>();
        try {
            dbManager.open();

            ResultSet tourguides = dbManager.getAllTourguideTrips();
            while (tourguides.next()) {
                System.out.println("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Phone:" + tourguides.getString("phone") + ", Trip to:" + tourguides.getString("TripTo") + " Departure date:" + tourguides.getString("DepartureDate"));
                answer.add("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name")+ ", Phone:" + tourguides.getString("phone") +", Trip to:" + tourguides.getString("TripTo") + " Departure date:" + tourguides.getString("DepartureDate"));
            }
            dbManager.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }


    /**
     * This method updates the tour-guide of the trips between two given dates.
     * @param tgprev previous tourguide
     * @param tgnew new tourguide to be set
     * @param date1 first date of the interval
     * @param date2 second date of the interval
     */
    public void updateTourguide(String tgprev, String tgnew, String date1, String date2) {

        try{
            dbManager.open();
            dbManager.updateTourguide(tgprev, tgnew, date1, date2);
            dbManager.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
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
    public void changeGuidesBetweenTrips(String guidename1, String guidephone1, String TripTo1, String DepartureDate1, String guidename2, String guidephone2, String TripTo2, String DepartureDate2) {
        try {
            dbManager.open();

            System.out.println("Finding the frist tourguide");
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

            dbManager.swapGuidesBetweenTrips(guide1.getString("GuideId"),guide2.getString("GuideId"),TripTo1,TripTo2,DepartureDate1,DepartureDate2);
            System.out.println("Update done correctly!");

            dbManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the number of customer each guide is responsible of
     */
    public Vector<String> retrieveNumCustomerGuideResponsible(){

        Vector<String> answer = new Vector<>();
        try {
            dbManager.open();

            ResultSet numCustomers = dbManager.retrieveNumCustomerGuideResponsible();
            while(numCustomers.next()){
                System.out.println("GuideId: "+ numCustomers.getString("GuideId") + ", Number of customers: "+ numCustomers.getString("num"));
                answer.add("GuideId: "+ numCustomers.getString("GuideId") + ", Number of customers: "+ numCustomers.getString("num"));
            }

            dbManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answer;
    }


/* MENU AND MENU-ORDERS RELATED */

    /**
     * This method aims to provide all menu orders
     * @return all menu orders
     */
    public Vector<String> getAllMenuOrders(){

    Vector<String> allorders = new Vector<>();
    try {
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
    }catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    /**
     * This method adds a menu-order to the database
     * @param choice String that represents whether additional objects should be created (y) or not (!=y)
     * @param menu_mtype String that represents the type of menu
     * @param menu_id String that represents the menu identifier
     * @param name String that represents the name of the customer
     * @param customer_id String that represents the id of the customer
     */
    public void insertMenuOrder(String choice, String menu_mtype, String menu_id,  String name, String customer_id) {
        dbManager.open();
        try {
            //Check if person exists -> create if must
            if (!dbManager.personExists(name, customer_id)){
                System.out.println("The person does not exist");

                if (choice.equals("y")) {
                    System.out.println("Creating new person...");
                    dbManager.insertPerson(name, null, customer_id);
                } else return;

            }

            //Check if menu exists -> create if must
            ResultSet menu = dbManager.getMenu(menu_id, customer_id);
            if(!menu.next()){
                System.out.println("The menu does not exist");

                if (choice.equals("y")) {
                    System.out.println("Creating a new menu...");
                    dbManager.insertMenu(menu_id, customer_id);
                    menu = dbManager.getMenu(menu_id, customer_id);
                    menu.next();
                } else return;
            }

            //Add menu-order
            dbManager.addMenuOrder(menu_mtype, menu_id, customer_id);
            dbManager.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* PEOPLE RELATED (restaurant db) */

    /**
     *
     * @param choice
     * @param name
     * @param age
     * @param id
     * @param food
     * @param restaurant
     * @throws SQLException
     */
    public void insertPersonUI(String choice, String name, String age, String id, String food, String restaurant) throws SQLException {

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
    public void deletePerson(String name, String id) throws SQLException {
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
    public Vector<String> getAllPeople(){

        Vector<String> answer = new Vector<>();
        try {
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
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     */
    public String getPerson(String name, String id){
        try {
            dbManager.open();
            String answer="";
            ResultSet person = dbManager.getPerson(name,id);
            if (person==null) System.out.println("No person matching the requirements was found.");
            else

                System.out.println("Name: " + person.getString("nameid") + ", id: " + person.getString("id") + ", eats:" + person.getString("dish") + ", frequented restaurant:" + person.getString("restaurname"));
            answer="Name: " + person.getString("nameid") + ", id: " + person.getString("id") + ", eats:" + person.getString("dish") + ", frequented restaurant:" + person.getString("restaurname");

            dbManager.close();
            return answer;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /* DISHES RELATED */

    /**
     * This method updates a given dishes' price to its half
     * @param dish provided dish
     */
    public void updateDishPrice(String dish)  {
        try{
            dbManager.open();
            dbManager.updateDishPrice(dish);
            dbManager.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @return
     */
    public Vector<String> getAllDishes() {
        Vector<String> answer = new Vector<>();
        try {
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
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }





    /* RESTAURANT RELATED */

    /**
     * This method gets the restaurants that provide food liked by all managers
     */
    public Vector<String> getRestaurantLikedManagers(){
        Vector<String> answer = new Vector<String>();
        try {
            dbManager.open();

            ResultSet restaurants = dbManager.getRestaurantLikedManagers();
            if (restaurants==null) System.out.println("No restaurants matching the requirements were found.");
            else
                while (restaurants.next()) {
                    System.out.println("Restaurname: " + restaurants.getString("restaurant") + ", dish: " + restaurants.getString("dish"));
                    answer.add("Restaurname: " + restaurants.getString("restaurant") + ", dish: " + restaurants.getString("dish"));
                }
            dbManager.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }

    /* EMPLOYEES-RELATED*/






    /**
     * This method gets the employees who have attended a single restaurant of a given city
     * @param city provided city
     * @return
     */
    public Vector<String> getEmployee1RestCity(String city){
        Vector<String> answer = new Vector<>();
        try {

            dbManager.open();

            ResultSet employees = dbManager.getEmployee1RestCity(city);
                while (employees.next()) {
                    System.out.println("Id: " + employees.getString("id") + ", Name: " + employees.getString("name"));
                    answer.add("Id: " + employees.getString("id") + ", Name: " + employees.getString("name"));
                }

            dbManager.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }



}
