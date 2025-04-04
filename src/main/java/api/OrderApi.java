/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import beans.Order;
import beans.OrderDetail;
import com.albertocoronanavarro.puntoventafx.App;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.OrderDetailsResponseDTO;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author albert
 */
public class OrderApi {
      String token = App.getUsuario().getToken();
      
    ConfigManager configManager = new ConfigManager(); //carga el archivo de config.properties
    String baseUrl = configManager.getProperty("api.base.url");
    String endpointOrdersSolds = configManager.getProperty("api.endpoint.orders.solds");
    String endpointOrders = configManager.getProperty("api.orders");
    //api.orders =/orders

    // Método para obtener la lista de pedidos como ObservableList
    public OrderDetailsResponseDTO fetchOrderDetails(String dateStar, String dateEnd, String userName, String departament) throws IOException {

        // Codificar el department para que no tenga espacios
        String encodedDepartmentName = URLEncoder.encode(departament, StandardCharsets.UTF_8);
        // Codificar el userName para que no tenga espacios
        String encodedUserName = URLEncoder.encode(userName, StandardCharsets.UTF_8);

        String urlString = baseUrl + endpointOrdersSolds + "?startDate=" + dateStar + "&endDate=" + dateEnd + "&userName=" + encodedUserName + "&departmentName=" + encodedDepartmentName;
        System.out.println("URL 1234= " + urlString);
        // String urlString = "http://localhost:3000/orders/solds?startDate=" + dateStar + "&endDate=" + dateEnd + "&userName=" + encodedUserName + "&departmentName=" + encodedDepartmentName;
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
            OrderDetailsResponseDTO response = mapper.readValue(inline.toString(), OrderDetailsResponseDTO.class);

            return response;
        }
    }

    // Método para obtener la lista de pedidos como ObservableList
    public ObservableList<OrderDetailDTO> fetchOrderDetails() throws IOException {
        // URL url = new URL("http://localhost:3000/orders");x
        URL url = new URL(baseUrl+endpointOrders);
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
            List<OrderDTO> orders = mapper.readValue(inline.toString(), new TypeReference<List<OrderDTO>>() {
            });

            // Flatten the structure
            ObservableList<OrderDetailDTO> orderDetailsList = FXCollections.observableArrayList();
            for (OrderDTO order : orders) {
                for (OrderDetailDTO detail : order.getOrderDetails()) {
                    detail.setOrder(order); // Set reference to the parent order
                    orderDetailsList.add(detail);
                }
            }
            return orderDetailsList;
        }
    }

      public void saveOrder(Order order) {

        System.out.println("Orden " + order.toString());

        //Enviamos order al endpoint
        // Convertir la orden a JSON
        // Convertir la orden a JSON
        JSONObject jsonOrder = new JSONObject();
        jsonOrder.put("userId", order.getUser().getId());

        JSONArray jsonOrderDetails = new JSONArray();
        for (OrderDetail detail : order.getOrderDetails()) {
            JSONObject jsonDetail = new JSONObject();
            jsonDetail.put("price", detail.getPrice());
            jsonDetail.put("amount", detail.getAmount());
            jsonDetail.put("purchasePrice", detail.getPurchasePrice());
            System.out.println("codigo de barras detail.getProduct().getBarcode()" + detail.getProduct().getBarcode());
            jsonDetail.put("productId", detail.getProduct().getId());
            jsonOrderDetails.put(jsonDetail);
        }
        jsonOrder.put("orderDetails", jsonOrderDetails);

        // Imprimir el JSON para depuración
        System.out.println("DEPURACION XXXXX:   " + jsonOrder);

        try {
            // Crear HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Crear HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                  //  .uri(new URI("http://localhost:3000/orders"))
                    .uri(new URI(baseUrl+endpointOrders))
                    .header("Content-Type", "application/json")
                     .header("Authorization", "Bearer " + token) // <-- Agregamos el token aquí
                    .POST(HttpRequest.BodyPublishers.ofString(jsonOrder.toString()))
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                System.out.println("Orden insertada con éxito: " + response.body());
            } else {
              //  this.setStatusSell(false);
                System.out.println("Error al insertar la orden: " + response.statusCode() + " " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
}
