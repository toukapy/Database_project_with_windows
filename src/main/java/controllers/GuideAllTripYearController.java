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
import java.util.Vector;
/**
 * This class aims to deal with the window that handles getting the guides that have attended all trips in a given year
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class GuideAllTripYearController implements Controller {

    private MainGUI guideAllTripWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TextField dateField;
    @FXML
    private TableView<String> tblTrip;
    @FXML
    private TableColumn<String,String> tripColumn;
    @FXML
    private Label errorLbl;
    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        guideAllTripWin = main;
    }

    /**
     * Method to initialize the information in the UI
     */
    @Override
    public void initializeInformation() {
        tblTrip.getItems().clear();
        errorLbl.setText("");
        dateField.setText("");
    }

    /**
     * Method that retrieves guides who have been in all trips on a given year
     */
    @FXML
    void onClickEnter(){
        errorLbl.setText("");
        tblTrip.getItems().clear();
        tripColumn.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });

        if(dateField.getText().isEmpty())
            errorLbl.setText("Please fill all fields.");

        else if(!checkYear(dateField.getText()))
            errorLbl.setText("The year has to be yyyy");

        else{
            try {
                Vector<String> rs = businessLogic.getTourguidesAllTripsYear(dateField.getText());

                tblTrip.getItems().clear();

                if (!rs.isEmpty()) {
                    tblTrip.getItems().addAll(rs);
                } else {
                    tblTrip.getItems().add("There is no such guide");
                }
            } catch (SQLException e){
                errorLbl.setText("An error with the database occurred. Please, try again later.");
            }
        }
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        guideAllTripWin.showQuery();
    }

    /**
     * Check whether the text is a valid year
     * @param year String - The year
     * @return boolean - whether it is valid or not
     */
    public boolean checkYear(String year){

        try {
            /* Given year should have 4 digits */
            if(year.length()==4) {
                Integer.parseInt(year);
                return true;
            }else return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
