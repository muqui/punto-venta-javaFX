/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.IncomeDTO;
import dto.OrderServiceDTO;
import dto.ProductFindDTO;
import helper.ConfigLoader;
import helper.PrintOrderService;
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
import javafx.scene.control.Alert;
import org.json.JSONObject;

/**
 *
 * @author albert
 */
public class OrderRepairApi {

    String printerName = ConfigLoader.getOrderPrinterName();
    String token = App.getUsuario().getToken();
    ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    String baseUrl = configManager.getProperty("api.base.url");
    String endpointCreatedServiceOrder = configManager.getProperty("api.endpoint.created.service.order");
    String endpointRepairservice = configManager.getProperty("api.endpoint.repair.service");
    String endPointRepairServiceUpdate = configManager.getProperty("api.endpoint.repaor.service.update");
    String clientId = configManager.getProperty("x.client.id");
    //api.endpoint.repair.service

    public void createdOrderService(OrderServiceDTO orderServiceDTO) {

        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(orderServiceDTO);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endpointCreatedServiceOrder)) // Cambiar el endpoint a POST si es necesario
                    .header("Content-Type", "application/json")
                    .header("x-client-id", clientId)
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiado a POST
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("RESPONSE= " + response.body());

            if (response.statusCode() == 200 || response.statusCode() == 201) {

                OrderServiceDTO order = objectMapper.readValue(response.body(), OrderServiceDTO.class);
                System.out.println("Orden insertada= " + order.toString());
                //PrintOrderService.sentToPrinter(order);
                switch (printerName) {
                    case "58mm":
                        PrintOrderService.printOrderReparir58mm(order);
                        break;
                    case "80mm":
                        PrintOrderService.printOrderReparir80mm(order);
                        break;
                    case "Letter":
                    default:
                        PrintOrderService.printOrderReparirLetter(order);
                        break;
                }

                PrintOrderService.printOrderReparir80mmDataClient(order);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Order de servcio creada con exito.");

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
                    showAlert(Alert.AlertType.ERROR, "ERROR", "Error al crear la orden.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            showAlert(Alert.AlertType.ERROR, "Éxito", "Order de servcio creada con exito.");
        }
    }

    public String createdOrderServiceRemote(OrderServiceDTO orderServiceDTO) {
        String baseUrlRemote = configManager.getProperty("api.base.url.back");
        String clientIdRemote = configManager.getProperty("database.remote.remote");
        String folio = "";
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(orderServiceDTO);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrlRemote + endpointCreatedServiceOrder)) // Cambiar el endpoint a POST si es necesario
                    .header("Content-Type", "application/json")
                    .header("x-client-id", clientIdRemote)
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .POST(HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiado a POST
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("RESPONSE= " + response.body());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                // Convertimos a objeto JSON
                JSONObject jsonObject = new JSONObject(response.body());
                folio = jsonObject.getString("folio");
                OrderServiceDTO order = objectMapper.readValue(response.body(), OrderServiceDTO.class);
                System.out.println("Orden insertada= " + order.toString());

            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + responseBody);

                // Parsear el cuerpo de la respuesta como JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Acceder al campo "message"
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);

                showAlert(Alert.AlertType.ERROR, "ERROR", "Error al crear la orden remoto.");
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            showAlert(Alert.AlertType.ERROR, "ERROR", "Error al crear la orden remoto." + e);
        }
        return folio;
    }

    public List<OrderServiceDTO> fetchOrdersService(String StartDate, String endDate, String status) throws IOException {
        String encodedStatus = URLEncoder.encode(status, StandardCharsets.UTF_8);

        //http://localhost:3000/repair-cellphones?startDate=2025-05-01&endDate=2025-05-09&status=Reparado
        List<OrderServiceDTO> orderServices = null;
        String urlString = baseUrl + endpointRepairservice + "?startDate=" + StartDate + "&endDate=" + endDate + "&status=" + encodedStatus;
        System.out.println("endpoint obtener todas la ordenes de servicio" + urlString);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token); // <-- Agregamos el token aquí
        conn.setRequestProperty("x-client-id", clientId);
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
            System.out.println("Busqueda cadena= " + inline);
            ObjectMapper mapper = new ObjectMapper();
            orderServices = mapper.readValue(inline.toString(), new TypeReference<List<OrderServiceDTO>>() {
            });

            return orderServices;
        }
    }

    public OrderServiceDTO getOrderService(String folio) throws IOException {

        OrderServiceDTO orderServices = null;
        String urlString = baseUrl + endpointRepairservice + "/" + folio;
        System.out.println("endpoint obtener todas la ordenes de servicio" + urlString);
        // String urlString = baseUrl + endpointProductsSearch+"?name=" + nameProduct;
        // String urlString = "http://localhost:3000/products/search?name=" + value;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token); // <-- Agregamos el token aquí
        conn.setRequestProperty("x-client-id", clientId);
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
            System.out.println("Busqueda cadena= " + inline);
            ObjectMapper mapper = new ObjectMapper();
            orderServices = mapper.readValue(inline.toString(), new TypeReference<OrderServiceDTO>() {
            });

            return orderServices;
        }
    }

    public void updateOrderRepair(OrderServiceDTO orderServiceDTO) {

        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(orderServiceDTO);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + endPointRepairServiceUpdate + "/" + orderServiceDTO.getFolio())) // Asegúrate de pasar el ID del producto

                    //  .uri(new URI("http://localhost:3000/products/" + product.getBarcode())) // Asegúrate de pasar el ID del producto
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .header("x-client-id", clientId)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiar POST a PATCH
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Order actualizada con exito.");
            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + responseBody);

                // Parsear el cuerpo de la respuesta como JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Acceder al campo "message"
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);
                showAlert(Alert.AlertType.ERROR, "Error", "Orden Finalizada no se puede modificar.");
                // Manejar error de autenticación
                if (message.equalsIgnoreCase("product not found")) {
                    //    showAlert("Info", "El producto no existe.");
                } else {
                    //      showAlert("Error", "Error al actualizar el producto. Código de estado: " + response.statusCode());
                }
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            //  showAlert("Error", "Error al conectarse con la API.");
        }

    }

    public void updateOrderRepairRemote(OrderServiceDTO orderServiceDTO) {
        System.out.println("actualizar remoto");
        String baseUrlRemote = configManager.getProperty("api.base.url.back");
        String clientIdRemote = configManager.getProperty("database.remote.remote");
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(orderServiceDTO);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrlRemote + endpointRepairservice + "/" + orderServiceDTO.getFolio())) // Asegúrate de pasar el ID del producto

                    //  .uri(new URI("http://localhost:3000/products/" + product.getBarcode())) // Asegúrate de pasar el ID del producto
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .header("x-client-id", clientIdRemote)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiar POST a PATCH
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Order actualizada con exito.");
            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + responseBody);

                // Parsear el cuerpo de la respuesta como JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Acceder al campo "message"
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);
                showAlert(Alert.AlertType.ERROR, "Error", "Orden Finalizada no se puede modificar.");
                // Manejar error de autenticación
                if (message.equalsIgnoreCase("product not found")) {
                    //    showAlert("Info", "El producto no existe.");
                } else {
                    //      showAlert("Error", "Error al actualizar el producto. Código de estado: " + response.statusCode());
                }
            }

        } catch (Exception e) {
            System.out.println("Error API " + e);
            //  showAlert("Error", "Error al conectarse con la API.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);  // Usar el tipo de alerta proporcionado como parámetro
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
