package controllers;

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

public class updateGuideController implements Controller {

    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

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


    @Override
    public void setMainApp(MainGUI main) {
        mainWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {
        fillTable();
        resetFields();
    }

    @FXML
    void onClickBack() {
        mainWin.showTransaction();
    }

    @FXML
    void onClickExecute() {
        errorLbl.setText("");
        correctLbl.setText("");
        if ((tgnew.getText().isEmpty() || tgprev.getText().isEmpty() || date1.getText().isEmpty() || date2.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else {
            businessLogic.updateTourguide(tgprev.getText(), tgnew.getText(), date1.getText(), date2.getText());
            correctLbl.setText("Transaction executed!!");
            fillTable();
        }
    }


    private void resetFields() {
        errorLbl.setText("");
        correctLbl.setText("");
        tgnew.setText("");
        tgprev.setText("");
        date1.setText("");
        date2.setText("");
    }

    private void fillTable() {
        col.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });
        // clear table
        tourguideTable.getItems().clear();


        // fill table with current guides in the database
        Vector<String> rs = businessLogic.getAllTourguideTrips();
        if (rs != null) {
            tourguideTable.getItems().addAll(rs);
        } else {
            tourguideTable.getItems().add("No guide in the database");
        }
    }
}