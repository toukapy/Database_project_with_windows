package controllers;

import businessLogic.BlFacadeImplementation;
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

public class CustomerAllOptExcTripController implements Controller {

    private MainGUI allOptExcWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();
    private ObservableList data;

    @FXML
    private TableView<Object> customerTable;

    @Override
    public void setMainApp(MainGUI main) {
        allOptExcWin = main;
    }

    @FXML
    void initialize() throws SQLException {

        ArrayList<String> rs = businessLogic.retrieveCustomerEveryTripExc();

        if(!rs.isEmpty()){
            System.out.println("There is no such customer in the database!!");
            customerTable.getItems().add("There is no such customer!!");
            return;
        }

        customerTable.getItems().addAll(rs);

    }




}
