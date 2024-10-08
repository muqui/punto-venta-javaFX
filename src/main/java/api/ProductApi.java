/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import beans.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ProductDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import org.json.JSONObject;

/**
 *
 * @author albert
 */
public class ProductApi {

    public void ProductApi() {

    }
    public ProductDTO getProductByBarcode(String barcode){
          ProductDTO product = new ProductDTO();

        try {

            // Crear un cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Construir la solicitud GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/products/" + barcode))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // Enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar la respuesta
            if (response.statusCode() == 200) {

                // Parsear la respuesta JSON
                JSONObject jsonResponse = new JSONObject(response.body());
                // Crear una instancia de Product y asignar valores

                product.setId(jsonResponse.getInt("id"));
                product.setName(jsonResponse.getString("name"));
                product.setDescription(jsonResponse.getString("description"));
                product.setBarcode(jsonResponse.getString("barcode"));
                // product.setPrice(jsonResponse.getDouble("price"));
                product.setPrice(jsonResponse.getBigDecimal("price"));
                product.setStock(jsonResponse.getInt("stock"));
                product.setImgUrl(jsonResponse.getString("imgUrl"));
                product.setCategoryId(jsonResponse.getInt("categoryId"));
               // product.setTotal(jsonResponse.getBigDecimal("price"));
             //   product.setPurchasePrice(jsonResponse.getBigDecimal("purchasePrice"));
                product.setHowToSell(jsonResponse.getString("howToSell"));

            } else {
                System.out.println("Error en la solicitud: " + response.statusCode());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Producto no encontrado.");
                alert.showAndWait();
            }

        } catch (Exception e) {
        }
        return product;
    }
    public void updateProduct(ProductDTO product) {
        try {
            // Convertir ProductDTO a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(product);

            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/products/" + product.getBarcode())) // Asegúrate de pasar el ID del producto
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonProduct)) // Cambiar POST a PATCH
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                showAlert("Éxito", "Producto actualizado exitosamente.");
            } else {
                String responseBody = response.body();
                System.out.println("Resultado =" + responseBody);

                // Parsear el cuerpo de la respuesta como JSON
                JSONObject jsonResponse = new JSONObject(responseBody);
                // Acceder al campo "message"
                String message = jsonResponse.getString("message");
                System.out.println("Message = " + message);

                // Manejar error de autenticación
                if (message.equalsIgnoreCase("product not found")) {
                    showAlert("Info", "El producto no existe.");
                } else {
                    showAlert("Error", "Error al actualizar el producto. Código de estado: " + response.statusCode());
                }
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
