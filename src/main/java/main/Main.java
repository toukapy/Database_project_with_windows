package main;

import businessLogic.BlFacadeImplementation;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

/**
 * This class aims to provide a menu for the user.
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class Main {

    private static BlFacadeImplementation bl = new BlFacadeImplementation();

    /**
     * Main method of the class
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            start();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method that asks the user to choose an option
     *
     * @throws SQLException
     */
    public static void start() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("-------- WELCOME TO OUR MANAGEMENT SYSTEM -------");
        System.out.println("-------- Choose an option between the following -------");
        System.out.println("1.- Queries\n" +
                "2.- Transactions\n" +
                "3.- Exit\n");

        int choice = sc.nextInt();

        while (choice != 3) {
            switch (choice) {
                case 1: /* queries */
                    System.out.println("* QUERIES *\n" +
                            "1.- Retrieve the trip with maximum gains\n" +
                            "2.- Retrieve customers that have been at least to all trips with optional excursion\n" +
                            "3.- Retrieve restaurants that serve food liked by all managers\n" +
                            "4.- Retrieve employees who only frequent a single restaurant in a given city\n" +
                            "5.- Retrieve customers who have gone at least on all cheapest trips attended by customers\n" +
                            "6.- Retrieve tour-guides who have gone on all trips of a given year\n" +
                            "7.- Retrieve tour-guides who speak all languages registered\n" +
                            "8.- Retrieve the number of customers associated to guides" +
                            "9.- Exit\n");
                    choice = sc.nextInt();
                    switch (choice) {
                        case 1: /* trip maximum gains */
                            bl.getMaximumGainedTrip();
                            System.out.println("Correctly executed!");
                            break;
                        case 2: /* customers gone to all trips with excursions */
                            bl.retrieveCustomerEveryTripExc();
                            System.out.println("Correctly executed!");
                            break;
                        case 3: /* restaurants offering food liked by all maagers */
                            bl.getRestaurantLikedManagers();
                            System.out.println("End of execution!");
                            break;
                        case 4: /* employees who only frequent 1 restaurant of a given city */
                            System.out.println("Insert the city");
                            sc.nextLine();
                            String city = sc.nextLine();
                            bl.getEmployee1RestCity(city);
                            System.out.println("End of execution!");
                            break;
                        case 5: /* customers gone to all cheapest trips */
                            bl.getCustomersAllCheapestTrips();
                            System.out.println("End of execution!");
                            break;
                        case 6: /* tour-guides gone to all trips of a given year */
                            System.out.println("Insert the year:");
                            sc.nextLine();
                            String year = sc.nextLine();
                            bl.getTourguidesAllTripsYear(year);
                            System.out.println("End of execution!");
                            break;
                        case 7: /* tour-guides who speak all languages */
                            bl.getTourguidesAllLanguages();
                            System.out.println("End of execution!");
                            break;
                        case 8: /* number of customers associated to the guides */
                            System.out.println("Number of customers associated to the guides: \n");
                            bl.retrieveNumCustomerGuideResponsible();
                            System.out.println("End of execution!!");
                        default:
                    } /* switch queries*/
                    break;

                case 2: /* transactions */
                    System.out.println("*  TRANSACTIONS *\n" +
                            "1.- Delete a customer from trip\n" +
                            "2.- Add a customer to a trip\n" +
                            "3.- Update the tour-guide of trips in a given interval of time\n" +
                            "4.- Sales! set a dish to half its price\n" +
                            "5.- Make an order\n" +
                            "6.- Swap guides between two trips\n" +
                            "7.- Exit\n");
                    choice = sc.nextInt();
                    switch (choice) {
                        case 1: /* delete customer from trip */
                            System.out.println("Give me name, phone number, trip and date");
                            sc.nextLine();
                            String name = sc.nextLine();

                            String phone = sc.nextLine();
                            String trip = sc.nextLine();
                            String date = sc.nextLine();

                            System.out.println(name);

                            try {
                                bl.deleteCustomerFromTrip(name, phone, trip, date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            System.out.println("Transaction done correctly!");
                            break;

                        case 2: /* add a customer to a trip */
                            System.out.println("Give me customer's name, phone, hotel name and city, and the name of the city to go and date");
                            sc.nextLine();
                            String nameCust = sc.nextLine();
                            String phoneCust = sc.nextLine();
                            String hotelname = sc.nextLine();
                            String hotelcity = sc.nextLine();
                            String tripTo = sc.nextLine();
                            String departureDate = sc.nextLine();

                            bl.addCustomerToTrip("y", nameCust, phoneCust, hotelname, hotelcity, tripTo, departureDate);
                            System.out.println("Transaction done correctly!!");
                            break;


                        case 3: /* Update the tour-guide of trips in a given interval of time */
                            System.out.println("Insert the tour-guide to remove, the tour-guide to be set and the date interval:");
                            sc.nextLine();
                            String tgprev = sc.nextLine();
                            String tgnew = sc.nextLine();
                            String date1 = sc.nextLine();
                            String date2 = sc.nextLine();
                            bl.updateTourguide(tgprev, tgnew, date1, date2);
                            System.out.println("End of execution!");
                            break;
                        case 4: /* set a dish to half its price */
                            System.out.println("Insert the dish that is on sale:");
                            sc.nextLine();
                            String dish = sc.nextLine();
                            bl.updateDishPrice(dish);
                            System.out.println("End of execution!");
                            break;
                        case 5: /* Make an order */
                            System.out.println("Insert the number of order, menu type, menu identifier and customer id:");
                            sc.nextLine();
                            String menu_mtype = sc.nextLine();
                            String menu_id = sc.nextLine();
                            String pname = sc.nextLine();
                            String customer_id = sc.nextLine();
                            System.out.println("Would you like to create objects if they don't exist? (y/n)");
                            sc.nextLine();
                            String  choiceorder = sc.nextLine();
                            bl.insertMenuOrder(choiceorder, menu_mtype, menu_id, pname, customer_id);
                            System.out.println("End of execution!");
                            break;
                        case 6: /* Swap guides between two trips */
                            System.out.println("Insert the data in this order (separated with enter)\n");
                            System.out.println("GuideName1, GuidePhone1, Destination1, Date1, GuideName2, GuidePHone2, Destination2, Date2");
                            sc.nextLine();
                            String name1 = sc.nextLine();
                            String phone1 = sc.nextLine();
                            String trip1 = sc.nextLine();
                            date1 = sc.nextLine();
                            String name2 = sc.nextLine();
                            String phone2 = sc.nextLine();
                            String trip2 = sc.nextLine();
                            date2 = sc.nextLine();

                            bl.changeGuidesBetweenTrips(name1, phone1, trip1, date1, name2, phone2, trip2, date2);
                            System.out.println("Successful transaction");

                            break;
                        default:
                    } /*switch transactions*/
                    break;
                    default:
                } /* outer switch */
                System.out.println("-------- Choose an option between the following -------");
                System.out.println("1.- Queries\n" +
                        "2.- Transactions\n" +
                        "3.- Exit\n");
                choice = sc.nextInt();
            } /* while */
        }
    }

