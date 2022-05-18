package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.util.Vector;

/**
 * This class aims to deal with the window that handles getting the restaurants that offer food liked by all managers
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class RestaurantFoodManagerController implements Controller {

    private MainGUI restaurantFoodWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblRestaurants;
    @FXML
    private TableColumn<String,String> columnRestaur;
    @FXML
    private Label errorLbl;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        restaurantFoodWin = main;
    }

    /**
     * Method to initialize the information in the UI
     * @throws SQLException
     */
    @Override
    public void initializeInformation() {
        try {
            columnRestaur.setCellValueFactory(data -> {
                return new SimpleStringProperty(data.getValue());
            });

            Vector<String> rs = businessLogic.getRestaurantLikedManagers();

            tblRestaurants.getItems().clear();

            if (!rs.isEmpty()) {
                tblRestaurants.getItems().addAll(rs);
            } else {
                tblRestaurants.getItems().add("There is not such restaurant");
            }
        } catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }

    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        restaurantFoodWin.showQuery();
    }
}
