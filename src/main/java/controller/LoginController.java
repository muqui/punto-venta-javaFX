/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import api.LoginApi;
import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.ApiResponseDTO;
import dto.UserDTO;
import helper.JwtUtils;
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
import java.util.List;
import javafx.scene.control.Label;
import org.json.JSONArray;
import org.json.JSONObject;

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

    LoginApi loginApi = new LoginApi();

    @FXML
    void onActionPassword(ActionEvent event) throws IOException {
        login(event);
    }

    @FXML
    void onActionUser(ActionEvent event) throws IOException {
        login(event);
    }

    @FXML
    void btnAceptarAction(ActionEvent event) throws IOException {

        login(event);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtError.setText("");

    }

    private void login(ActionEvent event) throws JsonProcessingException, IOException {
        usuario.setEmail(txtUsuer.getText().toString());
        usuario.setPassword(txtPassword.getText().toString());
        usuario.setName(txtUsuer.getText().toString());
        HttpResponse<String> response = loginApi.login(usuario);

        if (response == null) {
            txtError.setText("No se puede establecer conexion con el servidor");
            return;
        }

        if (response.statusCode() == 201) {
            // Deserializar el JSON en un objeto UsuarioDTO
            ObjectMapper objectMapper = new ObjectMapper();
            UserDTO authenticatedUser = objectMapper.readValue(response.body(), UserDTO.class);

            //  System.out.println("TOKEN: " + authenticatedUser.getToken());
            JSONObject payload = JwtUtils.decodePayload(authenticatedUser.getToken());
            if (payload != null) {
                String email = payload.optString("email");
                String name = payload.optString("name");
                int id = payload.optInt("id");

                JSONArray rolesArray = payload.optJSONArray("roles");
                String role = rolesArray != null && rolesArray.length() > 0 ? rolesArray.getString(0) : "user";

                authenticatedUser.setName(name);
                authenticatedUser.setEmail(email);
                authenticatedUser.setId(id);
                authenticatedUser.setIsAdmin(role);
                System.out.println("Nombre: " + name);
                System.out.println("Email: " + email);
                System.out.println("ID: " + id);
                System.out.println("Rol: " + role);
            }
            // Manejar autenticación exitosa
            App main = new App();
            //System.out.println("token desde el usuario login " + authenticatedUser.getToken());
            // System.out.println("OBJETO RECIBIDO DESDE LOGIN: " + authenticatedUser.toString());
            main.setUsuario(authenticatedUser);
            main.changeScene("/views/Principal.fxml", authenticatedUser, event);
            //System.out.println("Login exitoso");
        } else {

            ApiResponseDTO apiResponse = new ObjectMapper().readValue(response.body(), ApiResponseDTO.class);

            if (apiResponse.getMessage() instanceof String) {
                String msg = (String) apiResponse.getMessage();
                System.out.println("Mensaje simple: " + msg);
            } else if (apiResponse.getMessage() instanceof List) {
                List<?> msgs = (List<?>) apiResponse.getMessage();
                msgs.forEach(System.out::println);
            }
            txtError.setText("Error de conexión: " + apiResponse.getMessage());
        }

    }

}
