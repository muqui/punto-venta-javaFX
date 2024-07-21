/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import beans.Order;
import beans.OrderDetail;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author albert
 */
public class CobrarController implements Initializable {

    private Order order;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
        @FXML
    void btnCobrarAction(ActionEvent event) {
            System.out.println("COBRAR");
            saveOrder(order);
             closeWindow(event);

    }

    @FXML
    void btnImprimiryCobrarAction(ActionEvent event) {
        System.out.println("COBRAR E IMPRIMIR");

    }
     public void setOrder(Order order) {
        this.order = order;
        // Aquí puedes hacer algo con el objeto order, como mostrar detalles en la UI
    }
     
     
     public void saveOrder(Order order){
         
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
            System.out.println("codigo de barras detail.getProduct().getBarcode()" +  detail.getProduct().getBarcode());
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
                    .uri(new URI("http://localhost:3000/orders"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonOrder.toString()))
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                System.out.println("Orden insertada con éxito: " + response.body());
            } else {
                System.out.println("Error al insertar la orden: " + response.statusCode() + " " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         
     }
     
       private void closeWindow(ActionEvent event) {
        // Obtener el Stage actual y cerrarlo
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
}
