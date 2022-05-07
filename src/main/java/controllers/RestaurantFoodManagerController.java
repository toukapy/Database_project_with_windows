package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.util.Vector;

public class RestaurantFoodManagerController implements Controller {

    private MainGUI restaurantFoodWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblRestaurants;
    @FXML
    private TableColumn<String,String> columnRestaur;

    @Override
    public void setMainApp(MainGUI main) {
        restaurantFoodWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {

        columnRestaur.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });

        Vector<String> rs = businessLogic.getRestaurantLikedManagers();

        tblRestaurants.getItems().clear();

        if(!rs.isEmpty()){
            tblRestaurants.getItems().addAll(rs);
        }else{
            tblRestaurants.getItems().add("There is not such restaurant");
        }


    }

    @FXML
    void onClickBack(){
        restaurantFoodWin.showQuery();
    }
}