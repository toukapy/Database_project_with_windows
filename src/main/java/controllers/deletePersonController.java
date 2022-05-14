package controllers;

import businessLogic.BlFacadeImplementation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import uis.Controller;
import uis.MainGUI;

import java.sql.SQLException;
import java.util.Vector;

public class deletePersonController implements Controller {

    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> personTable1;

    @FXML
    private TableColumn<String, String> col1;



    @FXML
    private TextField name;

    @FXML
    private TextField id;



    @FXML
    private Label errorLbl;
    @FXML
    private Label correctLbl;

    @Override
    public void setMainApp(MainGUI main) {
        mainWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {
      fillTable();
      errorLbl.setText("");
      correctLbl.setText("");
    }

    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }

    @FXML
    void onClickDeletePerson(){
        errorLbl.setText("");
        correctLbl.setText("");
        if ((name.getText().isEmpty() || id.getText().isEmpty()))
            errorLbl.setText("Please, fill name and id fields");
        else {
            try {

                // execute query and fill the table
                businessLogic.deletePerson(name.getText(), id.getText());
                fillTable();

                correctLbl.setText("Transaction executed!!");
            } catch(SQLException e){
                errorLbl.setText("Transaction could not be executed. Please, change the data and try again.");
            }
        }
    }

    private void fillTable(){
        col1.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });
        // clear first table
        personTable1.getItems().clear();


        // fill first table with current people in the database
        Vector<String> rs = businessLogic.getAllPeople();
        if(rs!=null){
            personTable1.getItems().addAll(rs);
        }else{
            personTable1.getItems().add("No people in the database");
        }
    }



}
