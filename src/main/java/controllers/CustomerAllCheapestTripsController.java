package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.util.Vector;

public class CustomerAllCheapestTripsController implements Controller {

    private MainGUI allCheapestRestWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblCustomer;
    @FXML
    private TableColumn<String,String> customerColumn;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        allCheapestRestWin = main;
    }

    /**
     * Method to initialize the information in the UI
     * @throws SQLException
     */
    @Override
    public void initializeInformation() throws SQLException {
        customerColumn.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });

        Vector<String> rs = businessLogic.getCustomersAllCheapestTrips();

        tblCustomer.getItems().clear();

        if(!rs.isEmpty()){
            tblCustomer.getItems().addAll(rs);
        }else{
            tblCustomer.getItems().add("There is no such customer");
        }
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        allCheapestRestWin.showQuery();
    }
}
