package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import exceptions.UncompletedRequest;
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
/**
 * This class aims to deal with the window that handles making an order
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class makeOrderController implements Controller {

    private MainGUI mainWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> orderTable;
    @FXML
    private TableColumn<String, String> col;



    @FXML
    private TextField menu_mtype;
    @FXML
    private TextField menu_id;
    @FXML
    private TextField customer_id;
    @FXML
    private TextField answerField;


    @FXML
    private Label errorLbl;
    @FXML
    private Label correctLbl;

    private String choice="";

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
     */
    @Override
    public void initializeInformation() {
        fillTable();
        resetFields();

    }

    /**
     * Method to reset the different fields in the UI
     */
    private void resetFields(){
        errorLbl.setText("");
        correctLbl.setText("");
        menu_mtype.setText("");
        menu_id.setText("");
        customer_id.setText("");
        answerField.setText("");
        choice="";
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }

    /**
     * Method that handles making an order
     */
    @FXML
    void onClickMakeOrder(){
        errorLbl.setText("");
        correctLbl.setText("");
        //warning cases
        if(choice.equals(""))
            errorLbl.setText("Please, enter your choice");
        else if ((menu_mtype.getText().isEmpty() || menu_id.getText().isEmpty() ||  customer_id.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        //execute transaction
        else {
            try {
                businessLogic.insertMenuOrder(choice, menu_mtype.getText(), menu_id.getText(), customer_id.getText());
                fillTable();
                correctLbl.setText("Transaction executed!!");
            } catch (SQLException e){
                errorLbl.setText("An error with the database occurred. Please, try again later.");
            } catch (UncompletedRequest e) {
                errorLbl.setText("Transaction could not be done. Please change the fields' information.");
            }
        }
    }


    /**
     * Method to get the information about whether the user wants to create the objects if they do not exist
     */
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

    /**
     * Method to fill the table with the initial information
     */
    @FXML
    private void fillTable(){
        try {
            col.setCellValueFactory(data -> {
                return new SimpleStringProperty(data.getValue());
            });
            // clear table
            orderTable.getItems().clear();


            // fill table with current orders in the database
            Vector<String> rs = businessLogic.getAllMenuOrders();
            if (!rs.isEmpty()) {
                orderTable.getItems().addAll(rs);
            } else {
                orderTable.getItems().add("No orders in the database");
            }
        }catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }
    }


}
