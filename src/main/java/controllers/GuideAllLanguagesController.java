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

public class GuideAllLanguagesController implements Controller {

    private MainGUI guideAllLanWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> tblGuide;
    @FXML
    private TableColumn<String,String> guideColumn;

    @Override
    public void setMainApp(MainGUI main) {
        guideAllLanWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {
        guideColumn.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });

        tblGuide.getItems().clear();

        Vector<String> rs = businessLogic.getTourguidesAllLanguages();

        if(!rs.isEmpty()){
            tblGuide.getItems().addAll(rs);
        }else{
            tblGuide.getItems().add("There is no such tourguide");
        }

    }

    @FXML
    void onClickBack(){
        guideAllLanWin.showQuery();
    }
}
