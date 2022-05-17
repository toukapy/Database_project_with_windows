package controllers;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
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
 * This class aims to deal with the window that handles adding people to the restaurant database
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class addPersonController implements Controller {

    private MainGUI mainWin;
    private BlFacade businessLogic = new BlFacadeImplementation();

    @FXML
    private TableView<String> personTable;
    @FXML
    private TableColumn<String, String> col;


    @FXML
    private TextField name;
    @FXML
    private TextField age;
    @FXML
    private TextField id;
    @FXML
    private TextField food;
    @FXML
    private TextField restaurant;
    @FXML
    private TextField answerField;


    @FXML
    private Label errorLbl;
    @FXML
    private Label correctLbl;


    private String choice="";

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
    public void initializeInformation() {
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
        age.setText("");
        id.setText("");
        food.setText("");
        restaurant.setText("");
        answerField.setText("");
        choice="";
    }

    /**
     * Method to return to the parent window
     */
    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }


    /**
     * This method adds a person to the restaurant database.
     */
    @FXML
    void onClickAddPerson(){
        errorLbl.setText("");
        correctLbl.setText("");

        if(choice.equals(""))
            errorLbl.setText("Please, enter your choice");
        else if ((name.getText().isEmpty() || age.getText().isEmpty()|| id.getText().isEmpty() || food.getText().isEmpty()|| restaurant.getText().isEmpty() ))
            errorLbl.setText("Please, fill all fields");
        else {
            try {
                businessLogic.insertPerson(choice, name.getText(), age.getText(), id.getText(), food.getText(), restaurant.getText());
                fillTable();
                correctLbl.setText("Transaction executed!!");
            }catch (SQLException e){
                errorLbl.setText("An error with the database occurred. Please, try again later.");
            } catch (UncompletedRequest e) {
                errorLbl.setText("Transaction could not be done. Please change the fields' information.");
            }
        }


    }

    /**
     * Method to get the information about whether the user wants to create the objects if they do not exist
     */
    @FXML
    void onClickAnswer(){
        errorLbl.setText("");
        if(answerField.getText().isEmpty()){
            errorLbl.setText("Enter a valid answer (y/n)");
        }else if(answerField.getText().equals("y") || answerField.getText().equals("n")){
            choice = answerField.getText();
        }else{
            errorLbl.setText("Enter a valid answer (y/n)");
        }
    }

    /**
     * Method to fill the table with the initial information
     */
    private void fillTable(){
        col.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });
        // clear table
        personTable.getItems().clear();

        try {
            // fill table with current people in the restaurant database
            Vector<String> rs = businessLogic.getAllPeople();
            if (!rs.isEmpty()) {
                personTable.getItems().addAll(rs);
            } else {
                personTable.getItems().add("No people in the database");
            }
        } catch (SQLException e){
            errorLbl.setText("An error with the database occurred. Please, try again later.");
        }
    }

}
