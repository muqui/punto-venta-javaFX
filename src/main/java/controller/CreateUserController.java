/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.UserApi;
import dto.UserDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CreateUserController implements Initializable {

    UserDTO user = new UserDTO();
    UserApi userApi = new UserApi();
    @FXML
    private Button btnCreatedUser;

    @FXML
    private ComboBox<String> comboTypeUser;

    @FXML
    private TextField txtAdress;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtCountry;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPhone;

    @FXML
    void onActionCreateUser(ActionEvent event) {
        user.setName(txtName.getText());
        user.setEmail(txtEmail.getText());
        user.setPassword(txtPassword.getText());
        user.setAddress(txtAdress.getText());
        user.setPhone(txtPhone.getText());
        user.setCity(txtCity.getText());
        user.setCountry(txtCountry.getText());
        String selectedType = comboTypeUser.getSelectionModel().getSelectedItem();
        user.setIsAdmin(selectedType);

        userApi.createUser(user);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        comboTypeUser.getItems().addAll("user", "admin", "tech");
    }

}
