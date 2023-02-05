/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import beans.Usuario;
import com.albertocoronanavarro.puntoventafx.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class LoginController implements Initializable {
    private Usuario usuario = new Usuario();
    @FXML
    private Button btnAceptar;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsuer;

    @FXML
    void btnAceptarAction(ActionEvent event) throws IOException {
       
       
        this.getUsuario().setNombre(txtUsuer.getText().toString());
        this.getUsuario().setPassword(txtPassword.getText().toString());
        if(getUsuario().getNombre().equalsIgnoreCase("Admin") && getUsuario().getPassword().equals("Corona")){
            
         App main = new App();
         main.setUsuario(this.usuario);
       
           
          
             main.changeScene("/views/principal.fxml", getUsuario(), event);
        }
        else{
             Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error datos incorrectos intente nuevamente.");
                alert.showAndWait();
        }
       
        
       

    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //  this.usuario.setNombre("Corona");
      //  this.usuario.setPassword("dsdss");
        // TODO
         
    }    

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
