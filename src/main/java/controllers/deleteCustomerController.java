package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;
/**
 * This class aims to deal with the window that handles deleting a customer from a trip
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class deleteCustomerController implements Controller {

    private MainGUI mainWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> customerTable;
    @FXML
    private TableColumn<String, String> col;

    @FXML
    private TextField name;
    @FXML
    private TextField phoneNum;
    @FXML
    private TextField TripTo;
    @FXML
    private TextField DepartureDate;

    @FXML
    private Label errorLbl;
    @FXML
    private Label correctLbl;

    private String trip = "";
    private String departure = "";


    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        mainWin = main;
    }

    /**
     * Method to initialize the information in the UI
     * @throws SQLException
     */
    @Override
    public void initializeInformation() throws SQLException {
        resetFields();
        fillTable();

    }

    /**
     * Method to reset the different fields in the UI
     */
    private void resetFields(){
        correctLbl.setText("");
        errorLbl.setText("");
        name.setText("");
        phoneNum.setText("");
        TripTo.setText("");
        DepartureDate.setText("");
    }


    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }

    /**
     * Method that deletes the person matching the given information
     */
    @FXML
    void onClickExecute() {
        errorLbl.setText("");
        correctLbl.setText("");
        if ((name.getText().isEmpty() || phoneNum.getText().isEmpty() || TripTo.getText().isEmpty() || DepartureDate.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else if(trip.equals("") || departure.equals("")){
            errorLbl.setText("Please, first enter the destination and date");
        }
        else {
            try {
                businessLogic.deleteCustomerFromTrip(name.getText(), phoneNum.getText(), trip, departure);
                correctLbl.setText("Transaction executed!!");
                Vector<String> customers = businessLogic.getCustomerTrip(trip,departure);
                customerTable.getItems().clear();
                customerTable.getItems().addAll(customers);
            }catch (SQLException e){
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that takes the information of the trip given
     * @throws SQLException
     */
    @FXML
    void onClickEnterTrip() throws SQLException {
        if(TripTo.getText().isEmpty() || DepartureDate.getText().isEmpty()){
            errorLbl.setText("PLEASE, fill all fields");
        }else if(!check(TripTo.getText())){
            errorLbl.setText(("PLEASE, enter a valid destination"));
        }else{
            trip = TripTo.getText();
            departure = DepartureDate.getText();
            customerTable.getItems().clear();
            try {
                Vector<String> customers = businessLogic.getCustomerTrip(trip,departure);
                if(customers.isEmpty()){
                    customerTable.getItems().clear();
                    customerTable.getItems().add("There is no customer in this trip");
                    name.setDisable(true);
                    phoneNum.setDisable(true);
                }else{
                    customerTable.getItems().clear();
                    customerTable.getItems().addAll(customers);
                    name.setDisable(false);
                    phoneNum.setDisable(false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that checks whether the trip given is appropriate
     * @param text - The trip
     * @return boolean- whether it is appropriate or not
     */
    private boolean check(String text) {
        for(int i = 0; i<text.length();i++){
            if(text.charAt(i) >= '0' && text.charAt(i)<='9'){
                return false;
            }
        }

        return true;
    }


    /**
     * Method to fill the table with the initial information
     */
    private void fillTable() {
        col.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });
        // clear table
        customerTable.getItems().clear();


        // fill table with current customers in the database
        Vector<String> rs = businessLogic.getAllCustomersJustTrip();
        if (rs != null) {
            customerTable.getItems().addAll(rs);
        } else {
            customerTable.getItems().add("No customer in the database");
        }
    }
}
