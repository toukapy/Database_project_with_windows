package controllers;

import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;

public class TransactionPageController implements Controller {

    private MainGUI tranPageWin;

    @Override
    public void setMainApp(MainGUI main) {
        tranPageWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {

    }

}
