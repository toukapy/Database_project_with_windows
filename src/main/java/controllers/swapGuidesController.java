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

public class swapGuidesController implements Controller {

    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblGuide;
    @FXML
    private TableColumn<String, String> guideColumn;

    @FXML
    private TextField name1;
    @FXML
    private TextField name2;
    @FXML
    private TextField phone1;
    @FXML
    private TextField phone2;
    @FXML
    private TextField trip1;
    @FXML
    private TextField trip2;
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
        if ((name1.getText().isEmpty() || phone1.getText().isEmpty() || date1.getText().isEmpty() || name2.getText().isEmpty() || phone2.getText().isEmpty() || trip2.getText().isEmpty() || date2.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else {
            businessLogic.changeGuidesBetweenTrips(name1.getText(), phone1.getText(), trip1.getText(), date1.getText(), name2.getText(), phone2.getText(), trip2.getText(), date2.getText());
            correctLbl.setText("Transaction executed!!");
        }
    }
}
