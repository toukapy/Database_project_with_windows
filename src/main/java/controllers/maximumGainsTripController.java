package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uis.Controller;
import uis.MainGUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
/**
 * This class aims to deal with the window that handles getting the trips with maximum gains
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class maximumGainsTripController implements Controller {

    private MainGUI maximumGainsWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> customerTable;
    @FXML
    private TableColumn<String,String> destColumn;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        maximumGainsWin = main;
    }

    /**
     * Method to initialize the information in the UI
     * @throws SQLException
     */
    @Override
    public void initializeInformation() throws SQLException {

        destColumn.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });

        customerTable.getItems().clear();
        Vector<String> rs = businessLogic.getMaximumGainedTrip();
        if(!rs.isEmpty()){
            System.out.println("Trip exists");
            customerTable.getItems().addAll(rs);
        }

    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        maximumGainsWin.showQuery();
    }

}
