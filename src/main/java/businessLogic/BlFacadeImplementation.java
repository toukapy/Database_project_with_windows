package businessLogic;

import dataAccess.DataManager;
import exceptions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * This class represents the implementation of the BlFacade interface
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
     * @throws SQLException if database management fails
     * @throws ParseException if the date is not valid
     * @throws UncompletedRequest if transaction fails
     * @throws NotBelong if the person does not belong to the trip
     */
    @Override
    public void deleteCustomerFromTrip(String name, String phoneNum, String TripTo, String DepartureDate)
            throws SQLException, ParseException, UncompletedRequest, NotBelong {
        dbManager.open();

        //get the due customers
        ResultSet customer = dbManager.getCustomer(name,phoneNum);
        boolean exists = customer.next();
        if(!exists){
            //person does not exist in trip-> NotBelong
            System.out.println("Customer does not exist");
            throw new UncompletedRequest();
        }
        while(exists) {
            //person exists in trip -> delete
            if(dbManager.customerExistsInTripWithoutHotel(customer.getString("CustomerId"),TripTo,DepartureDate)){
                dbManager.deleteCustomerFromTrip(customer.getString("CustomerId"), TripTo, DepartureDate);
            }else{
                //person does not exist in trip-> NotBelong
                System.out.println("Customer does not exist in trip");
                throw new NotBelong();
            }
            exists = customer.next();
        }

        dbManager.close();
    }


    /**
     * Query 1 -> Retrieves the trip that has obtained the highest amount of gains
     * @return the trip that has obtained the highest amount of gains
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getMaximumGainedTrip() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();

        // Get the due trips
        ResultSet trip = dbManager.getMaximumGainedTrip();
        while(trip.next()){
            //Display and store the information
            answer.add("Destination: "+ trip.getString("TripTo") +", Departure date: "+ trip.getString("DepartureDate"));
            System.out.println("Destination: "+ trip.getString("TripTo") +", Departure date: "+ trip.getString("DepartureDate"));
        }
        dbManager.close();
        return answer;

    }


    /**
     * Query 2 -> Retrieves the customers that have gone to every trip with optional excursion
     * @return the customers that have gone to every trip with optional excursion
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> retrieveCustomerEveryTripExc() throws SQLException {
        Vector<String> answer = new Vector<String>();
        dbManager.open();
        //Get the due customers
        ResultSet customers = dbManager.retrieveCustomerEveryTripExc();
        while(customers.next()){
            //display and store the information
            System.out.println("CustomerId: "+ customers.getString("CustomerId") + ", name: " + customers.getString("custname") + ", phone: "+ customers.getString("custphone"));
            answer.add("CustomerId: "+ customers.getString("CustomerId") + ", name: " + customers.getString("custname") + ", phone: "+ customers.getString("custphone"));
        }
        dbManager.close();
        return answer;
    }
    /**
     * This method gets the customers who have attended at least all cheapest trips attended by customers
     * @return the customers who have attended at least all cheapest trips attended by customers
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getCustomersAllCheapestTrips() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();
        //get due customers
        ResultSet customers = dbManager.getCustomersAllCheapestTrips();
        while (customers.next()) {
            //display and store the due information
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
        //get the due customers
        ResultSet rs = dbManager.getCustomerTrip(trip,departure);
        while(rs.next()){
            //display and store the due information
            System.out.println("Customer: "+rs.getString("custname")+ ", Phone: " + rs.getString("custphone"));
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
     * @throws SQLException if database management fails
     * @throws ParseException if the date is not valid
     */
    @Override
    public Vector<String> getCustomerTripHotel(String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate)
            throws SQLException, ParseException {
        Vector<String> answer = new Vector<>();
        dbManager.open();
        //get due customers
        ResultSet rs = dbManager.getCustomerTripHotel(custname,custphone,hotelname,hotelcity,TripTo,DepartureDate);
        if(rs != null) {
            while (rs.next()) {
                //display and store the due information
                System.out.println("Destination: " + rs.getString("TripTo") + ", Departure date: " + rs.getString("DepartureDate") + ", Hotel name: " + rs.getString("hotelname") + ", hotel city: " + rs.getString("hotelcity") + ", Name: " + rs.getString("custname") + ", Phone: " + rs.getString("custphone"));
                answer.add("Destination: " + rs.getString("TripTo") + ", Departure date: " + new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("DepartureDate")) + ", Hotel name: " + rs.getString("hotelname") + ", hotel city: " + rs.getString("hotelcity") + ", Name: " + rs.getString("custname") + ", Phone: " + rs.getString("custphone"));
            }
        }
        dbManager.close();

        return answer;
    }


    /**
     * Method to get the information about all customers in a trip and a hotel
     *
     * @return Vector<String> - A vector containing strings with such information
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getAllCustomers() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();

        //get due customers
        ResultSet rs = dbManager.getAllCustomers();
        while (rs.next()) {
            //display and store the due information
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
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getAllCustomersJustTrip() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();
        //get due customers
        ResultSet rs = dbManager.getAllCustomersJustTrip();
        while (rs.next()) {
            //display and store the due information
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
     * @throws ObjectNotCreated if transaction cannot be completed because of non-created objects
     * @throws UncompletedRequest if transaction is not successful
     * @throws SQLException if database management fails
     * @throws ParseException if the date is not valid
     */
    @Override
    public void addCustomerToTrip(String choice, String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate)
            throws ObjectNotCreated, UncompletedRequest, SQLException, ParseException {

        dbManager.open();

        dbManager.addCustomerToTrip(choice, custname,custphone,hotelname,hotelcity,TripTo,DepartureDate);

        dbManager.close();


    }



    /* TOUR-GUIDES RELATED */

    /**
     * This method provides the tour-guides who speak all languages registered in the database
     * @return the tour-guides who speak all languages registered in the database
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getTourguidesAllLanguages() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();

        //get due tour-guides
        ResultSet tourguides = dbManager.getTourguidesAllLanguages();
        while(tourguides.next()) {
            //display and store the due information
            System.out.println("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Language amount: " + tourguides.getString("LangCount"));
            answer.add("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Language amount: " + tourguides.getString("LangCount"));
        }
        dbManager.close();


        return answer;
    }

    /**
     * This method provides the tour-guides who have attended all trips of a given year.
     * @param year provided year
     * @throws SQLException if database management fails
     *
     */
    @Override
    public Vector<String> getTourguidesAllTripsYear(String year) throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();
        //get due tourguides
        ResultSet tourguides = dbManager.getTourguidesAllTripsYear(year);
        while (tourguides.next()) {
            //display and store the due information
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
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getAllTourguideTrips() throws SQLException {

        Vector<String> answer = new Vector<>();
        dbManager.open();
        //get due tour-guides
        ResultSet tourguides = dbManager.getAllTourguideTrips();
        while (tourguides.next()) {
            //display and store the due information
            System.out.println("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name") + ", Phone: " + tourguides.getString("phone") + ", Trip to: " + tourguides.getString("TripTo") + ", Departure date: " + tourguides.getString("DepartureDate"));
            answer.add("Guideid: " + tourguides.getString("id") + ", Name: " + tourguides.getString("name")+ ", Phone: " + tourguides.getString("phone") +", Trip to: " + tourguides.getString("TripTo") + ", Departure date: " + tourguides.getString("DepartureDate"));
        }
        dbManager.close();

        return answer;
    }

    /**
     * Method to get the information of guides that are in trips (guides who do not have a trip are not going to appear here)
     *
     * @return Vector<String> - A vector containing strings with that information
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getAllTourguideTripsNotNull() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();
        //get due tour-guides
        ResultSet tourguides = dbManager.getAllTourguideTripsNotNull();
        while (tourguides.next()) {
            //display and store the due information
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
     * @throws UncompletedRequest if transaction fails
     * @throws SQLException if database management fails
     * @throws NoChange if no rows are updated
     * @throws NotBelong if tour-guides don't belong to the database
     * @throws ParseException if the date is not valid
     */
    @Override
    public void updateTourguide(String tgprev, String tgnew, String date1, String date2)
            throws UncompletedRequest, SQLException, NoChange, NotBelong, ParseException {

        dbManager.open();
        //check if tour-guides belong to the database
        ResultSet tourguide1 = dbManager.getGuideById(tgprev);
        if(!tourguide1.next()) throw new NotBelong();
        ResultSet tourguide2 = dbManager.getGuideById(tgnew);
        if(!tourguide2.next()) throw new NotBelong();

        //update tour-guides
        dbManager.updateTourguide(tgprev, tgnew, date1, date2);
        dbManager.close();
    }

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
    @Override
    public void changeGuidesBetweenTrips(String choice, String guidename1, String guidephone1, String TripTo1, String DepartureDate1, String guidename2, String guidephone2, String TripTo2, String DepartureDate2)
            throws UncompletedRequest, SQLException, ParseException {
            dbManager.open();

            //swap guides
            dbManager.swapGuidesBetweenTrips(choice, guidename1, guidename2, guidephone1, guidephone2, TripTo1, TripTo2, DepartureDate1, DepartureDate2);
            System.out.println("Update done correctly!");

            dbManager.close();

    }

    /**
     * Retrieve the number of customer each guide is responsible for
     * @return the number of customer each guide is responsible for
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> retrieveNumCustomerGuideResponsible() throws SQLException {
        Vector<String> answer = new Vector<>();

        dbManager.open();
        //retrieve the due information
        ResultSet numCustomers = dbManager.retrieveNumCustomerGuideResponsible();
        while(numCustomers.next()){
            //display and store the due information
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
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getAllMenuOrders() throws SQLException {
    Vector<String> allorders = new Vector<>();
    dbManager.open();

    //obtain the due information
    ResultSet orders = dbManager.getAllMenuOrders();
    while (orders.next()) {
        //display and store the information
        System.out.println("Order number: " + orders.getString("numord") + ", menu type: " + orders.getString("menu_mtype") + ", menu id:" + orders.getString("menu_id") + ", customer id:" + orders.getString("customer_id"));
        allorders.add("Order number: " + orders.getString("numord") + ", menu type: " + orders.getString("menu_mtype") + ", menu id:" + orders.getString("menu_id") + ", customer id:" + orders.getString("customer_id"));
    }

    dbManager.close();

    return allorders;
}

    /**
     * This method adds a menu-order (to the restaurant database)
     * @param choice String that represents whether additional objects should be created (y) or not (!=y)
     * @param menu_mtype String that represents the type of menu
     * @param menu_id String that represents the menu identifier
     * @param customer_id String that represents the id of the customer
     * @throws UncompletedRequest if transaction could not be completed
     * @throws SQLException if database management failed
     */
    @Override
    public void insertMenuOrder(String choice, String menu_mtype, String menu_id, String customer_id)
            throws UncompletedRequest, SQLException {
        dbManager.open();
        //Add menu-order
        dbManager.addMenuOrder(choice, menu_mtype, menu_id, customer_id);
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
     * @throws UncompletedRequest if transaction could not be completed
     */
    @Override
    public void insertPerson(String choice, String name, String age, String id, String food, String restaurant)
            throws SQLException, UncompletedRequest {
        dbManager.open();
        //insert person
        dbManager.insertPersonRestaurantEats(choice, name, age, id, food, restaurant);
        dbManager.close();
    }




    /**
     * This method deletes a person from the restaurants database
     * @param name String that represents the name of the person
     * @param id String that represents the id of the person
     * @throws SQLException if rollback could not be done
     * @throws UncompletedRequest if the transaction could not be completed
     * @throws NotBelong if the person does not belong to the database
     */
    @Override
    public void deletePerson(String name, String id) throws SQLException, UncompletedRequest, NotBelong {
        dbManager.open();
        ResultSet person = dbManager.getPerson(name,id);

        //the person does not belong to the database
        if(!person.next()) throw new NotBelong();

        //delete if they belong to the database
        else dbManager.deletePerson(name, id);
        dbManager.close();
    }

    /**
     * This method provides all the people that belong to the restaurants database
     * @return all the people that belong to the restaurants database
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getAllPeople() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();

        //obtain the due people
        ResultSet people = dbManager.getAllPeople();
        while(people.next()){
            //display and store the information
            System.out.println("Name: " + people.getString("p.nameid") + ", id: " + people.getString("p.id") + ", eats: " + people.getString("e.dish") + ", frequented restaurant: " + people.getString("f.restaurname"));
            answer.add("Name: " + people.getString("p.nameid") + ", id: " + people.getString("p.id") + ", eats: " + people.getString("e.dish") + ", frequented restaurant: " + people.getString("f.restaurname"));
        }

        dbManager.close();
        return answer;
    }





    /* DISHES RELATED */

    /**
     * This method updates a given dishes' price to its half
     * @param dish provided dish
     * @throws SQLException if database management fails
     * @throws UncompletedRequest if the transaction was not successful
     * @throws NoChange if no changes were made (serves table)
     * @throws NotBelong if the dish does not belong to the database (dish table)
     */
    @Override
    public void updateDishPrice(String dish) throws SQLException, UncompletedRequest, NoChange, NotBelong {
        dbManager.open();
        //check if dish is registered in the database
        if(!dbManager.foodExists(dish)) throw new NotBelong();
        //update the dish price for the restaurants that serve it
        dbManager.updateDishPrice(dish);
        dbManager.close();
    }


    /**
     * Method to retrieve the information about dishes
     *
     * @return Vector<String> - A vector containing that information
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getAllDishes() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();

        //obtain the due dishes
        ResultSet dishes = dbManager.getAllDishes();
        while(dishes.next()){
            //save and store the due information
            System.out.println("Dish: " + dishes.getString("dish") + ",    restaurant: " + dishes.getString("restaurname")+ ",    price: " + dishes.getString("price") );
            answer.add("Dish: " + dishes.getString("dish") + ",    restaurant: " + dishes.getString("restaurname")+ ",    price: " + dishes.getString("price") );
        }

        dbManager.close();
        return answer;

    }





    /* RESTAURANT RELATED */

    /**
     * This method gets the restaurants that provide food liked by all managers
     * @return the restaurants that provide food liked by all managers
     * @throws SQLException if database management fails
     */
    @Override
    public Vector<String> getRestaurantLikedManagers() throws SQLException {
        Vector<String> answer = new Vector<>();
        dbManager.open();

        //Obtain the due restaurants
        ResultSet restaurants = dbManager.getRestaurantLikedManagers();
        while (restaurants.next()) {
            //display and store the due information
            System.out.println("Restaurant name: " + restaurants.getString("restaurant") + ", dish: " + restaurants.getString("dish"));
            answer.add("Restaurant name: " + restaurants.getString("restaurant") + ", dish: " + restaurants.getString("dish"));
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
     */
    @Override
    public Vector<String> getEmployee1RestCity(String city) throws SQLException {
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

    /**
     * This method gives a raise of 1000 dollars to the employees who are paid less than
     * the average salary of the company. Also, if they have any dependent they get 100 dollars
     * per dependent
     */
    @Override
    public void risesForEmployees() throws SQLException {

        dbManager.open();

        dbManager.rises();

        dbManager.close();

    }

    /**
     * This query retrieves couples of names and a restaurant of people who frequent the same
     * restaurant, have at least a liked dish in common and that restaurant serves it
     * @return
     */
    @Override
    public Vector<String> restaurantDates() throws SQLException {
        Vector<String> answer = new Vector<>();

        dbManager.open();

        ResultSet dates = dbManager.resDates();

        while(dates.next()){
            System.out.println("Name: "+dates.getString("nId")+", Name: "+dates.getString("nameId")+", Restaurant: "+dates.getString("restaurname"));

            answer.add(dates.getString("nId")+" and "+
                    dates.getString("nameId")+" in "+dates.getString("restaurname"));
        }

        dbManager.close();


        return answer;
    }


    /**
     * For choosing from dept and locations for "bookTripToDepartment"
     * @return all the departments and locations
     */
    @Override
    public Vector<String> getDepartmentsAndLocations() {
        Vector<String> answer = new Vector<>();

        try {
            dbManager.open();
            ResultSet rs = dbManager.deptAndLocation();

            while(rs.next()){
                System.out.println(rs.getString("Dnumber")+","+rs.getString("Dlocation"));
                answer.add("Number: " + rs.getString("Dnumber")+",Location: "+rs.getString("Dlocation"));
            }

            dbManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;

    }

    /**
     * Book a trip to the employees not in the given department to the location of that department
     * in the given date
     * @param Dno the department the trip is to
     * @param location where the trip is to
     * @param date what date the trip is
     * @return
     */
    @Override
    public void bookTripToDepartment(String Dno, String location, String date) throws SQLException, UncompletedRequest, NoHotel, ParseException {

        dbManager.open();

        ResultSet custNumbers = dbManager.employeesNotInTheDepartment(Dno);
        Vector<String> c = new Vector<>();

        while(custNumbers.next()){
            c.add(custNumbers.getString("Cust_Id"));
        }

        dbManager.insertHotelTrip(location,date,c);

        System.out.println("Employees inserted successfully into the trip");

        dbManager.close();

    }

    /**
     * Get all the hotel customer trips that are to and in the date recieved, and the people going are employees
     * @param location where the trip is to
     * @param date the trip date
     * @return the ssn and full name of the employee, the trip location and date and the hotel id
     * @throws SQLException
     */
    @Override
    public Vector<String> getTrips(String location, String date) throws SQLException {
        dbManager.open();
        Vector<String> ans = new Vector<>();
        ResultSet rs = dbManager.insertedTrips(location,date);
        while (rs.next())
            ans.add("Ssn: "+rs.getString("Ssn")+", Name: "+rs.getString("Fname")+" "+rs.getString("Lname")+
                ", Trip to: "+rs.getString("TripTo")+", Date: "+rs.getString("DepartureDate")+", Hotel: "+rs.getString("HotelId"));

        dbManager.close();
        return ans;
    }

    /**
     * This method retrieves the employees that have been to the same hotels as the CEO
     * @return the info about the employees
     * @throws SQLException
     */
    @Override
    public Vector<String> hotelsCEO() throws SQLException {

        Vector<String> asw = new Vector<>();

        dbManager.open();

        ResultSet emp = dbManager.hotelsCEO();

        System.out.println("Employees who have been to the same hotels as the company CEO:");
        while (emp.next()) {
            System.out.println("\tEmployee Ssn: " + emp.getString("Ssn") + ", Name: " + emp.getString("Fname")
                    + ", Last Name: " + emp.getString("Lname"));
            asw.add("Employee Ssn: " + emp.getString("Ssn") + ", Name: " + emp.getString("Fname")
                    + ", Last Name: " + emp.getString("Lname"));
        }
        dbManager.close();

        return asw;
    }

    /**
     * Method to get the ssn-s and salaries of all employees in the database
     * @return the ssn and salary of all employees
     * @throws SQLException
     */
    @Override
    public Vector<String> getAllSalaries() throws SQLException {

        Vector<String> a = new Vector<>();
        dbManager.open();
        ResultSet rs = dbManager.getSalaries();
        while (rs.next())
            a.add("Ssn: "+rs.getString("Ssn")+", salary: "+rs.getString("Salary"));
        dbManager.close();

        return a;
    }

    /**
     * Get the employees who have worked the most hours in each project and the manager of the department responsible for the project
     * @return The employee name, last name, project name, the hours worked and the department managers full name
     * @throws SQLException
     */
    @Override
    public Vector<String> getSpeakers() throws SQLException {
        Vector<String> speakers = new Vector<>();
        dbManager.open();
        ResultSet spk = dbManager.getSpeakers();

        while (spk.next())
            speakers.add("Speaker: "+spk.getString("Fname")+" "+spk.getString("Lname")+
                    "\t Project: "+spk.getString("Pname")+"\t Hours worked: "+spk.getString("Hours")+
                    "\t Department manager: "+ spk.getString("Fn")+" "+spk.getString("Ln"));

        dbManager.close();
        return  speakers;
    }

}
