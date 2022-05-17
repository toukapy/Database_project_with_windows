package controllers;

import javafx.fxml.FXML;
import uis.Controller;
import uis.MainGUI;

/**
 * This class aims to deal with the main page
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class MainPageController implements Controller {


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
     */
    @Override
    public void initializeInformation() {

    }

    /**
     * Method to show the queries window
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
