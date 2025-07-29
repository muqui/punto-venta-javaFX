/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.ExpenseNameDTO;
import dto.ExpenseResponseDTO;
import dto.IncomesResponseDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
public class ExpenseApi {

    ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    String baseUrl = configManager.getProperty("api.base.url");
    String enpointExpenseName = configManager.getProperty("api.endpoint.expense.name");
    String endpointExpenses = configManager.getProperty("api.endpoint.expenses");
      String clientId = configManager.getProperty("x.client.id");

      String token = App.getUsuario().getToken();
    public void crearNombreEgreso(ExpenseNameDTO expenseNameDTO) {

        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(expenseNameDTO);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + enpointExpenseName)) // Cambiar el endpoint a POST si es necesario
                    // .uri(new URI("http://localhost:3000/expenses/name")) // Cambiar el endpoint a POST si es necesario
                    .header("Content-Type", "application/json")
                     .header("x-client-id", clientId) 
                      .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiado a POST
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                //   showAlert("Éxito", "Producto creado exitosamente.");
            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + responseBody);

                // Parsear el cuerpo de la respuesta como JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Acceder al campo "message"
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);

                // Manejar posibles errores devueltos por el servidor
                if (message.equalsIgnoreCase("product with the same barcode already exists")) {
                    //     showAlert("Info", "El producto ya existe, puede actualizarlo.");
                } else {
                    //  showAlert("Error", "Error al crear el producto. Código de estado: " + response.statusCode());
                }
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            //  showAlert("Error", "Error al conectarse con la API.");
        }
    }

    public ObservableList<ExpenseNameDTO> ComboExpenseName() {

        ObservableList<ExpenseNameDTO> departamentObservableList = null;
        try {

            String urlString = baseUrl + enpointExpenseName; // Cambia esto por la URL correcta de tu API
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
                List<ExpenseNameDTO> expenseName = mapper.readValue(inline.toString(), new TypeReference<List<ExpenseNameDTO>>() {
                });

                departamentObservableList = FXCollections.observableArrayList(expenseName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return departamentObservableList;
    }

    public ExpenseResponseDTO expenseResponseDTO(String startDate, String endDate, String name) {
        ExpenseResponseDTO response = null;
        // Codificar el userName para que no tenga espacios
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        try {
            String urlString = baseUrl + endpointExpenses + "?startDate=" + startDate + "&endDate=" + endDate + "&name=" + encodedName;
            //  String urlString = "http://localhost:3000/expenses?startDate="+startDate+"&endDate="+endDate+"&name="+encodedName; // Cambia esto por la URL correcta de tu API
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

                // Leemos la respuesta en el nuevo formato OrderDetailsResponseDTO
                response = mapper.readValue(inline.toString(), ExpenseResponseDTO.class);
                System.out.println("response " + response);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);  // Usar el tipo de alerta proporcionado como parámetro
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
