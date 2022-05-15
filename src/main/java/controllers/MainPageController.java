package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;

public class MainPageController implements Controller {

    @FXML
    private Button queryBtn;
    @FXML
    private Button transactionBtn;
    @FXML
    private Button backBtn;

    private MainGUI mainPageWin;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        mainPageWin = main;
    }

    /**
     * Method to initialize the information in the UI
     * @throws SQLException
     */
    @Override
    public void initializeInformation() throws SQLException {

    }

    /**
     * MEthod to show the queries window
     */
    @FXML
    void onClickQueries(){
        mainPageWin.showQuery();
    }

    /**
     * Method to show the transactions window
     */
    @FXML
    void onClickTransactions(){
        mainPageWin.showTransaction();
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        mainPageWin.setVisibility(false);
        System.exit(0);
    }

}
