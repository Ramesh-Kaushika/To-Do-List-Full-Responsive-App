package com.example.to_do_list.controller;

import com.example.to_do_list.db.DBConnection;
import com.example.to_do_list.tm.toDoListTM;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ToDoFormController implements Initializable {
    public Label lblUserName;
    public Label lblUserID;
    public AnchorPane rootToDoForm;
    public Pane rootSubRoot;
    public TextField txtAddToList;
    public ListView<toDoListTM>lstToDo;
    public TextField txtUpdateField;
    public Button btnDeleteID;
    public Button btnUpdateID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String userName = LoginFormController.loginUserName;
        String id = LoginFormController.loginUserID;

        lblUserName.setText("Hi! "+userName+" Welcome to To-Do List" );
        lblUserID.setText(id);
        
        rootSubRoot.setVisible(false);

        loadList();

        setDisableCommon(true);

        lstToDo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<toDoListTM>() {
            @Override
            public void changed(ObservableValue<? extends toDoListTM> observableValue, toDoListTM toDoListTM, toDoListTM t1) {
                txtUpdateField.requestFocus();
                rootSubRoot.setVisible(false);
                setDisableCommon(false);

                toDoListTM selectedItem = lstToDo.getSelectionModel().getSelectedItem();
                if (selectedItem == null){return;}
                txtUpdateField.setText(selectedItem.getDescription());

            }
        });


    }

    public void setDisableCommon(boolean isDisable){
        txtUpdateField.setDisable(isDisable);
        btnDeleteID.setDisable(isDisable);
        btnUpdateID.setDisable(isDisable);
    }


    public void btnLogOutOnAction(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Want To Log Out", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)){
            try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/example/to_do_list/login-form.fxml"));

                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) rootToDoForm.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void btnAddNewToDoOnAction(ActionEvent actionEvent) {

        rootSubRoot.setVisible(true);
        txtAddToList.requestFocus();
        lstToDo.getSelectionModel().clearSelection();
        setDisableCommon(true);
        rootSubRoot.setVisible(true);
        txtUpdateField.clear();

    }

    public void btnAddToList(ActionEvent actionEvent) {

        String id = autoGenerate();
        String description = txtAddToList.getText();
        String user_id = lblUserID.getText();
        try {
        Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values (?,?,?)");

            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,description);
            preparedStatement.setObject(3,user_id);

            preparedStatement.executeUpdate();

            txtAddToList.clear();
            rootSubRoot.setVisible(false);


            loadList();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public String autoGenerate(){

        Connection connection = DBConnection.getInstance().getConnection();

        String id = null;

        try {
            Statement statement = connection.createStatement();
            //Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select id from todo order by id desc limit 1");

            boolean isExist = resultSet.next();

            if (isExist){
                String oldId = resultSet.getString(1);

                // int length = oldId.length();

                oldId = oldId.substring(1, oldId.length());

                int intId = Integer.parseInt(oldId);

                intId = intId+1;

                // lblIDShow.setText("U00"+intId);
                if (intId < 10){
                   id ="T00"+intId;
                }
                else if (intId < 100){
                   id = "T0"+intId;
                }else {
                    id = "T"+intId;
                }

            }
            else {
                id = "T001";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;

    }

    public void loadList(){

        ObservableList<toDoListTM> items = lstToDo.getItems();
        items.clear();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select  * from todo where user_id = ?");

            preparedStatement.setObject(1,LoginFormController.loginUserID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String id = resultSet.getString(1);
                String description = resultSet.getString(2);
                String user_id = resultSet.getString(3);

                toDoListTM toDoListtm = new toDoListTM(id, description, user_id);
                items.add(toDoListtm);
            }

            lstToDo.refresh();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void btnDelete(ActionEvent actionEvent) {
    }

    public void btnUpdate(ActionEvent actionEvent) {
    }
}
