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
import javafx.util.StringConverter;
import org.json.JSONObject;

/**
 *
 * @author albert
 */
public class CategoriesApi {

    ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    String baseUrl = configManager.getProperty("api.base.url");
    String endpointCategories = configManager.getProperty("api.endpoint.categories");
      String clientId = configManager.getProperty("x.client.id");
    //api.endpoint.categories=/categories
     String token = App.getUsuario().getToken();
    // api.base.url=http://localhost:3000
    public ObservableList<DepartmentDTO> fillChoiceBoxDepartament() {
        System.out.println("TOKEN DESDE CATEGORIESAPI = " + token);
        String baseUrl = configManager.getProperty("api.base.url");
        String endpoint = configManager.getProperty("api.endpoint.categories");
        ObservableList<DepartmentDTO> departamentList = null;
        try {
            String urlString = baseUrl + endpoint;  // Cambia esto por la URL correcta de tu API
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-client-id", clientId);
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
                List<DepartmentDTO> departments = mapper.readValue(inline.toString(), new TypeReference<List<DepartmentDTO>>() {
                });

                departamentList = FXCollections.observableArrayList(departments);
                System.out.println("lista " + departments.get(0).getName());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return departamentList;
    }

    public void createDepartment(DepartmentDTO departamentDto) {
        //
        String apiUrl = baseUrl + endpointCategories;

        // String apiUrl = "http://localhost:3000/categories";
        System.out.println("Llamando a la API: " + apiUrl);

        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonProduct = objectMapper.writeValueAsString(departamentDto);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("x-client-id", clientId)
                      .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                showAlert("Éxito", "Departamento creado exitosamente.");
            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + response.body());

                // Parse the response body as JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Access the "message" field
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);

                showAlert("Error", "Error al crear departmamento:  " + message);

            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            showAlert("Error", "Error al conectarse con la API.");
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
