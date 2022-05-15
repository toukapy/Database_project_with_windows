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

public class addPersonController implements Controller {

    private MainGUI mainWin;
    private BlFacadeImplementation businessLogic = new BlFacadeImplementation();

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

    @Override
    public void setMainApp(MainGUI main) {
        mainWin = main;
    }

    @Override
    public void initializeInformation() throws SQLException {
        fillTable();
        resetFields();
    }

    private void resetFields(){
        errorLbl.setText("");
        correctLbl.setText("");
        name.setText("");
        age.setText("");
        id.setText("");
        food.setText("");
        restaurant.setText("");
        choice="";
    }
    @FXML
    void onClickBack(){
        mainWin.showTransaction();
    }


    @FXML
    void onClickAddPerson(){
        errorLbl.setText("");
        correctLbl.setText("");
        if(choice.equals(""))
            errorLbl.setText("Please, enter your choice");
        if ((name.getText().isEmpty() || age.getText().isEmpty()|| id.getText().isEmpty() || food.getText().isEmpty()|| restaurant.getText().isEmpty() ))
            errorLbl.setText("Please, fill all fields");
        else {
            try {
                businessLogic.insertPersonUI(choice, name.getText(), age.getText(), id.getText(), food.getText(), restaurant.getText());
                fillTable();
                correctLbl.setText("Transaction executed!!");
            } catch (SQLException e) {
                errorLbl.setText("Transaction could not be executed. Please, change the data and try again.");
            }
        }


    }

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

    private void fillTable(){
        col.setCellValueFactory(data ->{
            return new SimpleStringProperty(data.getValue());
        });
        // clear table
        personTable.getItems().clear();


        // fill table with current people in the database
        Vector<String> rs = businessLogic.getAllPeople();
        if(rs!=null){
            personTable.getItems().addAll(rs);
        }else{
            personTable.getItems().add("No people in the database");
        }
    }

}
