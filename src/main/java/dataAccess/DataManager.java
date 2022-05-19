package dataAccess;

import exceptions.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class DataManager {

    private DatabaseConnector connector;
    private Statement stmt;
    private ResultSet rs;
    private int correct = -1;
    private long currentCustomerId;
    private long currentHotelId;
    private long currentGuideId;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * MEthod that opens a connection with the Database connector
     */
    public void open(){
        connector = new DatabaseConnector();
        stmt = connector.getStatement();
    }

    /**
     * Closes the connection with the database
     * @throws SQLException
     */
    public void close() throws SQLException {
        connector.close();
        if(rs != null){
            rs.close();
        }
    }

    /* CUSTOMER-RELATED */





    /**
     * Returns a Customer by phone and name
     * @param custname String - The name of the customer
     * @param custphone String - the phone of the customer
     * @return ResultSet - The set of results that matches the information
     * @throws SQLException if database management fails
     */
    public ResultSet getCustomer(String custname, String custphone) throws SQLException {

        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM customer WHERE custname=? and custphone=?;");
        p.setString(1,custname);
        p.setString(2,custphone);

        rs = p.executeQuery();
        return rs;
    }

    /**
     * Method that inserts a customer in the customer table
     *
     * @param custname - Customer's name
     * @param custphone - Customer's phone
     */
    public void insertCustomer(String custname, String custphone) throws SQLException, UncompletedRequest {

        try {
           // connector.getConnector().setAutoCommit(false);
            ResultSet customers = connector.getStatement().executeQuery("SELECT CustomerId FROM customer WHERE CustomerId LIKE '100%';");
            while(customers.next()){
                if(customers.isLast()){
                    break;
                }
            }
            currentCustomerId = Integer.parseInt(customers.getString("CustomerId")) + 1;
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO customer VALUES (?,?,default,?);");
            p.setString(1,String.valueOf(currentCustomerId));
            p.setString(2,custname);
            p.setString(3,custphone);


            p.executeUpdate();

         //   connector.getConnector().commit();
            System.out.println("Customer added successfully!!");

        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }


    }

    /**
     * Retrieve the set of customers that go to that trip
     *
     * @param trip - Destination
     * @param departure - Departure date
     * @return ResultSet - The customers that attend that trip
     * @throws ParseException if the date format is not valid
     * @throws SQLException if database management fails
     */
    public ResultSet getCustomerTrip(String trip, String departure) throws ParseException, SQLException {
        PreparedStatement  stmt = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip_customer as htc INNER JOIN customer as c ON htc.CustomerId = c.CustomerId WHERE TripTo=? AND DepartureDate=?;");
        stmt.setString(1,trip);
        stmt.setDate(2,new Date(format.parse(departure).getTime()));
        rs = stmt.executeQuery();
        return rs;

    }


    /**
     * Method that returns a set that has all customers in trips with optional excursions
     *
     * @return ResultSet - A set with customer's in all trips with optional excursions
     * @throws SQLException if database management fails
     */
    public ResultSet retrieveCustomerEveryTripExc() throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT c.custname, c.custphone, c.CustomerId FROM customer as c WHERE NOT EXISTS(" +
                "SELECT * FROM trip as t WHERE NOT EXISTS(" +
                "SELECT * FROM excur_opt_customer as eoc WHERE eoc.CustomerId = c.CustomerId AND eoc.TripTo = t.TripTo AND eoc.DepartureDate = t.DepartureDate));");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!");

        return rs;
    }


    /**
     *
     * @param custname
     * @param custphone
     * @param hotelname
     * @param hotelcity
     * @param tripTo
     * @param departureDate
     * @return
     * @throws SQLException if database management fails
     * @throws ParseException if date is not valid
     */
    public ResultSet getCustomerTripHotel(String custname, String custphone, String hotelname, String hotelcity, String tripTo, String departureDate)
            throws SQLException, ParseException {

            ResultSet customer = getCustomer(custname, custphone);
            ResultSet hotel = getHotel(hotelname,hotelcity);

            if(customer.next() && hotel.next()){
                PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT * FROM (hotel_trip_customer as htc INNER JOIN customer as c on htc.CustomerId=c.CustomerId) INNER JOIN hotel as h ON htc.HotelId=h.HotelId  WHERE htc.CustomerId=? AND htc.HotelId=? AND htc.TripTo=? AND htc.DepartureDate=?;");
                stmt.setString(1,customer.getString("CustomerId"));
                stmt.setString(2,hotel.getString("HotelId"));
                stmt.setString(3,tripTo);
                stmt.setDate(4,new Date(format.parse(departureDate).getTime()));
                rs = stmt.executeQuery();
                return rs;
            }
            return null;
    }

    /**
     * This method gets the customers who have attended at least all cheapest trips attended by customers
     * @return the customers who have attended at least all cheapest trips attended by customers
     * @throws SQLException if database management fails
     */
    public ResultSet getCustomersAllCheapestTrips() throws SQLException {
        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT c.customerid as id, c.custname as name " +
                "FROM customer as c WHERE not exists ( " +
                "SELECT * FROM trip as t where  (t.Ppday*t.numdays)=(SELECT min(t2.ppday*t2.numdays) from trip as t2) and not exists ( " +
                "SELECT * from hotel_trip_customer as htc " +
                "where htc.tripto=t.tripto and htc.departuredate=t.departuredate and htc.customerid=c.customerid)) " +
                "and exists (SELECT * FROM trip as t where (t.Ppday*t.numdays)=(SELECT min(t2.ppday*t2.numdays) from trip as t2));");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");
        return rs;
    }


    /**
     * This method provides all customers
     * @return all customers
     * @throws SQLException if database management fails
     */
    public ResultSet getAllCustomers() throws SQLException {
        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT htc.TripTo, htc.DepartureDate, h.hotelname, h.hotelcity, c.custname, c.custphone " +
                "FROM hotel_trip_customer AS htc INNER JOIN hotel AS h ON htc.HotelId=h.HotelId " +
                "INNER JOIN customer AS c ON htc.CustomerId=c.CustomerId;");

        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");
        return rs;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public ResultSet getAllCustomersJustTrip() throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT DISTINCT htc.TripTo, htc.DepartureDate " +
                "FROM hotel_trip_customer AS htc " +
                "INNER JOIN customer AS c ON htc.CustomerId=c.CustomerId;");

        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");

        return rs;
    }

    /**
     * Deletes the customer (CustomerId) from a trip (TripTo, DepartureDate)
     *
     * @param CustomerId String - The id of the customer
     * @param TripTo String - The destination of the trip
     * @param DepartureDate String - The departure date of the trip
     * @throws SQLException
     */
    public void deleteCustomerFromTrip(String CustomerId, String TripTo, String DepartureDate) throws SQLException, ParseException, UncompletedRequest {
          try{
            connector.getConnector().setAutoCommit(false);
            PreparedStatement deleteStmt = connector.getConnector().prepareStatement("DELETE FROM hotel_trip_customer WHERE CustomerId=? and TripTo=? and DepartureDate=?;");

            deleteStmt.setString(1,CustomerId);
            deleteStmt.setString(2,TripTo);
            deleteStmt.setDate(3,new Date(format.parse(DepartureDate).getTime()));
            deleteStmt.executeUpdate();

            connector.getConnector().commit();

            System.out.println("Customer was deleted from trip successfully!!");

        }catch(SQLException e){
            System.out.println("Transaction is being rolled back!");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }

    }

    /**
     *
     * @param choice
     * @param custname
     * @param custphone
     * @param hotelname
     * @param hotelcity
     * @param TripTo
     * @param DepartureDate
     * @throws SQLException
     * @throws UncompletedRequest
     * @throws ParseException
     */
    public void addCustomerToTrip(String choice, String custname, String custphone, String hotelname, String hotelcity, String TripTo, String DepartureDate) throws SQLException, UncompletedRequest, ParseException {
        try {
            connector.getConnector().setAutoCommit(false);
            // check if customer exists -> create if must
            ResultSet customer = getCustomer(custname, custphone);
            if(!customer.next()){
                System.out.println("The customer does not exist");
                if(choice.equals("y")){
                    System.out.println("Creating a new customer with that data");
                    insertCustomer(custname, custphone);
                    customer = getCustomer(custname, custphone);
                    customer.next();
                }
            }

            // check if trip exist ->  create if must
            ResultSet trip = getTrip(TripTo, DepartureDate);
            if(!trip.next()) {
                System.out.println("the trip does not exist");
                if(choice.equals("y")){
                    System.out.println("Creating a new trip with the data");
                   insertTrip(TripTo, DepartureDate);
                }
            }

            // check if hotel exists -> create if must
            ResultSet hotel = getHotel(hotelname, hotelcity);
            if(!hotel.next()){
                System.out.println("The hotel does not exist");

                if(choice.equals("y")){
                    System.out.println("Creating a new hotel with the data");
                    insertHotel(hotelname, hotelcity);
                    hotel = getHotel(hotelname, hotelcity);
                    hotel.next();
                }
            }

            // check if hotel trip exists -> create if must
            ResultSet hotelTrip = getHotelTrip(TripTo, DepartureDate, hotel.getString("HotelId"));
            if(!hotelTrip.next())
                createHotelTrip(TripTo, DepartureDate, hotel.getString("HotelId"));

            if(customerExistsInTrip(customer.getString("CustomerId"),TripTo,DepartureDate,hotel.getString("HotelId"))){
                System.out.println("Customer already in trip !!");
            }else{

                PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO hotel_trip_customer VALUES (?,?,?,?,default);");
                p.setString(1,TripTo);
                p.setDate(2,new Date(format.parse(DepartureDate).getTime()));
                p.setString(3,hotel.getString("HotelId"));
                p.setString(4,customer.getString("CustomerId"));
                p.executeUpdate();
            }

            connector.getConnector().commit();
            System.out.println("Customer added successfully to the trip!!");
        } catch (ParseException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new ParseException("Please, enter a valid date",1);
        } catch (SQLException g){
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     * Method that returns whether a customer exists in a trip or not
     *
     * @param customerId String - Customer's id
     * @param tripTo String - Trip's destination
     * @param departureDate String - trip's date
     * @param hotelId String - Hotel's id
     * @return boolean - Whether the customer is in the trip or not
     * @throws SQLException if database management fails
     * @throws ParseException if date is not valid
     */
    private boolean customerExistsInTrip(String customerId, String tripTo, String departureDate, String hotelId) throws SQLException, ParseException {

        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip_customer WHERE CustomerId=? and TripTo=? and DepartureDate=? and HotelId=?;");
        p.setString(1,customerId);
        p.setString(2,tripTo);
        p.setDate(3,new Date(format.parse(departureDate).getTime()));
        p.setString(4,hotelId);
        rs = p.executeQuery();

        return rs.next();
    }

    /**
     * Method to see whether a customer exists in a trip or not, but without specifying the hotel
     *
     * @param customerId String - Customer's id
     * @param TripTo String - Trip's destination
     * @param DepartureDate String - Trip's date
     * @return boolean- Whether the customer exists in the trip or not
     * @throws SQLException if database management fails
     * @throws ParseException if date is not valid
     */
    public boolean customerExistsInTripWithoutHotel(String customerId, String TripTo, String DepartureDate)
            throws SQLException, ParseException {

        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip_customer WHERE CustomerId=? and TripTo=? and DepartureDate=?;");
        p.setString(1,customerId);
        p.setString(2,TripTo);
        p.setDate(3,new Date(format.parse(DepartureDate).getTime()));
        rs = p.executeQuery();

        return rs.next();
    }



    /* HOTEL-RELATED */
    /**
     * Method to get the hotel object of a given hotel
     *
     * @param hotelname - The name of the hotel
     * @param hotelcity - The city where the hotel is
     * @return ResultSet - The hotels matching that information
     * @throws SQLException if database management fails
     */
    public ResultSet getHotel(String hotelname, String hotelcity) throws SQLException {
        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel WHERE hotelname=? and hotelcity=?;");
        p.setString(1,hotelname);
        p.setString(2,hotelcity);
        rs =  p.executeQuery();
        return rs;
    }

    /**
     * Method to insert a hotel in the hotel table
     *
     * @param hotelname - The name of the hotel
     * @param hotelcity - The city where the hotel is
     * @throws SQLException if rollback fails
     */
    public void insertHotel(String hotelname, String hotelcity) throws SQLException, UncompletedRequest {
        try {
          //  connector.getConnector().setAutoCommit(false);
            ResultSet hotels = connector.getStatement().executeQuery("SELECT HotelId FROM hotel WHERE HotelId LIKE 'h%';");
            while(hotels.next()){
                if(hotels.isLast()){
                    break;
                }
            }
            currentHotelId = Integer.parseInt(hotels.getString("HotelId").substring(1)) + 1;
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO hotel VALUES (?,?,?,default);");
            p.setString(1,"h"+ currentHotelId);
            p.setString(2,hotelname);
            p.setString(3,hotelcity);


            p.executeUpdate();

          //  connector.getConnector().commit();
            System.out.println("Hotel added successfully!!");

        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }


    /* TRIP-RELATED */

    /**
     * Method to get the trips that match that conditions
     *
     * @param tripTo - The destination of the trip
     * @param departureDate - The date of the trip
     * @return ResultSet - The trips that match that condition
     * @throws SQLException if database management fails
     * @throws ParseException if the date is not valid
     */
    public ResultSet getTrip(String tripTo, String departureDate) throws SQLException, ParseException {
        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM trip WHERE TripTo=? and DepartureDate=?;");
        p.setString(1,tripTo);
        p.setDate(2,new Date(format.parse(departureDate).getTime()));
        rs =  p.executeQuery();
        return rs;
    }

    /**
     * Method to insert a trip into the trip table
     *
     * @param tripTo - Destination of the trip
     * @param departureDate - Date of the trip
     * @throws SQLException if database management fails
     * @throws UncompletedRequest if transaction has not been executed properly
     * @throws ParseException if date is not valid
     */
    public void insertTrip(String tripTo, String departureDate) throws SQLException, UncompletedRequest, ParseException {
        try {
            //connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO trip VALUES (?,?,default,default,default,default);");
            p.setString(1,tripTo);
            p.setDate(2, new Date(format.parse(departureDate).getTime()));
            p.executeUpdate();

            //connector.getConnector().commit();
            System.out.println("Trip added successfully!!");

        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     * Method that retrieves the trip (TripTo, DepartureDate) that has gained the maximum amount of money
     *
     * @return ResultSet - The set that has the object specified in the objectives
     * @throws SQLException if database management fails
     */
    public ResultSet getMaximumGainedTrip() throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT t.TripTo, t.DepartureDate, (t.NumDays * t.Ppday) as cost FROM (trip as t inner join hotel_trip_customer as htc on t.TripTo = htc.TripTo and t.DepartureDate = htc.DepartureDate) WHERE (t.NumDays * t.Ppday) > 0 " +
                "GROUP BY t.TripTo, t.DepartureDate " +
                "HAVING (cost * count(*))  >= all (" +
                "   SELECT (t2.NumDays * t2.Ppday) * count(*)" +
                "   FROM trip as t2 inner join hotel_trip_customer as htc2 on t2.TripTo = htc2.TripTo and t2.DepartureDate = htc2.DepartureDate" +
                "   WHERE (t2.NumDays * t2.Ppday) > 0" +
                "   GROUP BY t2.TripTo, t2.DepartureDate);");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");

        return rs;
    }

    /* HOTEL-TRIP RELATED */
    /**
     * Method to create the relationship between the hotel and the trip associated with it
     *
     * @param tripTo - The destination of the trip
     * @param departureDate - The date of the trip
     * @param hotelId - The id of the hotel
     * @throws SQLException
     */
    public void createHotelTrip(String tripTo, String departureDate, String hotelId) throws SQLException, UncompletedRequest, ParseException {
        try {
            //connector.getConnector().setAutoCommit(false);

            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO hotel_trip VALUES(?,?,?,default)");
            p.setString(1,tripTo);
            p.setDate(2,new Date(format.parse(departureDate).getTime()));
            p.setString(3,hotelId);
            p.executeUpdate();

           // connector.getConnector().commit();
        } catch (SQLException e) {
            System.out.println("Rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }

    }

    /**
     * Method to get the hotel trip relation that matches the information
     *
     * @param tripTo - The destination of the trip
     * @param departureDate - The date of the trip
     * @param hotelId - The id of the hotel
     * @return ResultSet - The Relation between the hotel and the trip
     * @throws SQLException if database management fails
     * @throws ParseException if date is not valid
     */
    public ResultSet getHotelTrip(String tripTo, String departureDate, String hotelId) throws SQLException, ParseException {

        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip WHERE TripTo=? and DepartureDate=? and HotelId=?;");
        p.setString(1,tripTo);
        p.setDate(2,new Date(format.parse(departureDate).getTime()));
        p.setString(3,hotelId);
        rs =  p.executeQuery();
        return rs;
    }



    /* TOUR-GUIDE RELATED */

    /**
     * Method to get a guide object by name and phone
     *
     * @param guidename String - Guide's name
     * @param guidephone String - Guide's phone
     * @return ResultSet - Set containing the guide that matches that conditions
     * @throws SQLException if database management fails
     */
    public ResultSet getGuide(String guidename, String guidephone) throws SQLException {

        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM tourguide WHERE guidename=? and guidephone=?; ");
        p.setString(1,guidename);
        p.setString(2,guidephone);
        rs = p.executeQuery();

        return rs;
    }



    /**
     * Method to get a guide object by id
     *
     * @param id String - Guide's id
     * @return ResultSet - Set containing the guide that matches that conditions
     * @throws SQLException if database management fails
     */
    public ResultSet getGuideById(String id) throws SQLException {
        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM tourguide WHERE guideid=?; ");
        p.setString(1,id);
        rs = p.executeQuery();
        return rs;
    }

    /**
     *
     * @param guideId
     * @param tripTo1
     * @param departureDate1
     * @throws SQLException
     * @throws ParseException
     */
    public void insertGuideInTrip(String guideId, String tripTo1, String departureDate1) throws SQLException, ParseException {
        try {
           // connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("UPDATE trip SET GuideId=? WHERE TripTo=? AND DepartureDate=?;");
            p.setString(1,guideId);
            p.setString(2,tripTo1);
            p.setDate(3,new Date(format.parse(departureDate1).getTime()));
            p.executeUpdate();

           // connector.getConnector().commit();

        } catch (SQLException e) {
            System.out.println("System rolling back");
            connector.getConnector().rollback();
            e.printStackTrace();
        }
    }


    /**
     * Method to insert a guide in the database by name and phone
     *
     * @param guidename String - Guide's name
     * @param guidephone String - Guide's phone
     * @throws UncompletedRequest if transaction could not be executed
     * @throws SQLException if database management fails
     */
    public void createGuide(String guidename, String guidephone) throws UncompletedRequest, SQLException {
        try {
            //connector.getConnector().setAutoCommit(false);
            PreparedStatement guidesNumber = connector.getConnector().prepareStatement("SELECT GuideId FROM tourguide ORDER BY GuideId;");
            ResultSet ids = guidesNumber.executeQuery();
            while(ids.next()){
                if(ids.isLast()){
                    break;
                }
            }
            currentGuideId = Integer.parseInt(ids.getString("GuideId"))+1;

            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO tourguide VALUES(?,?,?);");
            p.setString(1, String.valueOf(currentGuideId));
            p.setString(2,guidename);
            p.setString(3,guidephone);
            p.executeUpdate();

            //connector.getConnector().commit();
            System.out.println("Database updated and guide added succesfully!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }

    }



    /**
     * Method that says whether a guide is in a given trip or not
     * @return boolean - Whether the guide is in the trip or not
     * @throws ParseException if date format is not valid
     * @throws SQLException if database management fails
     */
    public boolean existGuideInTrip(String GuideId, String TripTo, String DepartureDate) throws ParseException, SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT * FROM trip WHERE GuideId=? AND TripTo=? AND DepartureDate=?;");
        stmt.setString(1,GuideId);
        stmt.setString(2,TripTo);
        stmt.setDate(3, new Date(format.parse(DepartureDate).getTime()));
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    /**
     *
     * @param choice
     * @param guidename1
     * @param guidename2
     * @param guidephone1
     * @param guidephone2
     * @param TripTo1
     * @param TripTo2
     * @param DepartureDate1
     * @param DepartureDate2
     * @throws SQLException
     * @throws UncompletedRequest
     * @throws ParseException
     */
    public void swapGuidesBetweenTrips(String choice, String guidename1, String guidename2, String guidephone1, String guidephone2, String TripTo1, String TripTo2, String DepartureDate1, String DepartureDate2) throws SQLException, UncompletedRequest, ParseException {
        try {
            connector.getConnector().setAutoCommit(false);

            //find first tourguide -> if not exists
            System.out.println("Finding the first tourguide");
            ResultSet guide1 = getGuide(guidename1,guidephone1);
            boolean exists = guide1.next();
            if(!exists && choice.equals("y")){
                System.out.println("System creating a guide");
                createGuide(guidename1, guidephone1);
            }else if(!exists && choice.equals("n")){
                System.out.println("Guide does not exist");
                System.out.println("Try again the transaction");
                close();
                throw new UncompletedRequest();
            }

            //find first trip
            System.out.println("Finding the first trip");
            ResultSet trip1 = getTrip(TripTo1,DepartureDate1);
            exists = trip1.next();
            //create trip if must
            if(!exists && choice.equals("y")) {
                System.out.println("System creating a trip");
                insertTrip(TripTo1,DepartureDate1);
                insertGuideInTrip(guide1.getString("GuideId"),trip1.getString("TripTo"),trip1.getString("DepartureDate"));
            //stop if trip does not exist and should not create it
            } else if(!exists && choice.equals("n")){
                System.out.println("Trip does not exist in the database");
                System.out.println("Try again the transaction");
                close();
                throw new UncompletedRequest();

            //stop if guide not in trip
            }else if(!existGuideInTrip(guide1.getString("GuideId"),TripTo1,DepartureDate1)){
                System.out.println("This guide is not in this trip!!!");
                throw new UncompletedRequest();
            }

            // find second tour-guide -> create if not exists
            System.out.println("Finding the second tour-guide");
            ResultSet guide2 = getGuide(guidename2,guidephone2);
            exists = guide2.next();
            //create guide if must
            if(!exists && choice.equals("y")){
                System.out.println("System creating a guide");
                createGuide(guidename2, guidephone2);

            //stop if guide not exists
            }else if(!exists && choice.equals("n")){
                System.out.println("Guide does not exist");
                System.out.println("Try again the transaction");
                close();
            }

            //find second trip
            System.out.println("Finding the second trip");
            ResultSet trip2 = getTrip(TripTo2,DepartureDate2);
            exists = trip2.next();
            // create trip if must
            if(!exists && choice.equals("y")){
                System.out.println("System creating a trip");
                insertTrip(TripTo2,DepartureDate2);
                insertGuideInTrip(guide2.getString("GuideId"),trip2.getString("TripTo"),trip2.getString("DepartureDate"));
            //stop if trip does not exist and should not create it
            }else if(!exists && choice.equals("n")){
                System.out.println("Trip does not exist in the database");
                System.out.println("Try again the transaction");
                close();
                throw new UncompletedRequest();
            //stop if due guide is not in the trip
            }else if(!existGuideInTrip(guide2.getString("GuideId"),TripTo2,DepartureDate2)){
                System.out.println("This guide is not in this trip!!!");
                throw new UncompletedRequest();
            }

            String guideId = guide1.getString("GuideId");
            String guideId1 = guide2.getString("GuideId");

            //swap guides
            PreparedStatement stmt1 = connector.getConnector().prepareStatement("UPDATE trip SET GuideId=? WHERE GuideId=? AND TripTo=? AND DepartureDate=?;");
            stmt1.setString(1,guideId1);
            stmt1.setString(2,guideId);
            stmt1.setString(3,TripTo1);
            stmt1.setDate(4,new Date(format.parse(DepartureDate1).getTime()));
            stmt1.executeUpdate();

            PreparedStatement stmt2 = connector.getConnector().prepareStatement("UPDATE trip SET GuideId=? WHERE GuideId=? AND TripTo=? AND DepartureDate=?;");
            stmt2.setString(1,guideId);
            stmt2.setString(2,guideId1);
            stmt2.setString(3,TripTo2);
            stmt2.setDate(4,new Date(format.parse(DepartureDate2).getTime()));
            stmt2.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Commited succesfully!");

        } catch (SQLException e) {
            System.out.println("System rolling back!");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        } catch (ParseException g){
            System.out.println("Databse rolling back");
            connector.getConnector().rollback();
            throw new ParseException("Please,enter a valid date",1);
        }
    }

    /**
     * Method that returns the number of customers a guide is responsible for
     *
     * @return ResultSet - A set with all guides, with each's number of customers they are responsible for
     * @throws SQLException if database mangement fails
     */
    public ResultSet retrieveNumCustomerGuideResponsible() throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT tg.GuideId, count(*) as num " +
                "FROM (trip AS t RIGHT JOIN tourguide AS tg ON t.GuideId = tg.GuideId) INNER JOIN hotel_trip_customer AS htc ON t.TripTo = htc.TripTo AND t.DepartureDate = htc.DepartureDate " +
                "GROUP BY tg.GuideId " +
                "ORDER BY tg.GuideId;");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");

        return rs;

    }



    /**
     * This method provides the tour-guides who have attended all trips of a given year.
     * @param year provided year
     * @throws SQLException if database management fails
     */
    public ResultSet getTourguidesAllTripsYear(String year) throws SQLException {
        String date1 =  year + "-01-01";
        String date2 =  year + "-12-31";

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT g.guideid as id, g.guidename as name " +
                "FROM tourguide as g WHERE not exists ( " +

                "SELECT * FROM trip as t where  t.departuredate between ? and ? and not exists ( " +
                "SELECT * from trip as t2 " +
                "where t2.tripto=t.tripto and t2.departuredate=t.departuredate and t.guideid=g.guideid)) " +
                "and exists (SELECT * FROM trip as t where  t.departuredate between ? and ?);");
        stmt.setString(1, date1);
        stmt.setString(2,date2);
        stmt.setString(3,date1);
        stmt.setString(4,date2);

        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");

        return rs;
    }

    /**
     * This method provides the tour-guides who speak all languages registered in the database
     * @return the tour-guides who speak all languages registered in the database
     * @throws SQLException if rollback fails
     */
    public ResultSet getTourguidesAllLanguages() throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT g.GuideId AS id, g.guidename AS name, COUNT(DISTINCT l.Lang) AS LangCount " +
                "FROM tourguide AS g INNER JOIN languages AS l ON g.GuideId=l.GuideId " +
                "GROUP BY g.GuideId " +
                "HAVING COUNT(DISTINCT l.Lang) = ( " +
                "SELECT COUNT(DISTINCT l2.Lang) FROM languages AS l2);");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");

        return rs;
    }

    /**
     * This method provides all tour-guides and their corresponding trips
     * @return all tour-guides and their corresponding trips
     * @throws SQLException if database management fails
     */
    public ResultSet getAllTourguideTrips() throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT g.GuideId AS id, g.guidename AS name, g.guidephone as phone, t.TripTo, t.DepartureDate " +
                "FROM tourguide AS g LEFT JOIN trip AS t ON g.GuideId=t.GuideId " +
                "ORDER BY t.DepartureDate ASC ");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");

        return rs;
    }

    /**
     * This method provides all tour-guides that have gone on trips and their corresponding trips
     * @return all tour-guides that have gone on trips and their corresponding trips
     * @throws SQLException if database management fails
     */
    public ResultSet getAllTourguideTripsNotNull() throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT g.GuideId AS id, g.guidename AS name, g.guidephone as phone, t.TripTo, t.DepartureDate " +
                "FROM tourguide AS g INNER JOIN trip AS t ON g.GuideId=t.GuideId " +
                "ORDER BY t.DepartureDate ASC ");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");


        return rs;
    }


    /**
     * This method updates a tourguide on the trips of a given interval of time
     * @param Guideidprev original tourguide id
     * @param Guideidnew new tourguide id
     * @param departuredate first date of interval
     * @param departuredate2 second date of interval
     * @throws SQLException if database management fails
     */
    public void updateTourguide(String Guideidprev, String Guideidnew, String departuredate, String departuredate2)
            throws SQLException, UncompletedRequest, NoChange, ParseException {

        try{
            connector.getConnector().setAutoCommit(false);
            PreparedStatement deleteStmt = connector.getConnector().prepareStatement("UPDATE trip SET guideid=? " +
                    "WHERE guideid=? AND departuredate BETWEEN ? AND ?;");

            deleteStmt.setString(1,Guideidnew);
            deleteStmt.setString(2,Guideidprev);
            deleteStmt.setDate(3,new Date(format.parse(departuredate).getTime()));
            deleteStmt.setDate(4, new Date(format.parse(departuredate2).getTime()));
            int changed = deleteStmt.executeUpdate();
            connector.getConnector().commit();

            if (changed==0) throw new NoChange();
            System.out.println("Transaction committed successfully!!");
            System.out.println("Guideid was changed successfully between the given dates!!");

        }catch(SQLException e){
            System.out.println("Transaction is being rolled back!");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }

    }







