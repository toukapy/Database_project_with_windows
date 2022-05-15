package controllers;

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

public class deleteCustomerController implements Controller {

    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

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



    @Override
    public void setMainApp(MainGUI main) {
        mainWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {
        resetFields();
        fillTable();

    }

    private void resetFields(){
        correctLbl.setText("");
        errorLbl.setText("");
        name.setText("");
        phoneNum.setText("");
        TripTo.setText("");
        DepartureDate.setText("");
    }


    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }

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

    private boolean check(String text) {
        for(int i = 0; i<text.length();i++){
            if(text.charAt(i) >= '0' && text.charAt(i)<='9'){
                return false;
            }
        }

        return true;
    }


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
