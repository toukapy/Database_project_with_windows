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
/**
 * This class aims to deal with the window that handles getting the quantity of customers a guide is responsible for
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class QuantityCustomerGuideController implements Controller {

    private MainGUI customerGuideWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblGuide;
    @FXML
    private TableColumn<String, String> guideColumn;


    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        customerGuideWin = main;
    }

    /**
     * Method to initialize the information in the UI
     * @throws SQLException
     */
    @Override
    public void initializeInformation() throws SQLException {

        guideColumn.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });

        Vector<String> rs = businessLogic.retrieveNumCustomerGuideResponsible();

        tblGuide.getItems().clear();

        if(!rs.isEmpty()){
            tblGuide.getItems().addAll(rs);
        }

    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        customerGuideWin.showQuery();
    }
}
