/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.DepartmentDTO;
import dto.UserDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.json.JSONObject;

/**
 *
 * @author albert
 */
public class UserApi {

    String token = App.getUsuario().getToken();

    ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    String baseUrl = configManager.getProperty("api.base.url");
    String endpointUser = configManager.getProperty("api.endpoint.users");  //api.endpoint.users
    String enpointCreateUser = configManager.getProperty("api.endpoint.signup");

    public ObservableList<UserDTO> fillChoiceBoxUser() {
        ObservableList<UserDTO> userList = null;
        try {
            String urlString = baseUrl + endpointUser; // Cambia esto por la URL correcta de tu API
            // String urlString = "http://localhost:3000/users"; // Cambia esto por la URL correcta de tu API
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token); // <-- Agregamos el token aquí
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                List<UserDTO> users = mapper.readValue(inline.toString(), new TypeReference<List<UserDTO>>() {
                });

                userList = FXCollections.observableArrayList(users);
                System.out.println("lista users" + users.get(0).getName());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public String createUser(UserDTO user) {
        String responseBody = null;
        try {
            // Convertir user a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(user);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + enpointCreateUser)) // Cambiar el endpoint a POST si es necesario
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiado a POST
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario creado  exitosamente.");
            } else {
                responseBody = response.body();
                System.out.println("Resultado =" + responseBody);

                // Parsear el cuerpo de la respuesta como JSON
                //    JSONObject jsonResponse = new JSONObject(responseBody);
                // Acceder al campo "message"
                //        message = jsonResponse.getString("message");
                //         System.out.println("Message = " + message);
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            //  showAlert("Error", "Error al conectarse con la API.");
        }
        return responseBody;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);  // Usar el tipo de alerta proporcionado como parámetro
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void updateUser(UserDTO user) {
        System.out.println("USUARIO PARA ACTUALIZAR desde la api =  " + user.toString());
        try {
            if(user.getPassword().equals("") ){
                user.setPassword(null);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(user);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointUser + "/" + user.getId())) // Asegúrate de pasar el ID del producto
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiar POST a PATCH
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario actualizado con exito.");
            } else {
                String responseBody = response.body();
                JSONObject jsonResponse = new JSONObject(responseBody);
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);
                showAlert(Alert.AlertType.ERROR, "Error", "Usuario  no se puede modificar.");
                if (message.equalsIgnoreCase("product not found")) {
                } else {
                }
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
        }
    }

}
