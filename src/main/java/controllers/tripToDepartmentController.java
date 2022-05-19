package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import exceptions.NoHotel;
import exceptions.ObjectNotCreated;
import exceptions.UncompletedRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;

/**
 * This class aims to deal with the window that handles adding a customer to the trip database
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class tripToDepartmentController implements Controller {

    private MainGUI mainWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> customerTable;
    @FXML
    private TableColumn<String, String> col;
    @FXML
    private ComboBox<String> combBox;


    @FXML
    private TextField DepartureDate;
    @FXML
    private Label errorLbl;
    @FXML
    private Label correctLbl;
    private String choice = "";


    /**
     * Method that sets this window as the main window
     *
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
        resetFields();
        fillTable();
    }

    /**
     * Method to reset the different fields in the UI
     */
    private void resetFields() {
        correctLbl.setText("");
        errorLbl.setText("");
        DepartureDate.setText("");
        choice = "";
        Vector<String> d = businessLogic.getDepartmentsAndLocations();
        combBox.getItems().clear();
        combBox.getItems().addAll(d);

    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack() {
        mainWin.showTransaction();
    }

    /**
     * Method that adds a customer in the selected trip and hotel
     */
    @FXML
    void onClickExecute() {
        errorLbl.setText("");
        correctLbl.setText("");
        customerTable.getItems().clear();

        try {
            if (DepartureDate.getText().isEmpty() || combBox.getValue() == null)
                errorLbl.setText("Please, fill all fields / select a department");
            else {
                // Get the dept number and location from comboBox
                String[] parts = combBox.getValue().split(",");
                String[] num = parts[0].split(" ");
                String[] loc = parts[1].split(" ");
                businessLogic.bookTripToDepartment(num[1], loc[1], DepartureDate.getText());
                correctLbl.setText("Transaction executed!!");

                Vector<String> answer = businessLogic.getTrips(loc[1], DepartureDate.getText());

                if (!answer.isEmpty()) {
                    for (String s : answer) {
                        System.out.println(s + "\n");
                        customerTable.getItems().add(s);
                    }

                } else {
                    customerTable.getItems().add("No trip has been created");
                }
            }


        }catch(SQLException e) {
            errorLbl.setText("An error with the database occurred. Please, try again later or change the fields.");
        } catch(UncompletedRequest e2){
            errorLbl.setText("Transaction could not be done. Please change the fields' information.");
        } catch(ParseException e3){
            errorLbl.setText("Please, enter a valid date: yyyy-mm-dd");
        } catch (NoHotel e4){
            errorLbl.setText("There is no hotel in that city.");
        }

    }




    /**
     * Method to fill the table with the initial information
     */
    private void fillTable() {
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        // clear table
        customerTable.getItems().clear();

    }
}
