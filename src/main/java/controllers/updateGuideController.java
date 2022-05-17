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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
/**
 * This class aims to deal with the window that handles updating a guide on the trips of a given interval of time
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class updateGuideController implements Controller {

    private MainGUI mainWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tourguideTable;
    @FXML
    private TableColumn<String, String> col;

    @FXML
    private TextField tgprev;
    @FXML
    private TextField tgnew;
    @FXML
    private TextField date1;
    @FXML
    private TextField date2;

    @FXML
    private Label errorLbl;
    @FXML
    private Label correctLbl;

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
    public void initializeInformation()  {
        fillTable();
        resetFields();
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack() {
        mainWin.showTransaction();
    }

    /**
     * Method that updates the guide of the trips in a given interval of time
     */
    @FXML
    void onClickExecute() {
        errorLbl.setText("");
        correctLbl.setText("");
        if ((tgnew.getText().isEmpty() || tgprev.getText().isEmpty() || date1.getText().isEmpty() || date2.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else if(!validDate(date1.getText()) && !validDate(date2.getText()))
            errorLbl.setText("Please, enter a valid date: yyyy-mm-dd");
        else if(tgprev.getText().equals(tgnew.getText()))
            errorLbl.setText("Please, enter a different tour-guide id");
        else {
            try {
                businessLogic.updateTourguide(tgprev.getText(), tgnew.getText(), date1.getText(), date2.getText());
                correctLbl.setText("Transaction executed!!");
                fillTable();
            } catch (SQLException e){
                errorLbl.setText("An error with the database occurred. Please, try again later.");
            } catch (UncompletedRequest e) {
                errorLbl.setText("Transaction could not be done. Please change the fields' information.");
            }
        }
    }

    /**
     * This method provides whether the date is valid
     * @param date provided date
     * @return whether the date is valid
     */
    private boolean validDate(String date){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Method to reset the different fields in the UI
     */
    private void resetFields() {
        errorLbl.setText("");
        correctLbl.setText("");
        tgnew.setText("");
        tgprev.setText("");
        date1.setText("");
        date2.setText("");
    }

    /**
     * Method to fill the table with the initial information
     */
    private void fillTable() {
        col.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });
        // clear table
        tourguideTable.getItems().clear();

        try {
            // fill table with current guides in the database
            Vector<String> rs = businessLogic.getAllTourguideTrips();
            if (!rs.isEmpty()) {
                tourguideTable.getItems().addAll(rs);
            } else {
                tourguideTable.getItems().add("No guide in the database");
            }
        } catch (SQLException e) {
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }
    }
}