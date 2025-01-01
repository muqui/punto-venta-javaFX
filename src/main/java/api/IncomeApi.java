/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.ExpenseDTO;
import dto.IncomeDTO;
import dto.IncomeNameDTO;
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
public class IncomeApi {

    ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    String baseUrl = configManager.getProperty("api.base.url");
    String endpointIncomes = configManager.getProperty("api.endpoint.incomes");
    String endpointIncomesName = configManager.getProperty("api.endpoint.incomes.name");
    String endpointExpenses = configManager.getProperty("api.endpoint.expenses");

    public void crearIngreso(IncomeDTO incomeDto) {
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(incomeDto);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointIncomes)) // Cambiar el endpoint a POST si es necesario
                    //.uri(new URI("http://localhost:3000/incomes")) // Cambiar el endpoint a POST si es necesario
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiado a POST
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Ingreso guardado  exitosamente.");
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

    public void crearNombreIngreso(IncomeNameDTO incomeNameDto) {
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(incomeNameDto);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointIncomesName))
                    //.uri(new URI("http://localhost:3000/incomes/name")) // Cambiar el endpoint a POST si es necesario
                    .header("Content-Type", "application/json")
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

    public ObservableList<IncomeNameDTO> ComboIncomeName() {
        ObservableList<IncomeNameDTO> departamentObservableList = null;
        try {
            String urlString = baseUrl + endpointIncomesName; // Cambia esto por la URL correcta de tu API
            // String urlString = "http://localhost:3000/incomes/name"; // Cambia esto por la URL correcta de tu API
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                List<IncomeNameDTO> incomeName = mapper.readValue(inline.toString(), new TypeReference<List<IncomeNameDTO>>() {
                });

                departamentObservableList = FXCollections.observableArrayList(incomeName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return departamentObservableList;
    }

    public void crearEgreso(ExpenseDTO expenseDTO) {

        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(expenseDTO);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointExpenses))
                    //.uri(new URI("http://localhost:3000/expenses")) // Cambiar el endpoint a POST si es necesario
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiado a POST
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Egreso  guardado exitosamente.");
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

    public IncomesResponseDTO incomeResponseDTO(String startDay, String endDay, String category) {
        IncomesResponseDTO response = null;

        // Codificar el userName para que no tenga espacios
        String encodedcategory = URLEncoder.encode(category, StandardCharsets.UTF_8);

        try {
            String urlString = baseUrl+endpointIncomes+"?startDate=" + startDay + "&endDate=" + endDay + "&name=" + encodedcategory; // Cambia esto por la URL correcta de tu API
                   
            //String urlString = "http://localhost:3000/incomes?startDate=" + startDay + "&endDate=" + endDay + "&name=" + encodedcategory; // Cambia esto por la URL correcta de tu API
            System.out.println("Icome String " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();

                // Leemos la respuesta en el nuevo formato OrderDetailsResponseDTO
                response = mapper.readValue(inline.toString(), IncomesResponseDTO.class);

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
