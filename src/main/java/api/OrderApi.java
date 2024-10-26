/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.OrderDetailsResponseDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author albert
 */
public class OrderApi {

    // Método para obtener la lista de pedidos como ObservableList
    public OrderDetailsResponseDTO fetchOrderDetails(String dateStar, String dateEnd, String userName, String departament) throws IOException {
        
            // Codificar el department para que no tenga espacios
            String encodedDepartmentName = URLEncoder.encode(departament, StandardCharsets.UTF_8);
            // Codificar el userName para que no tenga espacios
            String encodedUserName = URLEncoder.encode(userName, StandardCharsets.UTF_8);

        
        String urlString = "http://localhost:3000/orders/solds?startDate=" + dateStar + "&endDate=" + dateEnd + "&userName=" + encodedUserName + "&departmentName=" + encodedDepartmentName;
        URL url = new URL(urlString);
        System.out.println(" url " + urlString);
        //   URL url = new URL("http://localhost:3000/orders");

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
            OrderDetailsResponseDTO response = mapper.readValue(inline.toString(), OrderDetailsResponseDTO.class);

//        // Obtener los detalles de la orden
//        List<OrderDetailDTO> orders = response.getOrderDetails();
//        String totalPrice = response.getTotalPrice();
//
//        // Convertir la lista en un ObservableList
//        ObservableList<OrderDetailDTO> orderDetailsList = FXCollections.observableArrayList(orders);
//
//        // Mostramos el totalPrice (puedes utilizarlo de acuerdo a tus necesidades)
//        System.out.println("Total Price: " + totalPrice);
//            return orderDetailsList;
            return response;
        }
    }

}