/*   PERSON RELATED */

    /**
     * This method provides whether a person exists
     * @param nameid name of the person
     * @param id id of the person
     * @return whether a person exists
     * @throws SQLException if the database management fails
     */
    public boolean personExists(String nameid, String id) throws SQLException {

        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM person WHERE nameid=? AND id=?;");
        p.setString(1,nameid);
        p.setString(2,id);
        rs = p.executeQuery();

        return rs.next();
    }


    /**
     * This method provides the name, age, id, liked dish and frequented restaurant of a person of a person
     * @param name name of the person
     * @param id id of the person
     * @return the due data of a person to be found
     * @throws SQLException if the database management fails
     */
    public ResultSet getPerson(String name, String id) throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT p.nameid, p.age, p.id, e.dish, f.restaurname FROM person as p LEFT JOIN eats as e " +
                "on e.nameid=p.nameid LEFT JOIN frequents as f on f.nameid=p.nameid WHERE p.nameid=? and p.id=?;");
        stmt.setString(1, name);
        stmt.setString(2, id);
        rs = stmt.executeQuery();

        return rs;

    }

    /**
     * This method provides all people
     * @return all people
     * @throws SQLException if database management fails
     */
    public ResultSet getAllPeople() throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT p.nameid, p.age, p.id, e.dish, f.restaurname FROM person as p LEFT JOIN eats as e " +
                "on e.nameid=p.nameid LEFT JOIN frequents as f on f.nameid=p.nameid;");

        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");

        return rs;

    }

    /**
     * This method aims to insert a person with their likings regarding food and restaurants
     * @param choice whether to create objects if they don't exist
     * @param name name of the person
     * @param age age of the person
     * @param id id of the person
     * @param food food liked by the person
     * @param restaurant restaurant liked by the person
     * @throws SQLException if rollback fails
     * @throws UncompletedRequest if transaction has not been successful
     */
    public void insertPersonRestaurantEats(String choice, String name, String age, String id, String food, String restaurant)
            throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);

            //check person exists -> create if must
            System.out.println("Try to add person...");
            insertPerson(name, age, id);

            // Check food exists -> create if must
            System.out.println("Check if food exists...");
            if (!foodExists(food)) {
                System.out.println("The dish does not exist");

                if (choice.equals("y")) {
                    System.out.println("Creating a new dish...");
                    insertDish(food);

                    // register food as eaten by the person
                    insertEats(name, food);
                }
            } else insertEats(name, food);


            // Check restaurant exists -> create if must
            System.out.println("Check if restaurant exists...");
            if (!restaurantExists(restaurant)) {
                System.out.println("The restaurant does not exist");

                if (choice.equals("y")) {
                    System.out.println("Creating a new restaurant...");
                    insertRestaurant(restaurant);

                    //  make person frequent the restaurant
                    addFrequents(name, restaurant);
                }
            } else addFrequents(name, restaurant);

            connector.getConnector().commit();
        } catch (SQLException e){
            System.out.println("System rolling back.");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     * This method inserts a person in the restaurant database
     * @param name name of the person
     * @param age age of the person
     * @param id id of the person
     * @throws SQLException if database management fails
     */
    public void insertPerson(String name, String age, String id) throws SQLException {
        PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO person VALUES (?,?,NULL,?);");
        p.setString(1,name);
        p.setString(2,age);
        p.setString(3,id);
        p.executeUpdate();
    }

    /**
     * This method deletes a person from the restaurants database
     * @param name String that represents the name of the person
     * @param id String that represents the id of the person
     * @throws SQLException if rollback could not be done
     * @throws UncompletedRequest if the transaction could not be completed
     * @throws NotBelong if the person does not belong to the database
     */
    public void deletePerson(String name, String id) throws SQLException, UncompletedRequest {
        try{
            connector.getConnector().setAutoCommit(false);

            //delete the person's data of frequented restaurants
            PreparedStatement deleteStmt0 = connector.getConnector().prepareStatement("DELETE FROM frequents as f WHERE f.nameid=?;");
            deleteStmt0.setString(1, name);
            deleteStmt0.executeUpdate();

            //delete the person's data of liked food
            PreparedStatement deleteStmt1 = connector.getConnector().prepareStatement("DELETE FROM eats as e WHERE e.nameid=?;");
            deleteStmt1.setString(1, name);
            deleteStmt1.executeUpdate();

            //delete the person's data of menu orders
            PreparedStatement deleteStmt3 = connector.getConnector().prepareStatement("DELETE FROM menu_order as m WHERE m.customer_id=?;");
            deleteStmt3.setString(1, id);
            deleteStmt3.executeUpdate();

            //delete the person's personal data
            PreparedStatement deleteStmt = connector.getConnector().prepareStatement("DELETE FROM person as p WHERE p.nameid=? and p.id=?;");
            deleteStmt.setString(1, name);
            deleteStmt.setString(2, id);
            deleteStmt.executeUpdate();

            connector.getConnector().commit();

            System.out.println("Person was deleted successfully!!");

        }catch(SQLException e){
            System.out.println("Transaction is being rolled back!");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }

    }


    /* MENU AND MENU-ORDERS RELATED */


    /**
     * Method that adds a menu order
     * @param menu_mtype menu type
     * @param menu_id menu identifier
     * @param customer_id customer id
     * @throws SQLException if database management fails
     */
    public void addMenuOrder(String choice, String menu_mtype, String menu_id, String customer_id) throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);

            //Check if menu exists -> create if must
            ResultSet menu = getMenu(menu_id, customer_id);
            if(!menu.next()){
                System.out.println("The menu does not exist");
                if(choice.equals("y")) {
                    System.out.println("Creating a new menu...");
                    insertMenu(menu_mtype, menu_id);
                }
            }

            // Insert menu order
            System.out.println("Creating a new menu order...");
            insertMenuOrder(menu_mtype, menu_id, customer_id);

            connector.getConnector().commit();
            System.out.println("Menu order registered!!");

        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     * This method aims to insert a menu
     * @param menu_mtype menu type
     * @param menu_id menu identifier
     * @throws SQLException if database management fails
     */
    private void insertMenu(String menu_mtype, String menu_id) throws SQLException {
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO menu VALUES (?,?,default);");
            p.setString(1, menu_mtype);
            p.setString(2, menu_id);
            p.executeUpdate();

    }

    /**
     * This method aims to insert a menu order
     * @param menu_mtype menu type
     * @param menu_id menu identifier
     * @param customer_id customer identifier
     * @throws SQLException if database management fails
     */
    private void insertMenuOrder(String menu_mtype, String menu_id, String customer_id) throws SQLException {
        PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO menu_order VALUES (default,?,?,?);");
        p.setString(1,menu_mtype);
        p.setString(2,menu_id);
        p.setString(3,customer_id);
        p.executeUpdate();
    }

    /**
     * This method provides a menu
     *
     * @param menu_mtype String that represents the type of menu
     * @param menu_id String that represents the identifier of the menu
     * @return the menu to be provided
     * @throws SQLException if rollback fails
     */
        public ResultSet getMenu(String menu_mtype, String menu_id) throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT * FROM menu WHERE mtype=? AND mid=?;");
        stmt.setString(1,menu_mtype);
        stmt.setString(2,menu_id);
        rs = stmt.executeQuery();
        return rs;
    }

    /**
     * This method provides all menu-orders
     *
     * @return all menu-orders
     * @throws SQLException if rollback fails
     */
    public ResultSet getAllMenuOrders() throws SQLException {
        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT * FROM menu_order;");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");
        return rs;
    }

    /* DISH RELATED */

    /**
     * This method adds  a dish to the database
     *
     * @param food String that represents a certain dish
     * @throws SQLException if rollback fails
     */
    public void insertDish(String food) throws SQLException {
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO dishes VALUES (?, default, default, default);");
            p.setString(1,food);
            p.executeUpdate();
    }

    /**
     * This method provides whether a certain dish exists.
     *
     * @param food String that represents a certain dish
     * @return whether a certain dish exists.
     * @throws SQLException if database management fails
     */
    public boolean foodExists(String food) throws SQLException {
        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM dishes as d WHERE d.dish=?;");
        p.setString(1,food);
        rs = p.executeQuery();
        return rs.next();
    }

    /**
     * This method updates a given dishes' price to its half
     *
     * @param dish provided dish
     * @throws SQLException if rollback fails
     */
    public void updateDishPrice(String dish) throws SQLException, UncompletedRequest, NoChange {
        try{
            //execute transaction
            connector.getConnector().setAutoCommit(false);
            PreparedStatement updateStmt = connector.getConnector().prepareStatement("UPDATE serves SET price=(0.5*price) WHERE dish=?;");
            updateStmt.setString(1,dish);
            int changed = updateStmt.executeUpdate();
            connector.getConnector().commit();

            //final checks
            System.out.println("Transaction committed successfully!!");
            if(changed==0) throw new NoChange();
            System.out.println("Prices were updated successfully!!");

        }catch(SQLException e){
            System.out.println("Transaction is being rolled back!");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     * This method provides all dishes
     * @return all dishes
     * @throws SQLException if database management fails
     */
    public ResultSet getAllDishes() throws SQLException {

            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT s.dish, s.restaurname, s.price FROM serves as s;");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");
            return rs;
    }


/* RESTAURANT RELATED */

    /**
     * This method provides whether a restaurant exists
     * @param restaurant given restaurant
     * @return whether a restaurant exists
     * @throws SQLException if database management fails
     */
    public boolean restaurantExists(String restaurant) throws SQLException {
        PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM restaurant as r WHERE r.restaurname=?;");
        p.setString(1,restaurant);
        rs = p.executeQuery();
        return rs.next();
    }


    /**
     * This method creates a restaurant
     * @param restaurant given restaurant
     * @throws SQLException if database management fails
     */
    public void insertRestaurant(String restaurant) throws SQLException {
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO restaurant VALUES (?,default, default, default, default);");
            p.setString(1,restaurant);
            p.executeUpdate();
    }


    /**
     * This method gets the restaurants that provide food liked by all managers
     * @return the restaurants that provide food liked by all managers
     * @throws SQLException if rollback fails
     */
    public ResultSet getRestaurantLikedManagers() throws SQLException {

        connector.getConnector().setAutoCommit(false);
        PreparedStatement stmt = connector.getConnector().prepareStatement("select distinct r.restaurname as restaurant, s.dish as dish from serves as s inner join restaurant as r on r.restaurname=s.restaurname inner join (select distinct e.dish from eats as e  where not exists (select * from person as p inner join department as d on p.id=d.Mgr_ssn where not exists (select * from eats as e2 where e2.nameid=p.nameid and e2.dish=e.dish )) and exists (select * from person as p inner join department as d on p.id=d.Mgr_ssn)) as t2 on t2.dish=s.dish where  (r.capacity>=(select count(distinct d2.Mgr_ssn) from department as d2));");
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");

        return rs;
    }


    /* EMPLOYEE-RELATED */


    /**
     * This method provides the employees that frequent a single restaurant in the given city
     * @param city String - the given city
     * @return the employees that frequent a single restaurant in the given city
     * @throws SQLException if database management fails
     */
    public ResultSet getEmployee1RestCity(String city) throws SQLException {

        PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT p.id, e.fname , e.lname  " +
                "FROM frequents as f inner join (person as p inner join employee as e on e.ssn=p.id) ON f.nameid=p.nameid " +
                "inner join restaurant as r on r.restaurname=f.restaurname " +
                " WHERE  r.city=? " +
                " GROUP BY f.nameid " +
                " HAVING count(distinct f.restaurname)=1; ");
        stmt.setString(1,city);
        rs = stmt.executeQuery();
        System.out.println("Query executed correctly!!");
        return rs;
    }



    /* PREFERENCES RELATED */

    /**
     * This method inserts a person's eating preference
     * @param name person's name
     * @param food food to set as liked
     * @throws SQLException if database management fails
     */
    public void insertEats(String name, String food) throws SQLException {
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO eats VALUES (?,?);");
            p.setString(1,name);
            p.setString(2,food);
            p.executeUpdate();
    }

    /**
     * This method registers a person's frequented restaurant as such
     * @param name person's name
     * @param restaurant due restaurant
     * @throws SQLException if database management fails
     */
    public void addFrequents(String name, String restaurant) throws SQLException {
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO frequents VALUES (?,?);");
            p.setString(1,name);
            p.setString(2,restaurant);
            p.executeUpdate();
    }

    /**
     * This method gives a raise of 1000 dollars to the employees who are paid less than
     * the average salary of the company. Also, if they have any dependent they get 100 dollars
     * per dependent
     */
    public void rises(){

        try {
            connector.getConnector().setAutoCommit(false);

            PreparedStatement p = connector.getConnector().prepareStatement("SELECT Ssn, Salary FROM employee WHERE Salary < (" +
                    "SELECT AVG(Salary) FROM employee )");
            rs = p.executeQuery();

            System.out.println("Employees with salaries under the avg:");

            while (rs.next()){

                float salary = Float.parseFloat(rs.getString("Salary"));
                String id = rs.getString("Ssn");
                salary+=1000;

                System.out.println("\tSsn: "+id+" recieves a 1000 dollar raise");

                PreparedStatement p2 = connector.getConnector().prepareStatement("SELECT Ssn FROM dependent AS d, employee AS e WHERE " +
                        "d.Essn=e.Ssn AND Ssn=?");
                p2.setString(1,id);
                ResultSet rs2 = p2.executeQuery();

                int k=0;
                while(rs2.next()) {
                    k++;
                }

                salary+=100*k;

                if(k>0)
                    System.out.println("\t\tWith "+k+" dependants they also recieve "+k*100+" more dollars");

                PreparedStatement updateSalary = connector.getConnector().prepareStatement("UPDATE employee SET salary=? WHERE Ssn=?;");
                updateSalary.setString(1,Float.toString(salary));
                updateSalary.setString(2,id);
                updateSalary.executeUpdate();

                connector.getConnector().commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * This query retrieves couples of names and a restaurant of people who frequent the same
     * restaurant, have at least a liked dish in common and that restaurant serves it
     */
    public ResultSet resDates(){

        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT f1.nameId as nId, f2.nameId, f1.restaurname " +
                    "FROM frequents as f1, frequents as f2 " +
                    "WHERE f1.restaurname=f2.restaurname AND f1.nameId<>f2.nameId AND " +
                    "EXISTS (SELECT * FROM eats as e1, eats as e2 WHERE e1.dish=e2.dish AND f1.nameId=e1.nameId AND f2.nameId=e2.nameId AND " +
                    "EXISTS (SELECT * FROM serves as s WHERE s.restaurname=f1.restaurname AND s.dish=e1.dish))");
            rs = p.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;

    }

    /**
     * This method returns the employees that have been to the same hotels as the company CEO
     * @return The result of the query
     * @throws SQLException
     */
    public ResultSet hotelsCEO() throws SQLException {
        PreparedStatement p = connector.getConnector().prepareStatement("SELECT employee.Ssn, employee.Fname, employee.Lname\n" +
                "FROM employee INNER JOIN employee_customer ON Ssn=Emp_id " +
                "WHERE NOT EXISTS ( " +
                "    SELECT * " +
                "    FROM ( " +
                "        SELECT htc.HotelId " +
                "        FROM employee AS e, employee_customer AS ec, hotel_trip_customer AS htc " +
                "WHERE htc.CustomerId=ec.Cust_Id AND e.Ssn=ec.Emp_id AND e.Super_ssn=\"\" " +
                "    ) AS e2 " +
                "    WHERE NOT EXISTS ( " +
                "        SELECT htc3.HotelId " +
                "        FROM hotel_trip_customer as htc3 " +
                "        WHERE htc3.CustomerId=employee_customer.Cust_Id AND htc3.HotelId=e2.HotelId " +
                "        ) " +
                "    ) AND employee.Super_ssn<>\"\";");

        rs = p.executeQuery();

        return rs;
    }

    public String insertHotelTrip(String to, String date) throws SQLException, NoHotel, UncompletedRequest {

        Vector<String> trips = new Vector<>();

        PreparedStatement p = connector.getConnector().prepareStatement("SELECT h.HotelId FROM hotel AS h " +
                "WHERE h.hotelcity=? ORDER BY h.hotelcapacity DESC");
        p.setString(1, to);
        rs = p.executeQuery();
        String hotelId;
        if (rs.next())
            hotelId = rs.getString("HotelId");
        else
            throw new NoHotel();

        System.out.println("Selected hotel: "+hotelId);

        try {

            if (tripExists(to, date, hotelId)) {
                System.out.println("Trip is already created");
            } else {
                connector.getConnector().setAutoCommit(false);
                PreparedStatement p2 = connector.getConnector().prepareStatement("INSERT INTO hotel_trip VALUES (?,?,?,default);");
                p2.setString(1, to);
                p2.setString(2, date);
                p2.setString(3, hotelId);
                p2.executeUpdate();

                System.out.println("Trip registered");

                connector.getConnector().commit();
            }
        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }

        return hotelId;

    }

    public ResultSet insertedTrips(String to, String date) throws SQLException {
        PreparedStatement p = connector.getConnector().prepareStatement("SELECT e.Ssn, e.Fname, e.Lname, ht.TripTo, ht.DepartureDate, ht.HotelId " +
                "FROM hotel_trip_customer AS ht, employee_customer as ec, employee as e WHERE ht.TripTo=? AND ht.DepartureDate=? AND " +
                "e.Ssn=ec.Emp_id AND ec.Cust_Id=ht.CustomerId");
        p.setString(1,to);
        p.setString(2,date);
        rs = p.executeQuery();
        return rs;
    }

    public void insertCustomerToTrip(String to, String date, String hotelId, String custId) throws SQLException {
        try {

            if(customerAlreadyInTrip(to,date,hotelId,custId)){
                System.out.println("Trip is already created");
            }else{
                connector.getConnector().setAutoCommit(false);
                PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO hotel_trip_customer VALUES (?,?,?,?,?);");
                p.setString(1,to);
                p.setString(2,date);
                p.setString(3,hotelId);
                p.setString(4,custId);
                p.setString(5,"2");
                p.executeUpdate();

                connector.getConnector().commit();
                System.out.println("Customer registered successfully!!");
            }
        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            e.printStackTrace();
        }
    }

    public boolean customerAlreadyInTrip(String to, String date, String hotelId, String custId) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip_customer AS ht WHERE ht.TripTo=? AND " +
                    "ht.DepartureDate=? AND ht.hotelId=? AND ht.CustomerId=?");
            p.setString(1,to);
            p.setString(2,date);
            p.setString(3,hotelId);
            p.setString(4,custId);
            rs = p.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs.next();

    }

    public boolean tripExists(String to, String date, String hotel) throws SQLException {

        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip AS ht WHERE ht.TripTo=? AND " +
                    "ht.DepartureDate=? AND ht.HotelId=?");
            p.setString(1,to);
            p.setString(2,date);
            p.setString(3,hotel);
            rs = p.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return rs.next();
    }

    public ResultSet employeesNotInTheDepartment(String dno){
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT ec.Cust_Id FROM employee AS e INNER JOIN " +
                    "employee_customer AS ec ON e.Ssn=ec.Emp_id WHERE e.Dno<>?");
            p.setString(1,dno);
            rs = p.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet hotelsIn (String location){

        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT h.HotelId FROM hotel AS h " +
                    "WHERE h.hotelcity=? ORDER BY h.hotelcapacity DESC");
            p.setString(1,location);
            rs = p.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * This method returns all the departments and locations
     * @return the department numbers and locations
     */
    public ResultSet deptAndLocation(){
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT dl.Dnumber, dl.Dlocation FROM dept_locations AS dl ");
            rs = p.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * Get all the employees in the database and their salaries
     * @return the ssn and salary of all employees
     */
    public ResultSet getSalaries(){
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT Ssn, Salary FROM employee");
            rs = p.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

}
