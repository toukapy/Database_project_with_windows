package controllers;

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

public class maximumGainsTripController<TableColum> implements Controller {

    private MainGUI maximumGainsWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();
    ObservableList<String> data;

    @FXML
    private TableView<ResultSet> customerTable;
    @FXML
    private TableColumn<ResultSet,String> destColumn;
    @FXML
    private TableColumn<ResultSet,String> dateColumn;

    @Override
    public void setMainApp(MainGUI main) {
        maximumGainsWin = main;
    }


    public void initializeInformation() throws SQLException {
        ResultSet rs = businessLogic.getMaximumGainedTrip();
        destColumn.setCellValueFactory(data -> {
            try {
                return new SimpleStringProperty(data.getValue().getString("TripTo"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

        dateColumn.setCellValueFactory(data ->{
            try {
                return new SimpleStringProperty(data.getValue().getString("DepartureDate"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });


        customerTable.getItems().clear();

        if(rs != null){
            System.out.println("Trip exists");
            customerTable.getItems().add(rs);
        }

        businessLogic.close();


    }

}
