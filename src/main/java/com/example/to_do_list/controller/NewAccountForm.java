package com.example.to_do_list.controller;

import com.example.to_do_list.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class NewAccountForm implements Initializable {
    public TextField txtUserName;
    public TextField txtConfirmPassword;
    public TextField txtNewPassword;
    public TextField txtEmail;
    public Label lblPasswordNotMatch;
    public Label lblPasswordNotMatch2;
    public Label lblIDShow;
    public AnchorPane rootCreateAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setVisibility(false);
        setDisableTextField(true);

    }

    public void txtPasswordOnKeyPressed(KeyEvent keyEvent) {
        txtNewPassword.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                txtConfirmPassword.requestFocus();
            }
        });
    }

    public void txtConfirmPasswordOnAction(ActionEvent actionEvent) {
        register();
    }

    public void btnRegister(ActionEvent actionEvent) {
        register();
    }

    public void register(){
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        String id = lblIDShow.getText();
        String userName = txtUserName.getText();
        String email = txtEmail.getText();



        if (newPassword.equals(confirmPassword)){
            borderStyleSet("transparent");
            setVisibility(false);

            Connection connection = DBConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert  into user values (?,?,?,?)");
                preparedStatement.setObject(1,id);
                preparedStatement.setObject(2,userName);
                preparedStatement.setObject(3,email);
                preparedStatement.setObject(4,confirmPassword);

                int i = preparedStatement.executeUpdate();

                if (i != 0){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Query Success Execute");
                    alert.showAndWait();
                    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/example/to_do_list/login-form.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = (Stage) rootCreateAccount.getScene().getWindow();
                    stage.setScene(scene);
                    stage.centerOnScreen();


                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR," Something Went Wrong");
                    alert.showAndWait();
                }



            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }


        }
        else {
            borderStyleSet("red");
            txtNewPassword.requestFocus();
            setVisibility(true);

        }
    }

    public void setVisibility(boolean visible){
        lblPasswordNotMatch.setVisible(visible);
        lblPasswordNotMatch2.setVisible(visible);
    }

    public void borderStyleSet (String color){
        txtNewPassword.setStyle("-fx-border-color:"+color);
        txtConfirmPassword.setStyle("-fx-border-color: "+color);
    }

    public void btnAddUserVisible(ActionEvent actionEvent) {
        setAutoGenerateID();
        setDisableTextField(false);
        txtUserName.requestFocus();

    }

    public void setDisableTextField (boolean disableTextField){
        txtUserName.setDisable(disableTextField);
        txtEmail.setDisable(disableTextField);
        txtNewPassword.setDisable(disableTextField);
        txtConfirmPassword.setDisable(disableTextField);
    }

    public void setAutoGenerateID(){

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            //Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select id from user order by id desc limit 1");

            boolean isExist = resultSet.next();

            if (isExist){
                String oldId = resultSet.getString(1);

               // int length = oldId.length();

                oldId = oldId.substring(1, oldId.length());

                int intId = Integer.parseInt(oldId);

                intId = intId+1;

               // lblIDShow.setText("U00"+intId);
                if (intId < 10){
                    lblIDShow.setText("U00"+intId);
                }
                else if (intId < 100){
                    lblIDShow.setText("U0"+intId);
                }else {
                    lblIDShow.setText("U"+intId);
                }

            }
            else {
                lblIDShow.setText("U001");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
