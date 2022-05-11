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

public class addPersonController implements Controller {

    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblGuide;
    @FXML
    private TableColumn<String, String> guideColumn;


    @FXML
    private TextField name;
    @FXML
    private TextField age;
    @FXML
    private TextField gender;
    @FXML
    private TextField id;


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
    void onClickMakeOrder(){
        mainWin.showMakeOrder();
    }

    @FXML
    void onClickAddPerson(){
        errorLbl.setText("");
        correctLbl.setText("");
        if ((name.getText().isEmpty() || age.getText().isEmpty() || gender.getText().isEmpty() || id.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else {
         //   businessLogic.insertPersonUI( name.getText(), age.getText(), gender.getText(),  id.getText());
            correctLbl.setText("Transaction executed!!");
        }
    }


}
