/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.UserApi;
import dto.UserDTO;
import helper.AlertMessage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CreateUserController implements Initializable {

    private UserDTO user = new UserDTO();
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
        getUser().setName(txtName.getText());
        getUser().setEmail(txtEmail.getText());
        getUser().setPassword(txtPassword.getText());
        getUser().setAddress(txtAdress.getText());
        getUser().setPhone(txtPhone.getText());
        getUser().setCity(txtCity.getText());
        getUser().setCountry(txtCountry.getText());
        String selectedType = comboTypeUser.getSelectionModel().getSelectedItem();
        getUser().setIsAdmin(selectedType);
        System.out.println("id" + getUser().getId());
       // userApi.createUser(user);
        
         if ( getUser().getId() == 0) {
        String message =  userApi.createUser(getUser());
        if(message == null){
             Stage stage = (Stage) txtPhone.getScene().getWindow();
                stage.close();

        }
        else{
            AlertMessage.showAlert(Alert.AlertType.ERROR, "Error al crear Usuario", message);

        }
        
             System.out.println("Mensaje al crear usuario " +  message);
    } else {
       userApi.updateUser(user); // Actualizar usuario
         Stage stage = (Stage) txtPhone.getScene().getWindow();
                stage.close();
    }
          
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        comboTypeUser.getItems().addAll("user", "admin", "tech");
    }

    /**
     * @return the user
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserDTO user) {
        this.user = user;
         txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        txtPassword.setText(user.getPassword());
        txtAdress.setText(user.getAddress());
        txtPhone.setText(user.getPhone());
        txtCity.setText(user.getCity());
        txtCountry.setText(user.getCountry());
        comboTypeUser.getSelectionModel().select(user.getIsAdmin());
    }

}
