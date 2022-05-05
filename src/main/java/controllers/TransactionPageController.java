package controllers;

import uis.Controller;
import uis.MainGUI;

public class TransactionPageController implements Controller {

    private MainGUI tranPageWin;

    @Override
    public void setMainApp(MainGUI main) {
        tranPageWin = main;
    }

}
