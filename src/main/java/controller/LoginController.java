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
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

 private void login(ActionEvent event) {
    // Crear y mostrar el Splash
    Stage splashStage = new Stage(StageStyle.TRANSPARENT); // Cambiado a TRANSPARENT
    splashStage.setAlwaysOnTop(true);

    try {
        Parent splashRoot = FXMLLoader.load(getClass().getResource("/views/splash.fxml"));
        
        Scene splashScene = new Scene(splashRoot);
        splashScene.setFill(Color.TRANSPARENT); // Establece fondo transparente
        splashStage.setScene(splashScene);
        
        splashStage.toFront();
        splashStage.show();
    } catch (IOException e) {
        e.printStackTrace();
        txtError.setText("No se pudo cargar el splash");
        return;
    }

    // Datos del usuario
    usuario.setEmail(txtUsuer.getText());
    usuario.setPassword(txtPassword.getText());
    usuario.setName(txtUsuer.getText());

    // Task en segundo plano
    Task<Void> loginTask = new Task<>() {
        @Override
        protected Void call() {
            try {
                HttpResponse<String> response = loginApi.login(usuario);

                if (response == null) {
                    Platform.runLater(() -> txtError.setText("No se puede establecer conexión con el servidor"));
                    return null;
                }

                if (response.statusCode() == 201) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    UserDTO authenticatedUser = objectMapper.readValue(response.body(), UserDTO.class);

                    JSONObject payload = JwtUtils.decodePayload(authenticatedUser.getToken());
                    if (payload != null) {
                        authenticatedUser.setEmail(payload.optString("email"));
                        authenticatedUser.setName(payload.optString("name"));
                        authenticatedUser.setId(payload.optInt("id"));

                        JSONArray rolesArray = payload.optJSONArray("roles");
                        String role = rolesArray != null && rolesArray.length() > 0 ? rolesArray.getString(0) : "user";
                        authenticatedUser.setIsAdmin(role);
                    }

                    Platform.runLater(() -> {
                        try {
                            App main = new App();
                            main.setUsuario(authenticatedUser);
                            main.changeScene("/views/Principal.fxml", authenticatedUser, event);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });

                } else {
                    ApiResponseDTO apiResponse = new ObjectMapper().readValue(response.body(), ApiResponseDTO.class);
                    Platform.runLater(() -> txtError.setText(""+ apiResponse.getMessage()));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> txtError.setText("Error durante el login"));
            }
            return null;
        }

        @Override
        protected void succeeded() {
            splashStage.close();
        }

        @Override
        protected void failed() {
            splashStage.close();
        }

        @Override
        protected void cancelled() {
            splashStage.close();
        }
    };

    new Thread(loginTask).start();
}



}
