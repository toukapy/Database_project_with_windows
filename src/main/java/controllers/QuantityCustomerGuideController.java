package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import uis.Controller;
import uis.MainGUI;

import javax.swing.text.BadLocationException;
import java.sql.SQLException;
import java.util.Vector;

public class QuantityCustomerGuideController implements Controller {

    private MainGUI customerGuideWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblGuide;
    @FXML
    private TableColumn<String, String> guideColumn;


    @Override
    public void setMainApp(MainGUI main) {
        customerGuideWin = main;
    }

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

    @FXML
    void onClickBack(){
        customerGuideWin.showQuery();
    }
}
