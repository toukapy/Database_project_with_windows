package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class GuideAllTripYearController implements Controller {

    private MainGUI guideAllTripWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TextField dateField;
    @FXML
    private TableView<String> tblTrip;
    @FXML
    private TableColumn<String,String> tripColumn;

    @Override
    public void setMainApp(MainGUI main) {
        guideAllTripWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {
        tblTrip.getItems().clear();
        dateField.setText("");
    }

    @FXML
    void onClickEnter(){
        tripColumn.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });

        if(!dateField.getText().isEmpty()){

            if(!checkDate(dateField.getText())){
                System.out.println("The date has to be yyyy-MM-dd format");
            }else{
                Vector<String> rs = businessLogic.getTourguidesAllTripsYear(dateField.getText());

                tblTrip.getItems().clear();

                if(!rs.isEmpty()){
                    tblTrip.getItems().addAll(rs);
                }else{
                    tblTrip.getItems().add("There is no such guide");
                }
            }

        }


    }

    @FXML
    void onClickBack(){
        guideAllTripWin.showQuery();
    }

    public boolean checkDate(String date){

        try {
            /* Given year has 4 digits */
            if(date.length()==4)
                Integer.parseInt(date);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
