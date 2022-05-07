package businessLogic;

import dataAccess.DataManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;

public class BlFacadeImplementation {

    private DataManager dbManager = new DataManager();

    /**
     * Trasaction 1 -> Delete a customer by phone and name from a trip
     * @param name
     * @param phoneNum
     */
    public void deleteCustomerFromTrip(String name, String phoneNum, String TripTo, String DepartureDate) throws SQLException {
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
     * Transaction 2 -> Retrieves the trip that has obtained the highest amount of gains
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
     *
     * @param custname
     * @param custphone
     * @param hotelname
     * @param hotelcity
     * @param TripTo
     * @param DepartureDate
     */
    public void addCustomerToTrip(String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate){
        Scanner sc = new Scanner(System.in);
        String choice = "";

        dbManager.open();
        try {

            ResultSet customer = dbManager.getCustomer(custname, custphone);

            if(!customer.next()){
                System.out.println("The customer does not exist");
                System.out.println("Do you want to create a new customer? (y/n)\n");
                choice = sc.nextLine();

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
                System.out.println("Do you want to create a new trip? (y/n)\n");
                choice = sc.nextLine();

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
                System.out.println("Do you want to create a new hotel? (y/n)\n");
                choice = sc.nextLine();

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


    /**
     *
     * @param custname
     * @param custphone
     * @param hotelname
     * @param hotelcity
     * @param TripTo
     * @param DepartureDate
     */
    public void addCustomerToTripUI(String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate){
        dbManager.open();
        try {

            ResultSet customer = dbManager.getCustomer(custname, custphone);

            if(!customer.next()){
                    System.out.println("Creating a new customer with that data");
                    dbManager.insertCustomer(custname, custphone);
                    customer = dbManager.getCustomer(custname, custphone);
                    customer.next();
            }

            ResultSet trip = dbManager.getTrip(TripTo, DepartureDate);
            if(!trip.next()) {
                    System.out.println("Creating a new trip with the data");
                    dbManager.insertTrip(TripTo, DepartureDate);
            }

            ResultSet hotel = dbManager.getHotel(hotelname, hotelcity);
            if(!hotel.next()){
                    System.out.println("Creating a new hotel with the data");
                    dbManager.insertHotel(hotelname, hotelcity);
                    hotel = dbManager.getHotel(hotelname, hotelcity);
                    hotel.next();
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

    /**
     *
     * @param guidename1
     * @param guidephone1
     * @param TripTo1
     * @param DepartureDate1
     * @param guidename2
     * @param guidephone2
     * @param TripTo2
     * @param DepartureDate2
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
            ResultSet guide2 = dbManager.getGuide(guidename1,guidephone1);
            if(!guide2.next()){
                System.out.println("System creating a guide");
                dbManager.createGuide(guidename1, guidephone1);
            }

            System.out.println("Finding the second trip");
            ResultSet trip2 = dbManager.getTrip(TripTo1,DepartureDate1);
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
     *
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


    /**
     * This method adds a menu-order to the database
     * @param menu_mtype
     * @param menu_id
     * @param customer_id
     */
    public void insertMenuOrder(String menu_mtype, String menu_id, String name, String customer_id) {
        Scanner sc = new Scanner(System.in);
        String choice = "";

        dbManager.open();
        try {
            if (!dbManager.personExists(name, customer_id)){
                System.out.println("The person does not exist");
                System.out.println("Do you want to create a new person? (y/n)\n");
                choice = sc.nextLine();

                if (choice.equals("y")) {
                    dbManager.insertPerson(name, null, null, customer_id);
                } else {
                    return;
                }
            }

            ResultSet menu = dbManager.getMenu(menu_id, customer_id);
            if(!menu.next()){
                System.out.println("The customer does not exist");
                System.out.println("Do you want to create a new customer? (y/n)\n");
                choice = sc.nextLine();

                if (choice.equals("y")) {
                    System.out.println("Creating a new menu with the data");
                    dbManager.insertMenu(menu_id, customer_id);
                    menu = dbManager.getMenu(menu_id, customer_id);
                    menu.next();
                }
            }


            dbManager.addMenuOrder(menu_mtype, menu_id, customer_id);

            dbManager.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method adds a menu-order to the database
     * @param menu_mtype
     * @param menu_id
     * @param customer_id
     */
    public void insertMenuOrderUI(String menu_mtype, String menu_id,  String name, String customer_id) {
        dbManager.open();
        try {
            if (!dbManager.personExists(name, customer_id)){
                dbManager.insertPerson(name, null, null, customer_id);
            }

            ResultSet menu = dbManager.getMenu(menu_id, customer_id);
            if(!menu.next()){
                    System.out.println("Creating a new menu with the data");
                    dbManager.insertMenu(menu_id, customer_id);
                    menu = dbManager.getMenu(menu_id, customer_id);
                    menu.next();
            }

            dbManager.addMenuOrder(menu_mtype, menu_id, customer_id);
            dbManager.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    /**
     * This method provides the tour-guides who speak all languages registered in the database
     */
    public void getTourguidesAllLanguages(){
        try {
            dbManager.open();

            ResultSet tourguides = dbManager.getTourguidesAllLanguages();
            if (tourguides==null) System.out.println("No tour-guides matching the requirements were found.");
            else
                while(tourguides.next())
                    System.out.println("Guideid: "+ tourguides.getString("id") + ", Name: "+ tourguides.getString("name") + ", Language amount: "+ tourguides.getString("LangCount"));

            dbManager.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
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

    /**
     * This method gets the customers who have attended at least all cheapest trips attended by customers
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


    public void close() throws SQLException {
        dbManager.close();

    }
}
