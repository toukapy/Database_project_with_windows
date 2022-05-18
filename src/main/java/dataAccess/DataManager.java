package dataAccess;

import exceptions.NoChange;
import exceptions.UncompletedRequest;

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

    /**
     * MEthod that opens a connection with the Database connector
     */
    public void open(){
        connector = new DatabaseConnector();
        stmt = connector.getStatement();
    }

    /**
     * Returns a Customer by phone and name
     * @param custname String - The name of the customer
     * @param custphone String - the phone of the customer
     * @return ResultSet - The set of results that matches the information
     * @throws SQLException
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
            connector.getConnector().setAutoCommit(false);
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

            connector.getConnector().commit();
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
     * @throws ParseException
     */
    public ResultSet getCustomerTrip(String trip, String departure) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        PreparedStatement stmt = null;
        try {
            stmt = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip_customer as htc INNER JOIN customer as c ON htc.CustomerId = c.CustomerId WHERE TripTo=? AND DepartureDate=?;");
            stmt.setString(1,trip);
            stmt.setDate(2,new Date(dateFormat.parse(departure).getTime()));
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;

    }

    /* HOTEL-RELATED */
    /**
     * Method to get the hotel object of a given hotel
     *
     * @param hotelname - The name of the hotel
     * @param hotelcity - The city where the hotel is
     * @return ResultSet - The hotels matching that information
     * @throws SQLException
     */
    public ResultSet getHotel(String hotelname, String hotelcity) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel WHERE hotelname=? and hotelcity=?;");
            p.setString(1,hotelname);
            p.setString(2,hotelcity);
            rs =  p.executeQuery();
        } catch (SQLException e) {
            System.out.println("System is rolling back");
            connector.getConnector().rollback();
            rs = null;
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * Method to insert a hotel in the hotel table
     *
     * @param hotelname - The name of the hotel
     * @param hotelcity - The city where the hotel is
     */
    public void insertHotel(String hotelname, String hotelcity) throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
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

            connector.getConnector().commit();
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
     * @throws SQLException
     */
    public ResultSet getTrip(String tripTo, String departureDate) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM trip WHERE TripTo=? and DepartureDate=?;");
            p.setString(1,tripTo);
            p.setString(2,departureDate);
            rs =  p.executeQuery();
        } catch (SQLException e) {
            System.out.println("System is rolling back");
            connector.getConnector().rollback();
            rs = null;
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * Method to insert a trip into the trip table
     *
     * @param tripTo - Destination of the trip
     * @param departureDate - Date of the trip
     */
    public void insertTrip(String tripTo, String departureDate) throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO trip VALUES (?,?,default,default,default,default);");
            p.setString(1,tripTo);
            p.setString(2,departureDate);
            p.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Trip added successfully!!");

        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     * Method to create the relationship between the hotel and the trip associated with it
     *
     * @param tripTo - The destination of the trip
     * @param departureDate - The date of the trip
     * @param hotelId - The id of the hotel
     * @throws SQLException
     */
    public void createHotelTrip(String tripTo, String departureDate, String hotelId) throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);

            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO hotel_trip VALUES(?,?,?,default)");
            p.setString(1,tripTo);
            p.setString(2,departureDate);
            p.setString(3,hotelId);
            p.executeUpdate();

            connector.getConnector().commit();
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
     * @throws SQLException
     */
    public ResultSet getHotelTrip(String tripTo, String departureDate, String hotelId) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip WHERE TripTo=? and DepartureDate=? and HotelId=?;");
            p.setString(1,tripTo);
            p.setString(2,departureDate);
            p.setString(3,hotelId);
            rs =  p.executeQuery();
        } catch (SQLException e) {
            System.out.println("System is rolling back");
            connector.getConnector().rollback();
            rs = null;
            e.printStackTrace();
        }

        return rs;
    }


    /* TOUR-GUIDE RELATED */

    /**
     * Method to get a guide object by name and phone
     *
     * @param guidename String - Guide's name
     * @param guidephone String - Guide's phone
     * @return ResultSet - Set containing the guide that matches that conditions
     * @throws SQLException
     */
    public ResultSet getGuide(String guidename, String guidephone) throws SQLException {

        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM tourguide WHERE guidename=? and guidephone=?; ");
            p.setString(1,guidename);
            p.setString(2,guidephone);
            rs = p.executeQuery();
        } catch (SQLException e) {
            System.out.println("System is rolling back!");
            connector.getConnector().rollback();
            rs = null;
            e.printStackTrace();
        }
        return rs;
    }



    /**
     * Method to get a guide object by id
     *
     * @param id String - Guide's id
     * @return ResultSet - Set containing the guide that matches that conditions
     */
    public ResultSet getGuideById(String id) {

        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM tourguide WHERE guideid=?; ");
            p.setString(1,id);
            rs = p.executeQuery();
            return rs;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Method to insert a guide in the database by name and phone
     *
     * @param guidename String - Guide's name
     * @param guidephone String - Guide's phone
     */
    public void createGuide(String guidename, String guidephone) throws UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
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

            connector.getConnector().commit();
            System.out.println("Database updated and guide added succesfully!!");

        } catch (SQLException e) {
            throw new UncompletedRequest();
        }

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            connector.getConnector().setAutoCommit(false);
            PreparedStatement deleteStmt = connector.getConnector().prepareStatement("DELETE FROM hotel_trip_customer WHERE CustomerId=? and TripTo=? and DepartureDate=?;");

            deleteStmt.setString(1,CustomerId);
            deleteStmt.setString(2,TripTo);
            deleteStmt.setDate(3,new Date(dateFormat.parse(DepartureDate).getTime()));
            deleteStmt.executeUpdate();

            connector.getConnector().commit();

            System.out.println("Transaction commited succesfully!!");
            System.out.println("Customer was deleted from trip succesfully!!");

        }catch(SQLException e){
            System.out.println("Transaction is being rolled back!");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }

    }

    /**
     * Method to add a customer to the trip table
     *
     * @param CustomerId String - customer's id
     * @param TripTo String - Trip's destination
     * @param DepartureDate String - Trip's date
     * @param HotelId String - Hotel's id
     */
    public void addCustomerToTrip(String CustomerId, String TripTo, String DepartureDate, String HotelId) throws SQLException, UncompletedRequest {
        try {

            if(customerExistsInTrip(CustomerId,TripTo,DepartureDate,HotelId)){
                System.out.println("Customer already in trip !!");
            }else{
                connector.getConnector().setAutoCommit(false);
                PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO hotel_trip_customer VALUES (?,?,?,?,default);");
                p.setString(1,TripTo);
                p.setString(2,DepartureDate);
                p.setString(3,HotelId);
                p.setString(4,CustomerId);
                p.executeUpdate();

                connector.getConnector().commit();
                System.out.println("Customer added successfully to the trip!!");
            }
        } catch (SQLException e) {
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
     * @return boolean - Whether or not the customer is in the trip
     * @throws SQLException if rollback fails
     */
    private boolean customerExistsInTrip(String customerId, String tripTo, String departureDate, String hotelId) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip_customer WHERE CustomerId=? and TripTo=? and DepartureDate=? and HotelId=?;");
            p.setString(1,customerId);
            p.setString(2,tripTo);
            p.setString(3,departureDate);
            p.setString(4,hotelId);
            rs = p.executeQuery();

        } catch (SQLException e) {
            rs = null;
            e.printStackTrace();
        }
        return rs.next();
    }

    /**
     * Method to see whether a customer exists in a trip or not, but without specifying the hotel
     *
     * @param customerId String - Customer's id
     * @param TripTo String - Trip's destination
     * @param DepartureDate String - Trip's date
     * @return boolean- Whether the customer exists in the trip or not
     * @throws SQLException if rollback fails
     */
    public boolean customerExistsInTripWithoutHotel(String customerId, String TripTo, String DepartureDate) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM hotel_trip_customer WHERE CustomerId=? and TripTo=? and DepartureDate=?;");
            p.setString(1,customerId);
            p.setString(2,TripTo);
            p.setString(3,DepartureDate);
            rs = p.executeQuery();

        } catch (SQLException e) {
            rs = null;
            e.printStackTrace();
        }
        return rs.next();
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

    /**
     * Method that retrieves the trip (TripTo, DepartureDate) that has gained the maximum amount of money
     *
     * @return ResultSet - The set that has the object specified in the objectives
     * @throws SQLException if rollback fails
     * @throws UncompletedRequest if there has been a problem during the execution of the query
     */
    public ResultSet getMaximumGainedTrip() throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT t.TripTo, t.DepartureDate, (t.NumDays * t.Ppday) as cost FROM (trip as t inner join hotel_trip_customer as htc on t.TripTo = htc.TripTo and t.DepartureDate = htc.DepartureDate) WHERE (t.NumDays * t.Ppday) > 0 " +
                    "GROUP BY t.TripTo, t.DepartureDate " +
                    "HAVING (cost * count(*))  >= all (" +
                    "   SELECT (t2.NumDays * t2.Ppday) * count(*)" +
                    "   FROM trip as t2 inner join hotel_trip_customer as htc2 on t2.TripTo = htc2.TripTo and t2.DepartureDate = htc2.DepartureDate" +
                    "   WHERE (t2.NumDays * t2.Ppday) > 0" +
                    "   GROUP BY t2.TripTo, t2.DepartureDate);");
            rs = stmt.executeQuery();
            connector.getConnector().commit();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            throw new UncompletedRequest();
        }
        return rs;
    }

    /**
     * Method that returns a set that has all customers in trips with optional excursions
     *
     * @return ResultSet - A set with customer's in all trips with optional excursions
     * @throws UncompletedRequest if there has been a problem during the execution of the query
     */
    public ResultSet retrieveCustomerEveryTripExc() throws UncompletedRequest {
        try {
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT c.custname, c.custphone, c.CustomerId FROM customer as c WHERE NOT EXISTS(" +
                    "SELECT * FROM trip as t WHERE NOT EXISTS(" +
                    "SELECT * FROM excur_opt_customer as eoc WHERE eoc.CustomerId = c.CustomerId AND eoc.TripTo = t.TripTo AND eoc.DepartureDate = t.DepartureDate));");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!");

        } catch (SQLException e) {
            throw new UncompletedRequest();
        }

        return rs;
    }

    /**
     * Method that swaps two guides between two trips
     *
     * @param guideId String - First guide's id
     * @param guideId1 String - Second guide's id
     * @param tripTo1 String - First trip's destination
     * @param tripTo2 String - Second trip's destination
     * @param departureDate1 String - First trip's date
     * @param departureDate2 String - Second trip's date
     * @throws SQLException
     */
    public void swapGuidesBetweenTrips(String guideId, String guideId1, String tripTo1, String tripTo2, String departureDate1, String departureDate2) throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt1 = connector.getConnector().prepareStatement("UPDATE trip SET GuideId=? WHERE GuideId=? AND TripTo=? AND DepartureDate=?;");
            stmt1.setString(1,guideId1);
            stmt1.setString(2,guideId);
            stmt1.setString(3,tripTo1);
            stmt1.setString(4,departureDate1);
            stmt1.executeUpdate();

            PreparedStatement stmt2 = connector.getConnector().prepareStatement("UPDATE trip SET GuideId=? WHERE GuideId=? AND TripTo=? AND DepartureDate=?;");
            stmt2.setString(1,guideId);
            stmt2.setString(2,guideId1);
            stmt2.setString(3,tripTo2);
            stmt2.setString(4,departureDate2);
            stmt2.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Commited succesfully!");

        } catch (SQLException e) {
            System.out.println("System rolling back!");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     * Method that returns the number of customers a guide is responsible for
     *
     * @return ResultSet - A set with all guides, with each's number of customers they are responsible for
     * @throws SQLException
     */
    public ResultSet retrieveNumCustomerGuideResponsible() throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT tg.GuideId, count(*) as num " +
                    "FROM (trip AS t RIGHT JOIN tourguide AS tg ON t.GuideId = tg.GuideId) INNER JOIN hotel_trip_customer AS htc ON t.TripTo = htc.TripTo AND t.DepartureDate = htc.DepartureDate " +
                    "GROUP BY tg.GuideId " +
                    "ORDER BY tg.GuideId;");
            rs = stmt.executeQuery();
            connector.getConnector().commit();
            System.out.println("Query executed correctly!!");
        } catch (SQLException e) {
            System.out.println("System rolling back!!");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }

        return rs;

    }

    /**
     * This method gets the customers who have attended at least all cheapest trips attended by customers
     * @return the customers who have attended at least all cheapest trips attended by customers
     * @throws UncompletedRequest if query could not be executed
     * @throws SQLException if rollback fails
     */
    public ResultSet getCustomersAllCheapestTrips() throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT c.customerid as id, c.custname as name " +
                    "FROM customer as c WHERE not exists ( " +
                    "SELECT * FROM trip as t where  (t.Ppday*t.numdays)=(SELECT min(t2.ppday*t2.numdays) from trip as t2) and not exists ( " +
                    "SELECT * from hotel_trip_customer as htc " +
                    "where htc.tripto=t.tripto and htc.departuredate=t.departuredate and htc.customerid=c.customerid)) " +
                    "and exists (SELECT * FROM trip as t where (t.Ppday*t.numdays)=(SELECT min(t2.ppday*t2.numdays) from trip as t2));");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
            throw new UncompletedRequest();
        }
        return rs;
    }

    /**
     * This method provides the tour-guides who have attended all trips of a given year.
     * @param year provided year
     * @throws UncompletedRequest if query could not be executed
     * @throws SQLException if rollback fails
     */
    public ResultSet getTourguidesAllTripsYear(String year) throws SQLException, UncompletedRequest {
        try {
            String date1 =  year + "-01-01";
            String date2 =  year + "-12-31";
            connector.getConnector().setAutoCommit(false);
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

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
            throw new UncompletedRequest();
        }
        return rs;
    }

    /**
     * This method provides the tour-guides who speak all languages registered in the database
     * @return the tour-guides who speak all languages registered in the database
     * @throws UncompletedRequest if query could not be executed
     * @throws SQLException if rollback fails
     */
    public ResultSet getTourguidesAllLanguages() throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT g.GuideId AS id, g.guidename AS name, COUNT(DISTINCT l.Lang) AS LangCount " +
                    "FROM tourguide AS g INNER JOIN languages AS l ON g.GuideId=l.GuideId " +
                    "GROUP BY g.GuideId " +
                    "HAVING COUNT(DISTINCT l.Lang) = ( " +
                    "SELECT COUNT(DISTINCT l2.Lang) FROM languages AS l2);");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
            throw new UncompletedRequest();
        }
        return rs;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public ResultSet getAllTourguideTrips() throws SQLException {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT g.GuideId AS id, g.guidename AS name, g.guidephone as phone, t.TripTo, t.DepartureDate " +
                    "FROM tourguide AS g LEFT JOIN trip AS t ON g.GuideId=t.GuideId " +
                    "ORDER BY t.DepartureDate ASC ");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
        }
        return rs;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public ResultSet getAllTourguideTripsNotNull() throws SQLException {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT g.GuideId AS id, g.guidename AS name, g.guidephone as phone, t.TripTo, t.DepartureDate " +
                    "FROM tourguide AS g INNER JOIN trip AS t ON g.GuideId=t.GuideId " +
                    "ORDER BY t.DepartureDate ASC ");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
        }
        return rs;
    }


    /**
     *
     * @param Guideidprev
     * @param Guideidnew
     * @param departuredate
     * @param departuredate2
     * @throws SQLException
     */
    public void updateTourguide(String Guideidprev, String Guideidnew, String departuredate, String departuredate2) throws SQLException, UncompletedRequest, NoChange {

        try{
            connector.getConnector().setAutoCommit(false);
            PreparedStatement deleteStmt = connector.getConnector().prepareStatement("UPDATE trip SET guideid=? WHERE guideid=? AND departuredate BETWEEN ? AND ?;");

            deleteStmt.setString(1,Guideidnew);
            deleteStmt.setString(2,Guideidprev);
            deleteStmt.setString(3,departuredate);
            deleteStmt.setString(4, departuredate2);
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

    /**
     *
     * @param custname
     * @param custphone
     * @param hotelname
     * @param hotelcity
     * @param tripTo
     * @param departureDate
     * @return
     * @throws SQLException
     */
    public ResultSet getCustomerTripHotel(String custname, String custphone, String hotelname, String hotelcity, String tripTo, String departureDate) throws SQLException {
        try {

            ResultSet customer = getCustomer(custname, custphone);
            ResultSet hotel = getHotel(hotelname,hotelcity);

            if(customer.next() && hotel.next()){
                PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT * FROM (hotel_trip_customer as htc INNER JOIN customer as c on htc.CustomerId=c.CustomerId) INNER JOIN hotel as h ON htc.HotelId=h.HotelId  WHERE htc.CustomerId=? AND htc.HotelId=? AND htc.TripTo=? AND htc.DepartureDate=?;");
                stmt.setString(1,customer.getString("CustomerId"));
                stmt.setString(2,hotel.getString("HotelId"));
                stmt.setString(3,tripTo);
                stmt.setString(4,departureDate);
                rs = stmt.executeQuery();
                return rs;
            }

        } catch (SQLException e) {
            System.out.println("System rolling back");
            connector.getConnector().rollback();
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @return
     * @throws SQLException
     */
    public ResultSet getAllCustomers() throws SQLException {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT htc.TripTo, htc.DepartureDate, h.hotelname, h.hotelcity, c.custname, c.custphone " +
                    "FROM hotel_trip_customer AS htc INNER JOIN hotel AS h ON htc.HotelId=h.HotelId " +
                    "INNER JOIN customer AS c ON htc.CustomerId=c.CustomerId;");

            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
        }
        return rs;

    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public ResultSet getAllCustomersJustTrip() throws SQLException {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT DISTINCT htc.TripTo, htc.DepartureDate " +
                    "FROM hotel_trip_customer AS htc " +
                    "INNER JOIN customer AS c ON htc.CustomerId=c.CustomerId;");

            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
        }
        return rs;

    }





/*   PERSON RELATED */

    /**
     *
     * @param nameid
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean personExists(String nameid, String id) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM person WHERE nameid=? AND id=?;");
            p.setString(1,nameid);
            p.setString(2,id);
            rs = p.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs.next();
    }


    /**
     *
     * @param name
     * @param id
     * @return
     * @throws SQLException
     */
    public ResultSet getPerson(String name, String id) throws SQLException {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT p.nameid, p.age, p.id, e.dish, f.restaurname FROM person as p LEFT JOIN eats as e " +
                    "on e.nameid=p.nameid LEFT JOIN frequents as f on f.nameid=p.nameid WHERE p.nameid=? and p.id=?;");
            stmt.setString(1, name);
            stmt.setString(2, id);
            rs = stmt.executeQuery();

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
        }
        return rs;

    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public ResultSet getAllPeople() throws SQLException {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT p.nameid, p.age, p.id, e.dish, f.restaurname FROM person as p LEFT JOIN eats as e " +
                    "on e.nameid=p.nameid LEFT JOIN frequents as f on f.nameid=p.nameid;");

            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");
            if(rs==null)  System.out.println("No matchings");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
        }
        return rs;

    }

    /**
     *
     * @param name
     * @param age
     * @param id
     * @throws SQLException
     */
    public void insertPerson(String name, String age, String id) throws SQLException, UncompletedRequest {
        try {

            if(personExists(name, id)){
                System.out.println("Person already registered !!");
            }else{
                connector.getConnector().setAutoCommit(false);
                PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO person VALUES (?,?,NULL,?);");
                p.setString(1,name);
                p.setString(2,age);
                p.setString(3,id);
                p.executeUpdate();

                connector.getConnector().commit();
                System.out.println("Person registered successfully!!");
            }
        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     *
     * @param name
     * @param id
     * @throws SQLException
     */
    public void deletePerson(String name, String id) throws SQLException, UncompletedRequest {
        try{
            connector.getConnector().setAutoCommit(false);

            PreparedStatement deleteStmt0 = connector.getConnector().prepareStatement("DELETE FROM frequents as f WHERE f.nameid=?;");
            deleteStmt0.setString(1, name);
            deleteStmt0.executeUpdate();

            PreparedStatement deleteStmt1 = connector.getConnector().prepareStatement("DELETE FROM eats as e WHERE e.nameid=?;");
            deleteStmt1.setString(1, name);
            deleteStmt1.executeUpdate();

            PreparedStatement deleteStmt3 = connector.getConnector().prepareStatement("DELETE FROM menu_order as m WHERE m.customer_id=?;");
            deleteStmt3.setString(1, id);
            deleteStmt3.executeUpdate();

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
     *
     * @param menu_mtype
     * @param menu_id
     * @param customer_id
     * @throws SQLException
     */
    public void addMenuOrder(String menu_mtype, String menu_id, String customer_id) throws SQLException, UncompletedRequest {
        try {

            connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO menu_order VALUES (default,?,?,?);");
            p.setString(1,menu_mtype);
            p.setString(2,menu_id);
            p.setString(3,customer_id);
            p.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Menu order registered!!");

        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     *
     * @param menu_mtype
     * @param menu_id
     * @throws SQLException
     */
    public void insertMenu(String menu_mtype, String menu_id) throws SQLException, UncompletedRequest {
        try {

            connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO menu VALUES (?,?,default);");
            p.setString(1, menu_mtype);
            p.setString(2, menu_id);
            p.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Menu registered!!");

        } catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
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
            try {

                connector.getConnector().setAutoCommit(false);
                PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT * FROM menu WHERE mtype=? AND mid=?;");
                stmt.setString(1,menu_mtype);
                stmt.setString(2,menu_id);
                rs = stmt.executeQuery();

                connector.getConnector().commit();

            } catch (SQLException e) {
                System.out.println("Database rolling back");
                connector.getConnector().rollback();
                e.printStackTrace();
            }
            return rs;
    }

    /**
     * This method provides all menu-orders
     *
     * @return all menu-orders
     * @throws SQLException if rollback fails
     */
    public ResultSet getAllMenuOrders() throws SQLException {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT * FROM menu_order;");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
        }
        return rs;
    }

    /* DISH RELATED */

    /**
     * This method adds  a dish to the database
     *
     * @param food String that represents a certain dish
     * @throws SQLException if rollback fails
     */
    public void insertDish(String food) throws SQLException, UncompletedRequest {
        try {

            connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO dishes VALUES (?, default, default, default);");
            p.setString(1,food);
            p.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Person registered successfully!!");
        }  catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     * This method provides whether a certain dish exists.
     *
     * @param food String that represents a certain dish
     * @return whether a certain dish exists.
     * @throws SQLException if rollback fails
     */
    public boolean foodExists(String food) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM dishes as d WHERE d.dish=?;");
            p.setString(1,food);
            rs = p.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error executing query");
        }
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
            connector.getConnector().setAutoCommit(false);
            PreparedStatement updateStmt = connector.getConnector().prepareStatement("UPDATE serves SET price=(0.5*price) WHERE dish=?;");

            updateStmt.setString(1,dish);
            int changed = updateStmt.executeUpdate();

            connector.getConnector().commit();

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
     *
     * @return
     * @throws SQLException
     */
    public ResultSet getAllDishes() throws SQLException {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT s.dish, s.restaurname, s.price FROM serves as s;");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
        }
        return rs;
    }


/* RESTAURANT RELATED */

    /**
     *
     * @param restaurant
     * @return
     * @throws SQLException
     */
    public boolean restaurantExists(String restaurant) throws SQLException {
        try {
            PreparedStatement p = connector.getConnector().prepareStatement("SELECT * FROM restaurant as r WHERE r.restaurname=?;");
            p.setString(1,restaurant);
            rs = p.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error executing query");
        }
        return rs.next();
    }


    /**
     *
     * @param restaurant
     * @throws SQLException
     */
    public void insertRestaurant(String restaurant) throws SQLException, UncompletedRequest {
        try {

            connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO restaurant VALUES (?,default, default, default, default);");
            p.setString(1,restaurant);
            p.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Restaurant registered successfully!!");

        }  catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }


    /**
     * This method gets the restaurants that provide food liked by all managers
     * @return the restaurants that provide food liked by all managers
     * @throws SQLException if rollback fails
     * @throws UncompletedRequest if the query could not be executed
     */
    public ResultSet getRestaurantLikedManagers() throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("select distinct r.restaurname as restaurant, s.dish as dish from serves as s inner join restaurant as r on r.restaurname=s.restaurname inner join (select distinct e.dish from eats as e  where not exists (select * from person as p inner join department as d on p.id=d.Mgr_ssn where not exists (select * from eats as e2 where e2.nameid=p.nameid and e2.dish=e.dish )) and exists (select * from person as p inner join department as d on p.id=d.Mgr_ssn)) as t2 on t2.dish=s.dish where  (r.capacity>=(select count(distinct d2.Mgr_ssn) from department as d2));");
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
            throw new UncompletedRequest();
        }
        return rs;
    }


    /* EMPLOYEE-RELATED */


    /**
     * This method provides the employees that frequent a single restaurant in the given city
     * @param city Strig - the given city
     * @return the employees that frequent a single restaurant in the given city
     * @throws SQLException if rollback fails
     * @throws UncompletedRequest if the query could not be executed
     */
    public ResultSet getEmployee1RestCity(String city) throws SQLException, UncompletedRequest {
        try {
            connector.getConnector().setAutoCommit(false);
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT p.id, e.fname , e.lname  " +
                    "FROM frequents as f inner join (person as p inner join employee as e on e.ssn=p.id) ON f.nameid=p.nameid " +
                    "inner join restaurant as r on r.restaurname=f.restaurname " +
                    " WHERE  r.city=? " +
                    " GROUP BY f.nameid " +
                    " HAVING count(distinct f.restaurname)=1; ");
            stmt.setString(1,city);
            rs = stmt.executeQuery();
            System.out.println("Query executed correctly!!");

        } catch (SQLException e) {
            connector.getConnector().rollback();
            System.out.println("Couldn't execute query.");
            throw new UncompletedRequest();
        }
        return rs;
    }



    /* PREFERENCES RELATED */

    /**
     *
     * @param name
     * @param food
     * @throws SQLException
     */
    public void insertEats(String name, String food) throws SQLException, UncompletedRequest {
        try {

            connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO eats VALUES (?,?);");
            p.setString(1,name);
            p.setString(2,food);
            p.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Person registered successfully!!");
        }  catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }

    /**
     *
     * @param name
     * @param restaurant
     * @throws SQLException
     */
    public void addFrequents(String name, String restaurant) throws SQLException, UncompletedRequest {
        try {

            connector.getConnector().setAutoCommit(false);
            PreparedStatement p = connector.getConnector().prepareStatement("INSERT INTO frequents VALUES (?,?);");
            p.setString(1,name);
            p.setString(2,restaurant);
            p.executeUpdate();

            connector.getConnector().commit();
            System.out.println("Transaction executed successfully!!");
        }  catch (SQLException e) {
            System.out.println("Database rolling back");
            connector.getConnector().rollback();
            throw new UncompletedRequest();
        }
    }


    /**
     * Method that says whether a guide is in a given trip or not
     * @return boolean - Whether the guide is in the trip or not
     */
    public boolean existGuideInTrip(String GuideId, String TripTo, String DepartureDate) {
        try {
            PreparedStatement stmt = connector.getConnector().prepareStatement("SELECT * FROM trip WHERE GuideId=? AND TripTo=? AND DepartureDate=?;");
            stmt.setString(1,GuideId);
            stmt.setString(2,TripTo);
            stmt.setString(3,DepartureDate);
            ResultSet rs = stmt.executeQuery();

            if(!rs.next()){
                return false;
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
