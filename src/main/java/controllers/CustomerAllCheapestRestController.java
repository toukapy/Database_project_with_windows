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

public class CustomerAllCheapestRestController implements Controller {

    private MainGUI allCheapestRestWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblCustomer;
    @FXML
    private TableColumn<String,String> customerColumn;

    @Override
    public void setMainApp(MainGUI main) {
        allCheapestRestWin = main;
    }

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
}
