package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import exceptions.NoChange;
import exceptions.NotBelong;
import exceptions.UncompletedRequest;
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
/**
 * This class aims to deal with the window that deletes a person from the restaurant database
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class deletePersonController implements Controller {

    private MainGUI mainWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

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

    /**
     * Method that sets this window as the main window
     * @param main MainGUI - Current window
     */
    @Override
    public void setMainApp(MainGUI main) {
        mainWin = main;
    }

    /**
     * Method to initialize the information in the UI
     */
    @Override
    public void initializeInformation()  {
      fillTable();
      resetFields();
    }

    /**
     * Method to reset the different fields in the UI
     */
    private void resetFields(){
        errorLbl.setText("");
        correctLbl.setText("");
        name.setText("");
        id.setText("");
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }

    /**
     * Method that deletes a person from the restaurant database
     */
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
            } catch (UncompletedRequest e) {
                errorLbl.setText("Transaction could not be done. Please change the fields' information.");
            } catch (NotBelong e) {
                errorLbl.setText("There is no such person in the database.");
            }
        }
    }

    /**
     * Method to fill the table with the initial information
     */
    private void fillTable(){
        col1.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });
        // clear table
        personTable1.getItems().clear();

        try {
            // fill table with current people in the database
            Vector<String> rs = businessLogic.getAllPeople();
            if (!rs.isEmpty()) {
                personTable1.getItems().addAll(rs);
            } else {
                personTable1.getItems().add("No people in the database");
            }
        } catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }
    }



}
