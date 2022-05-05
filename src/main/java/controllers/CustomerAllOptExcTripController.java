package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import uis.Controller;
import uis.MainGUI;

import javax.swing.text.BadLocationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class CustomerAllOptExcTripController implements Controller {

    private MainGUI allOptExcWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();
    private ObservableList data;

    @FXML
    private TableView<String> customerTable;
    @FXML
    private TableColumn<String, String> idColumn;

    @Override
    public void setMainApp(MainGUI main) {
        allOptExcWin = main;
    }


    @Override
    public void initializeInformation() throws SQLException {

        idColumn.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });

        Vector<String> rs = businessLogic.retrieveCustomerEveryTripExc();

        customerTable.getItems().clear();

        if(rs.isEmpty()){
            System.out.println("There is no such customer in the database!!");
            customerTable.getItems().add("There is no such customer!!");
        }else{
            customerTable.getItems().addAll(rs);
        }


    }

    @FXML
    void onClickBack(){
        allOptExcWin.showQuery();
    }




}
