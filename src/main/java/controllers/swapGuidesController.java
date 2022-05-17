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
 * This class aims to deal with the window that handles swapping guides between two trips
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class swapGuidesController implements Controller {

    private MainGUI mainWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> guideTable;
    @FXML
    private TableColumn<String, String> col = new TableColumn<>();

    @FXML
    private TextField name1;
    @FXML
    private TextField name2;
    @FXML
    private TextField phone1;
    @FXML
    private TextField phone2;
    @FXML
    private TextField trip1;
    @FXML
    private TextField trip2;
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
    public void initializeInformation() {
        fillTable();
        resetFields();
    }

    /**
     * Method to reset the different fields in the UI
     */
    private void resetFields() {
        errorLbl.setText("");
        correctLbl.setText("");
        name1.setText("");
        name2.setText("");
        date1.setText("");
        date2.setText("");
        phone1.setText("");
        phone2.setText("");
        trip1.setText("");
        trip2.setText("");
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack() {
        mainWin.showTransaction();
    }

    /**
     * Method that swaps the given guides between the given trips
     */
    @FXML
    void onClickExecute() {
        errorLbl.setText("");
        correctLbl.setText("");
        if ((name1.getText().isEmpty() || phone1.getText().isEmpty() || date1.getText().isEmpty() || trip1.getText().isEmpty() || name2.getText().isEmpty() || phone2.getText().isEmpty() || trip2.getText().isEmpty() || date2.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else {
            try {
                businessLogic.changeGuidesBetweenTrips(name1.getText(), phone1.getText(), trip1.getText(), date1.getText(), name2.getText(), phone2.getText(), trip2.getText(), date2.getText());
                correctLbl.setText("Transaction executed!!");
                fillTable();
            } catch (SQLException e) {
                errorLbl.setText("An error with the database occurred. Please, try again later.");
            } catch (UncompletedRequest e) {
                errorLbl.setText("Transaction could not be done. Please change the fields' information.");
            }
        }
    }

    /**
     * Method to fill the table with the initial information
     */
    private void fillTable() {
        col.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });
        // clear table
        guideTable.getItems().clear();

        try {
            // fill table with current guides that have attended trips in the database
            Vector<String> rs = businessLogic.getAllTourguideTripsNotNull();
            if (!rs.isEmpty()) {
                guideTable.getItems().addAll(rs);
            } else {
                guideTable.getItems().add("No guide in the database");
            }
        } catch (SQLException e) {
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }
    }
}
