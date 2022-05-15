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
import java.util.Vector;

public class addCustomerController implements Controller {

    public Label questionLbl;
    public TextField answerField;
    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> customerTable;
    @FXML
    private TableColumn<String,String> col;


    @FXML
    private TextField custname;
    @FXML
    private TextField custphone;
    @FXML
    private TextField hotelname;
    @FXML
    private TextField hotelcity;
    @FXML
    private TextField TripTo;
    @FXML
    private TextField DepartureDate;
    @FXML
    private Label errorLbl;
    @FXML
    private Label correctLbl;
    private String choice ="";


    @Override
    public void setMainApp(MainGUI main) {
        mainWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {
        resetFields();
        col.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });

    }

    private void resetFields(){
        correctLbl.setText("");
        errorLbl.setText("");
        custname.setText("");
        custphone.setText("");
        hotelname.setText("");
        hotelcity.setText("");
        TripTo.setText("");
        DepartureDate.setText("");
        answerField.setText("");
        choice="";
    }

    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }

    @FXML
    void onClickExecute() throws SQLException {
        errorLbl.setText("");
        correctLbl.setText("");
        if ((custname.getText().isEmpty() || custphone.getText().isEmpty() || hotelname.getText().isEmpty() || hotelcity.getText().isEmpty() || TripTo.getText().isEmpty() || DepartureDate.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else if(businessLogic.getCustomerTripHotel(custname.getText(), custphone.getText(), hotelname.getText(), hotelcity.getText(), TripTo.getText(), DepartureDate.getText())!= null){errorLbl.setText("The customer is already in the trip");}
        else if(choice.equals("")){errorLbl.setText("Answer the question first");}
        else {
            businessLogic.addCustomerToTrip(choice,custname.getText(), custphone.getText(), hotelname.getText(), hotelcity.getText(), TripTo.getText(), DepartureDate.getText());
            correctLbl.setText("Transaction executed!!");
            Vector<String> answer = businessLogic.getCustomerTripHotel(custname.getText(), custphone.getText(), hotelname.getText(), hotelcity.getText(), TripTo.getText(), DepartureDate.getText());
            customerTable.getItems().clear();

            if(!answer.isEmpty()){
                for(String s: answer){
                    System.out.println(s+"\n");
                    customerTable.getItems().add(s);
                }

            }else{
                customerTable.getItems().add("There is no such customer");
            }

        }
    }

    @FXML
    void onClickAnswer(){
        errorLbl.setText("");
        if(answerField.getText().isEmpty()){
            errorLbl.setText("Enter a valid answer (y/n)");
        }else if(answerField.getText().equals("y") || answerField.getText().equals("n")){
            choice = answerField.getText();
        }else{
            errorLbl.setText("Enter a valid answer (y/n)");
        }
    }
}
