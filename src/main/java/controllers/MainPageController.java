package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import uis.Controller;
import uis.MainGUI;

public class MainPageController implements Controller {

    @FXML
    private Button queryBtn;
    @FXML
    private Button transactionBtn;
    @FXML
    private Button backBtn;

    private MainGUI mainPageWin;


    @Override
    public void setMainApp(MainGUI main) {
        mainPageWin = main;
    }

    @FXML
    void onClickQueries(){
        mainPageWin.showQuery();
    }

    @FXML
    void onClickTransactions(){
        mainPageWin.showTransaction();
    }

    @FXML
    void onClickBack(){

    }

}
