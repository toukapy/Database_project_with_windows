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
 * This class aims to deal with the window that handles getting guides who speak all languages registered in the travel database
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class GuideAllLanguagesController implements Controller {

    private MainGUI guideAllLanWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblGuide;
    @FXML
    private TableColumn<String,String> guideColumn;
    @FXML
    private Label errorLbl;

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        guideAllLanWin = main;
    }

    /**
     * Method to initialize the information in the UI
     */
    @Override
    public void initializeInformation()  {
        try {
            guideColumn.setCellValueFactory(data -> {
                return new SimpleStringProperty(data.getValue());
            });

            tblGuide.getItems().clear();

            Vector<String> rs = businessLogic.getTourguidesAllLanguages();

            if (!rs.isEmpty()) {
                tblGuide.getItems().addAll(rs);
            } else {
                tblGuide.getItems().add("There is no such tour-guide");
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
        guideAllLanWin.showQuery();
    }
}
