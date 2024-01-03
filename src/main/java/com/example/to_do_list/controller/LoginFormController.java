package com.example.to_do_list.controller;

import com.example.to_do_list.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    public TextField txtUserName;
    public TextField txtPassword;
    public Label lblCreateNewAccount;
    public AnchorPane rootLogin;

    public static String loginUserName;
    public static String loginUserID;
    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUserName.requestFocus();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void txtUserNameOnKeyPressed(KeyEvent keyEvent) {
        txtUserName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                txtPassword.requestFocus();
            }
        });
    }

    public void btnLogin(ActionEvent actionEvent) {
        setLogin();

    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        setLogin();
    }

    public void lblCreateAccountOnMouseClicked(MouseEvent mouseEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/to_do_list/new-account-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) rootLogin.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Create New Account Form");
        stage.centerOnScreen();

    }

    public void setLogin() {

        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where user_name = ? and password = ?");
            preparedStatement.setObject(1,userName);
            preparedStatement.setObject(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();



            if (resultSet.next()){

                 loginUserName = resultSet.getString(2);
                 loginUserID = resultSet.getString(1);

                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/example/to_do_list/ToDoForm.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) rootLogin.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("TO Do LIST Form");
                stage.centerOnScreen();

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong");
                alert.showAndWait();
                txtUserName.clear();
                txtPassword.clear();
                txtUserName.requestFocus();

            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}