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
    private TableView<String> customerTable;
    @FXML
    private TableColumn<String,String> destColumn;
    @Override
    public void setMainApp(MainGUI main) {
        maximumGainsWin = main;
    }


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

}
