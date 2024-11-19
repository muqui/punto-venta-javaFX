/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ApiResponseDTO;
import dto.UserDTO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class LoginController implements Initializable {

    private UserDTO usuario = new UserDTO();
    @FXML
    private Button btnAceptar;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsuer;

    @FXML
    private Label txtError;

    @FXML
    void btnAceptarAction(ActionEvent event) throws IOException {
        usuario.setEmail(txtUsuer.getText().toString());
        usuario.setPassword(txtPassword.getText().toString());
        usuario.setName(txtUsuer.getText().toString());

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/auth/signin"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + usuario.getEmail() + "\", \"password\":\"" + usuario.getPassword() + "\"}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("resultado =" + response.statusCode());

            if (response.statusCode() == 201) {
                // Deserializar el JSON en un objeto UsuarioDTO
                ObjectMapper objectMapper = new ObjectMapper();
                UserDTO authenticatedUser = objectMapper.readValue(response.body(), UserDTO.class);
                System.out.println("OBJETO RECIBIDO DESDE LOGIN: " + authenticatedUser.toString());

                // Manejar autenticación exitosa
                App main = new App();

                main.setUsuario(authenticatedUser);
                main.changeScene("/views/Principal.fxml", usuario, event);
                System.out.println("Login exitoso");
            } else {

                ApiResponseDTO apiResponse = new ObjectMapper().readValue(response.body(), ApiResponseDTO.class);
                txtError.setText("Error de conexión: " + apiResponse.getMessage());
            }

        } catch (Exception e) {
            System.out.println("error api " + e);
            txtError.setText("ERROR: " + e);
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtError.setText("");

    }
    
    

}
