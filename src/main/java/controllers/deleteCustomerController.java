package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;

public class deleteCustomerController implements Controller {

    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblGuide;
    @FXML
    private TableColumn<String, String> guideColumn;

    @FXML
    private TextField name;
    @FXML
    private TextField phoneNum;
    @FXML
    private TextField TripTo;
    @FXML
    private TextField DepartureDate;

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
/*
        guideColumn.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });
*/
    }

    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }

    @FXML
    void onClickExecute() {
        errorLbl.setText("");
        correctLbl.setText("");
        if ((name.getText().isEmpty() || phoneNum.getText().isEmpty() || TripTo.getText().isEmpty() || DepartureDate.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else {
            try {
                businessLogic.deleteCustomerFromTrip(name.getText(), phoneNum.getText(), TripTo.getText(), DepartureDate.getText());
                correctLbl.setText("Transaction executed!!");
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
