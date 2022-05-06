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

public class makeOrderController implements Controller {

    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblGuide;
    @FXML
    private TableColumn<String, String> guideColumn;


    @FXML
    private TextField numord;
    @FXML
    private TextField menu_mtype;
    @FXML
    private TextField menu_id;
    @FXML
    private TextField customer_id;


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
    void onClickExecute(){
        errorLbl.setText("");
        correctLbl.setText("");
        if ((numord.getText().isEmpty() || menu_mtype.getText().isEmpty() || menu_id.getText().isEmpty() || customer_id.getText().isEmpty()))
            errorLbl.setText("Please, fill all fields");
        else {
            businessLogic.insertMenuOrder(numord.getText(), menu_mtype.getText(), menu_id.getText(), customer_id.getText());
            correctLbl.setText("Transaction executed!!");
        }
    }
}
