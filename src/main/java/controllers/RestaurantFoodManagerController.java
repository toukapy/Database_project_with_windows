package controllers;

import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;

public class RestaurantFoodManagerController implements Controller {

    private MainGUI restaurantFoodWin;

    @Override
    public void setMainApp(MainGUI main) {
        restaurantFoodWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {

    }
}
